package com.github.happiexplorer.discovery;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Describes an attribute of an entity.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@XmlRootElement
public class Attribute {

    /**
     * Attribute name.
     */
    @JsonbProperty("name")
    @XmlElement(name = "name")
    private String name;

    /**
     * Attribute type.
     */
    @JsonbProperty("type")
    @XmlElement(name = "type")
    private String type;

    /**
     * Constraints.
     */
    @JsonbProperty("constraints")
    @XmlElement(name = "constraints")
    private Set<Constraint> constraints;

    /**
     * Default constructor.
     */
    public Attribute() {
        this.constraints = new HashSet<>();
    }

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var attribute = (Attribute) o;
            equality = Objects.equals(name, attribute.name)
                    && Objects.equals(type, attribute.type)
                    && Objects.equals(constraints, attribute.constraints);

        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, constraints);
    }

    @Override
    public String toString() {
        return new StringBuilder("Attribute{")
                .append("name='").append(name).append('\'')
                .append(", type='").append(type).append('\'')
                .append(", constraints=").append(constraints)
                .append('}')
                .toString();
    }

    // Getters & Setters...

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(final Set<Constraint> constraints) {
        this.constraints = constraints;
    }
}
