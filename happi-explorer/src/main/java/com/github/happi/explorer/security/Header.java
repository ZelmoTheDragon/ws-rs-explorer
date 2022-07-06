package com.github.happi.explorer.security;

import java.util.Objects;
import jakarta.json.JsonObject;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.config.PropertyOrderStrategy;

/**
 * Simple <i>JWT</i> header.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public final class Header {

    /**
     * Signature algorithm.
     */
    @JsonbProperty("alg")
    private String algorithm;

    /**
     * Token type.
     */
    @JsonbProperty("typ")
    private String type;

    /**
     * Full <i>JSON</i> object of <i>JWT</i> header.
     */
    @JsonbTransient
    private transient JsonObject rawData;

    /**
     * Default constructor.
     */
    public Header() {
        // NO-OP
    }

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var header = (Header) o;
            equality = Objects.equals(algorithm, header.algorithm)
                    && Objects.equals(type, header.type);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithm, type);
    }

    // Getters and setters...

    public JsonObject getRawData() {
        return rawData;
    }

    public void setRawData(final JsonObject rawData) {
        this.rawData = rawData;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(final String algorithm) {
        this.algorithm = algorithm;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
