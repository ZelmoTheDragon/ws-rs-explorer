package com.github.ws.rs.explorer.security;

import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;

@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
class Header {

    @JsonbProperty("alg")
    private String algorithm;

    @JsonbProperty("alg")
    private String type;

    public Header() {
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
