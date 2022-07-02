package endpoint;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import util.WebContext;

class CustomerIT {

    private static final String ENDPOINT = String.join("/", WebContext.API_BASE_URL, "entity", "customer");

    CustomerIT() {
    }

    @Test
    void testEndpointIsOnline() {

        RestAssured
                .when()
                .get(ENDPOINT)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }
}
