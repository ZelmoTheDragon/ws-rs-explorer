package endpoint;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import util.WebContext;

class DebugIT {

    DebugIT() {
    }

    @Test
    void testServerIsOnline() {

        RestAssured
                .when()
                .get(WebContext.BASE_URL)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    void testApplicationIsOnline() {
        RestAssured
                .when()
                .get(WebContext.APP_BASE_URL)
                .then()
                .assertThat()
                .statusCode(200);
    }
}
