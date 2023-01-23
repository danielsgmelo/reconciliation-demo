package org.acme.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.acme.TestUtils;
import org.acme.models.Record;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RecordResourceTest {

        @Test
        public void getTest() {
                given()
                                .when()
                                .get("/records/" + TestUtils.createRecordAndGetId(null))
                                .then()
                                .statusCode(HttpStatus.SC_OK);
        }

        @Test
        public void listAllTest() {
                given()
                                .when()
                                .get("/records/")
                                .then()
                                .statusCode(HttpStatus.SC_OK)
                                .body("size()", is(Math.toIntExact(Record.count())));
        }
}