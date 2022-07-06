package endpoint;

import io.restassured.RestAssured;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
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
                .options(ENDPOINT)
                .then()
                .assertThat()
                .header(HttpHeaders.ALLOW, Matchers.is("OPTIONS, GET, POST"))
                .statusCode(HttpStatus.SC_OK);
    }
}
