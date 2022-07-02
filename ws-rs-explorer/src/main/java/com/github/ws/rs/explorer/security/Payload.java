package com.github.ws.rs.explorer.security;

import java.util.Objects;
import jakarta.json.JsonObject;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.config.PropertyOrderStrategy;

/**
 * Simple <i>JWT</i> payload.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public class Payload {

    /**
     * Issuer.
     */
    @JsonbProperty("iss")
    private String issuer;

    /**
     * Subject.
     */
    @JsonbProperty("sub")
    private String subject;

    /**
     * Audience.
     */
    @JsonbProperty("aud")
    private String audience;

    /**
     * Expiration time (in second).
     */
    @JsonbProperty("exp")
    private Long expirationTime;

    /**
     * Not before time (in second).
     */
    @JsonbProperty("nbf")
    private Long notBeforeTime;

    /**
     * Issued at time (in second).
     */
    @JsonbProperty("iat")
    private Long issuedAtTime;

    /**
     * <i>JWT</i> identifier.
     */
    @JsonbProperty("jti")
    private String jwtId;

    /**
     * Full <i>JSON</i> object of <i>JWT</i> payload.
     */
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

    // Getters and setters...

    public JsonObject getRawData() {
        return rawData;
    }

    public void setRawData(final JsonObject rawData) {
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
