package com.github.happi.explorer.junit.scenario;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import jakarta.json.bind.JsonbBuilder;

import com.github.happi.explorer.junit.scenario.model.Scenario;
import com.github.happi.explorer.junit.util.TokenGenerator;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Utility class for scenario test with JUnit 5.
 */
public final class Scenarios {

    /**
     * A map for pattern matching in the body of the response.
     */
    private static final Map<String, Function<Object, Matcher<?>>> MATCHERS = Map.ofEntries(
            Map.entry("equalTo", Matchers::equalTo),
            Map.entry("greaterThan", v -> Matchers.greaterThan((Comparable) v)),
            Map.entry("greaterThanOrEqualTo", v -> Matchers.greaterThanOrEqualTo((Comparable) v)),
            Map.entry("lessThan", v -> Matchers.lessThan((Comparable) v)),
            Map.entry("lessThanOrEqualTo", v -> Matchers.lessThanOrEqualTo((Comparable) v)),
            Map.entry("not.equalTo", v -> Matchers.not(Matchers.equalTo(v))),
            Map.entry("equalToIgnoringCase", v -> Matchers.equalToIgnoringCase((String) v)),
            Map.entry("not.equalToIgnoringCase", v -> Matchers.not(Matchers.equalToIgnoringCase((String) v))),
            Map.entry("empty", v -> Matchers.empty()),
            Map.entry("not.empty", v -> Matchers.not(Matchers.empty())),
            Map.entry("hasItem", Matchers::hasItem),
            Map.entry("not.hasItem", v -> Matchers.not(Matchers.hasItem(v))),
            Map.entry("anything", v -> Matchers.anything())
    );

    /**
     * Internal constructor.
     * Instance does not allow.
     */
    private Scenarios() {
        throw new UnsupportedOperationException("Instance not allowed");
    }

    /**
     * Call an endpoint with a scenario data.
     *
     * @param reporter JUnit reporter for logging test
     * @param scenario An entity for a test scenario
     */
    public static void callEndpoint(final Scenario scenario, final TestReporter reporter) {
        reporter.publishEntry(scenario.getTitle());

        var query = RestAssured
                .given()
                .when();

        appendPaths(query, scenario);
        appendQueries(query, scenario);
        appendHeaders(query, scenario);
        setBody(query, scenario);
        var response = execute(query, scenario);
        validateResponse(response, scenario);
    }

    /**
     * Create JUnit parameter arguments for a test.
     *
     * @return A stream of JUnit argument
     */
    public static Stream<Arguments> createArguments() {
        try {
            var resource = Optional
                    .ofNullable(Scenarios.class.getClassLoader().getResource("scenario"))
                    .orElseThrow(() -> new IllegalStateException("Wrong resource URL !"));

            var scenarioPath = Path.of(resource.toURI());
            try (var paths = Files.find(scenarioPath, Integer.MAX_VALUE, (p, a) -> a.isRegularFile() && Files.isReadable(p))) {

                var scenarios = paths
                        .sorted()
                        .map(Scenarios::loadFile)
                        .map(Scenarios::readJsonString)
                        .filter(s -> Objects.equals(Boolean.FALSE, s.getSkip()))
                        .toList();

                return scenarios
                        .stream()
                        .map(Arguments::arguments);

            }
        } catch (IOException | URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Load a JSON file as string.
     *
     * @param path Path of the JSON file
     * @return A JSON file as string
     */
    private static String loadFile(final Path path) {
        try {
            return Files.readString(path);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Convert a string JSON to an object.
     *
     * @param json A string JSON
     * @return A scenario
     */
    private static Scenario readJsonString(final String json) {
        try (var builder = JsonbBuilder.create()) {
            return builder.fromJson(json, Scenario.class);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Append path parameters.
     *
     * @param query    Current request
     * @param scenario Scenario data
     */
    private static void appendPaths(final RequestSpecification query, final Scenario scenario) {
        if (Objects.nonNull(scenario.getPathParameters())) {
            for (var h : scenario.getPathParameters()) {
                query.pathParam(h.getName(), h.getValue());
            }
        }
    }

    /**
     * Append queries parameters.
     *
     * @param query    Current request
     * @param scenario Scenario data
     */
    private static void appendQueries(final RequestSpecification query, final Scenario scenario) {
        if (Objects.nonNull(scenario.getQueryParameters())) {
            for (var h : scenario.getPathParameters()) {
                query.queryParam(h.getName(), h.getValue());
            }
        }
    }

    /**
     * Append headers to the current HTTP request.
     * If an authentication is required, append the header <i>Authorization</i> with a <i>Bearer</i> token.
     *
     * @param query    Current request
     * @param scenario Scenario data
     */
    private static void appendHeaders(final RequestSpecification query, final Scenario scenario) {
        if (Objects.nonNull(scenario.getHeaderParameters())) {
            for (var h : scenario.getHeaderParameters()) {
                query.header(h.getName(), h.getValue());
            }
        }
        if (Objects.equals(Boolean.TRUE, scenario.getRequiredGeneratedJWT())) {
            var jwt = TokenGenerator.generateNewToken();
            query.header("Authorization", "Bearer " + jwt);
        }
    }

    /**
     * Set the body of the current request.
     *
     * @param query    Current request
     * @param scenario Scenario data
     */
    private static void setBody(final RequestSpecification query, final Scenario scenario) {
        if (Objects.nonNull(scenario.getBody())) {
            query.body(scenario.getBody());
        }
    }

    /**
     * Execute the current request.
     *
     * @param query    Current request
     * @param scenario Scenario data
     * @return A response for JUnit assertions
     */
    private static ValidatableResponse execute(final RequestSpecification query, final Scenario scenario) {
        var m = Objects.requireNonNull(scenario.getMethod(), "Missing method name !");
        var path = Objects.requireNonNull(scenario.getPath(), "Missing path !");
        var port = Optional.ofNullable(scenario.getPort()).orElse(8080);
        query.port(port);
        return query.request(m, path).then();
    }

    /**
     * Validate a response with JUnit assertions.
     *
     * @param response A response for JUnit assertions
     * @param scenario Scenario data
     */
    private static void validateResponse(final ValidatableResponse response, final Scenario scenario) {

        var validationResponse = response
                .assertThat()
                .statusCode(scenario.getStatusCode());

        if (Objects.nonNull(scenario.getMatchers())) {
            for (var m : scenario.getMatchers()) {

                var matcherProvider = MATCHERS.get(m.getOperator());
                if (Objects.isNull(matcherProvider)) {
                    throw new IllegalArgumentException("Unknown operator : " + m.getOperator());
                } else {
                    var matcher = matcherProvider.apply(m.getValue());
                    validationResponse.body(m.getJsonPath(), matcher);
                }
            }
        }
    }

}
