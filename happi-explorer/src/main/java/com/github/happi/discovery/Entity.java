package com.github.happi.discovery;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Describes an entity.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@XmlRootElement
public class Entity {

    /**
     * Entity type name.
     */
    @JsonbProperty("type")
    @XmlElement(name = "type")
    private String type;

    /**
     * Entity attributes.
     */
    @JsonbProperty("attributes")
    @XmlElement(name = "attributes")
    private Set<Attribute> attributes;

    /**
     * Default constructor.
     */
    public Entity() {
        this.attributes = new HashSet<>();
    }

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var entity = (Entity) o;
            equality = Objects.equals(type, entity.type)
                    && Objects.equals(attributes, entity.attributes);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, attributes);
    }

    @Override
    public String toString() {
        return new StringBuilder("Entity{")
                .append("type='").append(type).append('\'')
                .append(", attributes=").append(attributes)
                .append('}')
                .toString();
    }

    // Getters & Setters...

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(final Set<Attribute> attributes) {
        this.attributes = attributes;
    }
}
