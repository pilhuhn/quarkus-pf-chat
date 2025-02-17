package org.patternfly.chat.bot;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ChatResourceTest {
    @Test
    @Order(2)
    void testHelloEndpoint() {
        given()
          .when().get("/hello?q=quarkus%20version")
          .then()
             .statusCode(200)
                .body(containsString("Quarkus"));
    }

    @Test
    @Order(1)
    void testUpload() {
        given()
                .when().get("/upload?url=https://quarkus.io")
                .then()
                .statusCode(200);
    }
}
