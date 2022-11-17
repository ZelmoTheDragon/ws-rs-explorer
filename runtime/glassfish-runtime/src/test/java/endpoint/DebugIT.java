package endpoint;

import java.util.UUID;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import util.WebContext;

class DebugIT {

    private static final String FAKE_ENDPOINT = String.join("/", WebContext.API_BASE_URL, "entity", "fake");

    DebugIT() {
    }

    @Test
    void testServerIsOnline() {

        RestAssured
                .when()
                .get(WebContext.BASE_URL)
                .then()
                .assertThat()
                .statusCode(
                        Matchers.anyOf(
                                Matchers.is(HttpStatus.SC_OK),
                                Matchers.is(HttpStatus.SC_NOT_FOUND)
                        ));
    }

    @Test
    void testApplicationIsOnline() {
        RestAssured
                .when()
                .get(WebContext.APP_BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void testFakeEndpoint() {

        RestAssured
                .when()
                .options(FAKE_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testFakeEndpointId() {
        var path = String.join("/", FAKE_ENDPOINT, UUID.randomUUID().toString());

        RestAssured
                .when()
                .options(path)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
