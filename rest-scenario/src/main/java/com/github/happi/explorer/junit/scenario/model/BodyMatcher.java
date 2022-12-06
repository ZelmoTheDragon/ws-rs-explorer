package com.github.happi.explorer.junit.scenario.model;

import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;

/**
 * Assertion for the body of the HTTP response.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public class BodyMatcher {

    /**
     * JSON path.
     * Use <i>RestAssured</i> syntax.
     */
    @JsonbProperty("jsonPath")
    private String jsonPath;

    /**
     * Logical operator.
     *
     * @see com.github.happi.explorer.junit.scenario.Scenarios
     */
    @JsonbProperty("operator")
    private String operator;

    /**
     * Value.
     */
    @JsonbProperty("value")
    private String value;

    /**
     * Default constructor.
     * Required for serialization and deserialization.
     */
    public BodyMatcher() {
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
            BodyMatcher that = (BodyMatcher) o;
            equality = Objects.equals(jsonPath, that.jsonPath)
                    && Objects.equals(operator, that.operator)
                    && Objects.equals(value, that.value);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonPath, operator, value);
    }

    @Override
    public String toString() {
        return new StringBuilder("BodyMatcher{")
                .append("jsonPath='").append(jsonPath).append('\'')
                .append(", operator='").append(operator).append('\'')
                .append(", value='").append(value).append('\'')
                .append('}')
                .toString();
    }

    // Getters & Setters...

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(final String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(final String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
