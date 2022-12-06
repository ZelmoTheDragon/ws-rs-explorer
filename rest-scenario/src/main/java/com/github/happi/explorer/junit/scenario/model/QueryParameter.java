package com.github.happi.explorer.junit.scenario.model;

import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;

/**
 * HTTP query parameter.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public class QueryParameter {

    /**
     * Name.
     */
    @JsonbProperty("name")
    private String name;

    /**
     * Value.
     */
    @JsonbProperty("value")
    private String value;

    /**
     * Default constructor.
     * Required for serialization and deserialization.
     */
    public QueryParameter() {
        // NO-OP
    }

    // Object identity...

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            QueryParameter that = (QueryParameter) o;
            equality = Objects.equals(name, that.name)
                    && Objects.equals(value, that.value);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return new StringBuilder("QueryParameter{")
                .append("name='").append(name).append('\'')
                .append(", value='").append(value).append('\'')
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

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
