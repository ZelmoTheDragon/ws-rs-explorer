package com.github.happi.explorer.junit.scenario.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.json.bind.config.PropertyOrderStrategy;

import com.github.happi.explorer.junit.scenario.StringJsonbAdapter;
import io.restassured.http.Method;

/**
 * A test scenario.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public class Scenario {

    /**
     * Title.
     * Human readable.
     */
    @JsonbProperty("title")
    private String title;

    /**
     * HTTP method.
     */
    @JsonbProperty("method")
    private Method method;

    /**
     * Complete URL.
     * Can contains expression like for path parameters.
     */
    @JsonbProperty("url")
    private String url;

    /**
     * Whether the current scenario is skipped or not.
     */
    @JsonbProperty("skip")
    private Boolean skip;

    /**
     * Whether the current scenario required an authentication with token or not.
     */
    @JsonbProperty("requiredGeneratedJWT")
    private Boolean requiredGeneratedJWT;

    /**
     * HTTP request body.
     */
    @JsonbTypeAdapter(StringJsonbAdapter.class)
    @JsonbProperty("body")
    private String body;

    /**
     * Expected response status.
     */
    @JsonbProperty("statusCode")
    private Integer statusCode;

    /**
     * Query parameters.
     */
    @JsonbProperty("queries")
    private List<QueryParameter> queries;

    /**
     * Path parameter resolutions.
     */
    @JsonbProperty("paths")
    private List<PathParameter> paths;

    /**
     * Header parameters.
     */
    @JsonbProperty("headers")
    private List<HeaderParameter> headers;

    /**
     * Matchers for response body.
     */
    @JsonbProperty("matchers")
    private List<BodyMatcher> matchers;

    /**
     * Default constructor.
     * Required for serialization and deserialization.
     */
    public Scenario() {
        this.queries = new ArrayList<>();
        this.paths = new ArrayList<>();
        this.headers = new ArrayList<>();
        this.matchers = new ArrayList<>();
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
            Scenario scenario = (Scenario) o;
            equality = Objects.equals(title, scenario.title)
                    && Objects.equals(method, scenario.method)
                    && Objects.equals(url, scenario.url)
                    && Objects.equals(statusCode, scenario.statusCode)
                    && Objects.equals(body, scenario.body)
                    && Objects.equals(requiredGeneratedJWT, scenario.requiredGeneratedJWT);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, method, url, statusCode, body, requiredGeneratedJWT);
    }

    @Override
    public String toString() {
        return new StringBuilder("Scenario{")
                .append("title='").append(title).append('\'')
                .append(", method='").append(method).append('\'')
                .append(", uri='").append(url).append('\'')
                .append(", statusCode='").append(statusCode).append('\'')
                .append(", body='").append(body).append('\'')
                .append(", skip='").append(skip).append('\'')
                .append(", requiredGeneratedJWT='").append(requiredGeneratedJWT).append('\'')
                .append('}')
                .toString();
    }

    // Getters & Setters...

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(final Method method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public Boolean getSkip() {
        return skip;
    }

    public void setSkip(final Boolean skip) {
        this.skip = skip;
    }

    public Boolean getRequiredGeneratedJWT() {
        return requiredGeneratedJWT;
    }

    public void setRequiredGeneratedJWT(final Boolean requiredGeneratedJWT) {
        this.requiredGeneratedJWT = requiredGeneratedJWT;
    }

    public List<QueryParameter> getQueries() {
        return queries;
    }

    public void setQueries(final List<QueryParameter> queries) {
        this.queries = queries;
    }

    public List<PathParameter> getPaths() {
        return paths;
    }

    public void setPaths(final List<PathParameter> paths) {
        this.paths = paths;
    }

    public List<HeaderParameter> getHeaders() {
        return headers;
    }

    public void setHeaders(final List<HeaderParameter> headers) {
        this.headers = headers;
    }

    public List<BodyMatcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(final List<BodyMatcher> matchers) {
        this.matchers = matchers;
    }
}
