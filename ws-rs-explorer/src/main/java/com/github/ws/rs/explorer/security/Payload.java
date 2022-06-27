package com.github.ws.rs.explorer.security;

import java.util.Objects;
import jakarta.json.JsonObject;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.config.PropertyOrderStrategy;

@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
class Payload {

    @JsonbProperty("iss")
    private String issuer;

    @JsonbProperty("sub")
    private String subject;

    @JsonbProperty("aud")
    private String audience;

    @JsonbProperty("exp")
    private Long expirationTime;

    @JsonbProperty("nbf")
    private Long notBeforeTime;

    @JsonbProperty("iat")
    private Long issuedAtTime;

    @JsonbProperty("jti")
    private String jwtId;

    @JsonbTransient
    private transient JsonObject rawData;

    public Payload() {
    }

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        } else if (o == null || getClass() != o.getClass()) {
            equality = false;
        } else {
            var payload = (Payload) o;
            equality = Objects.equals(issuer, payload.issuer)
                    && Objects.equals(subject, payload.subject)
                    && Objects.equals(audience, payload.audience)
                    && Objects.equals(expirationTime, payload.expirationTime)
                    && Objects.equals(notBeforeTime, payload.notBeforeTime)
                    && Objects.equals(issuedAtTime, payload.issuedAtTime)
                    && Objects.equals(jwtId, payload.jwtId);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, subject, audience, expirationTime, notBeforeTime, issuedAtTime, jwtId);
    }

    JsonObject getRawData() {
        return rawData;
    }

    void setRawData(final JsonObject rawData) {
        this.rawData = rawData;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(final String audience) {
        this.audience = audience;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(final Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Long getNotBeforeTime() {
        return notBeforeTime;
    }

    public void setNotBeforeTime(final Long notBeforeTime) {
        this.notBeforeTime = notBeforeTime;
    }

    public Long getIssuedAtTime() {
        return issuedAtTime;
    }

    public void setIssuedAtTime(final Long issuedAtTime) {
        this.issuedAtTime = issuedAtTime;
    }

    public String getJwtId() {
        return jwtId;
    }

    public void setJwtId(final String jwtId) {
        this.jwtId = jwtId;
    }
}
