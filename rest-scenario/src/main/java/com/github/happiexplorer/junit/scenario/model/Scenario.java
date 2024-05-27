package com.github.happiexplorer.junit.scenario.model;


import com.github.happiexplorer.junit.scenario.StringJsonbAdapter;
import io.restassured.http.Method;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.json.bind.config.PropertyOrderStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A test scenario.
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public class Scenario {

    /**
     * Title.
     * Human-readable.
     */
    @JsonbProperty("title")
    private String title;

    /**
     * HTTP method.
     */
    @JsonbProperty("method")
    private Method method;

    /**
     * Resource port.
     */
    private Integer port;

    /**
     * Resource path.
     * Can contain expressions like for path parameters.
     */
    @JsonbProperty("path")
    private String path;

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
    @JsonbProperty("queryParameters")
    private List<QueryParameter> queryParameters;

    /**
     * Path parameter resolutions.
     */
    @JsonbProperty("pathParameters")
    private List<PathParameter> pathParameters;

    /**
     * Header parameters.
     */
    @JsonbProperty("headerParameters")
    private List<HeaderParameter> headerParameters;

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
        this.queryParameters = new ArrayList<>();
        this.pathParameters = new ArrayList<>();
        this.headerParameters = new ArrayList<>();
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
                    && Objects.equals(port, scenario.port)
                    && Objects.equals(path, scenario.path)
                    && Objects.equals(statusCode, scenario.statusCode)
                    && Objects.equals(body, scenario.body)
                    && Objects.equals(requiredGeneratedJWT, scenario.requiredGeneratedJWT);
        }
        return equality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, method, port, path, statusCode, body, requiredGeneratedJWT);
    }

    @Override
    public String toString() {
        return new StringBuilder("Scenario{")
                .append("title='").append(title).append('\'')
                .append(", method='").append(method).append('\'')
                .append(", port='").append(port).append('\'')
                .append(", path='").append(path).append('\'')
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

    public Integer getPort() {
        return port;
    }

    public void setPort(final Integer port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
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

    public List<QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(final List<QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public List<PathParameter> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(final List<PathParameter> pathParameters) {
        this.pathParameters = pathParameters;
    }

    public List<HeaderParameter> getHeaderParameters() {
        return headerParameters;
    }

    public void setHeaderParameters(final List<HeaderParameter> headerParameters) {
        this.headerParameters = headerParameters;
    }

    public List<BodyMatcher> getMatchers() {
        return matchers;
    }

    public void setMatchers(final List<BodyMatcher> matchers) {
        this.matchers = matchers;
    }
}
