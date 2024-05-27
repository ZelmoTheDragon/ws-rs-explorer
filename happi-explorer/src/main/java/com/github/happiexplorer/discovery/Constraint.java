package com.github.happiexplorer.discovery;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Describes the constraint of an entity.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@XmlRootElement
public class Constraint {

    /**
     * Constraint name.
     */
    @JsonbProperty("name")
    @XmlElement(name = "name")
    private String name;

    /**
     * Description.
     */
    @JsonbProperty("description")
    @XmlElement(name = "description")
    private String description;

    /**
     * Constraint specification.
     */
    @JsonbProperty("specifications")
    @XmlElement(name = "specifications")
    private Map<String, Object> specifications;

    /**
     * Default constructor.
     */
    public Constraint() {
        this.specifications = new HashMap<>();
    }

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var that = (Constraint) o;
            equality = Objects.equals(name, that.name)
                    && Objects.equals(description, that.description);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return new StringBuilder("Constraint{")
                .append("name='").append(name).append('\'')
                .append("description='").append(description).append('\'')
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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Map<String, Object> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(final Map<String, Object> specifications) {
        this.specifications = specifications;
    }
}
