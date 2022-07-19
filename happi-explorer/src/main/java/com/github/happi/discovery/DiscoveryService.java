package com.github.happi.discovery;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * A discovery service.
 * This service shows any data class used in explorer service.
 */
@ApplicationScoped
public class DiscoveryService {

    private static final List<String> EXCLUDE_ANNOTATION_METHODS = List.of(
            "message",
            "groups",
            "payload"
    );

    /**
     * Registered data class.
     */
    private final Set<Entity> entities;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public DiscoveryService() {
        this.entities = new HashSet<>();
    }

    /**
     * Scan a data <i>bean</i>.
     *
     * @param bean Any <i>bean</i> used by explorer service
     */
    public void scan(final Class<?> bean) {

        var attributes = new HashSet<Attribute>();
        var fields = bean.getDeclaredFields();
        for (var f : fields) {

            var attribute = new Attribute();
            String name;
            if (f.isAnnotationPresent(JsonbProperty.class)) {
                var jsonName = f.getAnnotation(JsonbProperty.class);
                name = jsonName.value();
            } else if (f.isAnnotationPresent(XmlElement.class)) {
                var xmlName = f.getAnnotation(XmlElement.class);
                name = xmlName.name();
            } else if (f.isAnnotationPresent(XmlAttribute.class)) {
                var xmlName = f.getAnnotation(XmlAttribute.class);
                name = xmlName.name();
            } else {
                name = f.getName();
            }

            var constraints = new HashSet<Constraint>();
            var annotations = f.getAnnotations();
            for (var a : annotations) {
                var type = a.annotationType();
                if (type.getPackageName().startsWith("jakarta.validation.constraints")) {
                    var constraint = createConstraint(a);
                    constraints.add(constraint);
                }
            }

            attribute.setName(name);
            attribute.setType(f.getType().getSimpleName());
            attribute.setConstraints(constraints);
            attributes.add(attribute);
        }
        var entity = new Entity();
        entity.setType(bean.getSimpleName());
        entity.setAttributes(attributes);
        this.entities.add(entity);
    }

    /**
     * Register a new data class manually.
     *
     * @param dataClass A new data class
     */
    public void registerDataClass(final Entity dataClass) {
        this.entities.add(dataClass);
    }

    /**
     * Get a copy of registered data class.
     *
     * @return A set of registered entities
     */
    public Set<Entity> getEntities() {
        return Set.copyOf(entities);
    }

    /**
     * Create a constraint based on <i>Bean Validation</i> annotation.
     *
     * @param beanValidationAnnotation <i>Bean Validation</i> annotation
     * @return A constraint
     */
    private static Constraint createConstraint(final Annotation beanValidationAnnotation) {

        var constraint = new Constraint();
        var specifications = new HashMap<String, Object>();
        var type = beanValidationAnnotation.annotationType();
        var methods = type.getDeclaredMethods();

        for (var m : methods) {
            var name = m.getName();
            if (!EXCLUDE_ANNOTATION_METHODS.contains(name)) {
                try {
                    specifications.put(name, m.invoke(beanValidationAnnotation));
                } catch (InvocationTargetException | IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        constraint.setName(beanValidationAnnotation.annotationType().getName());
        constraint.setSpecifications(specifications);
        return constraint;
    }
}
