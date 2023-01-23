package org.acme.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;

import org.acme.TestUtils;
import org.acme.models.Dataset;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class DatasetResourceTest {
        @Test
        public void createTest() {
                given()
                                .multiPart("file", new File("src/test/resources/DatasetTest1.csv"),
                                                "text/csv")
                                .multiPart("fileName", "DatasetTest1.csv")
                                .when()
                                .post("/datasets")
                                .then()
                                .statusCode(HttpStatus.SC_CREATED)
                                .body("totalRecords", is(11),
                                                "name", is("DatasetTest1.csv"));
        }

        @Test
        public void createWithInvalidInputFileTest() {
                given()
                                .multiPart("file", new File("src/test/resources/InvalidDatasetTest.csv"),
                                                "text/csv")
                                .multiPart("fileName", "InvalidDatasetTest.csv")
                                .when()
                                .post("/datasets")
                                .then()
                                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                                .body("details", containsString("Error parsing CSV line: 2. [🐈]"));
        }

        @Test
        public void deleteTest() {
                given()
                                .when()
                                .delete("/datasets/" + TestUtils.createDatasetAndGetId())
                                .then()
                                .statusCode(HttpStatus.SC_NO_CONTENT);
        }

        @Test
        public void getTest() {
                String id = TestUtils.createDatasetAndGetId();
                given()
                                .when()
                                .get("/datasets/" + id)
                                .then()
                                .statusCode(HttpStatus.SC_OK)
                                .body("id", is(id));
        }

        @Test
        public void listAllTest() {
                given()
                                .when()
                                .get("/datasets/")
                                .then()
                                .statusCode(HttpStatus.SC_OK)
                                .body("size()", is(Math.toIntExact(Dataset.count())));
        }

        @Test
        public void listRecordsTest() {
                String dId = TestUtils.createDatasetAndGetId();
                String rId = TestUtils.createRecordAndGetId(dId);
                given()
                                .when()
                                .get("/datasets/" + dId + "/records")
                                .then()
                                .statusCode(HttpStatus.SC_OK)
                                .body("size()", is(1),
                                                "[0].id", is(rId));
        }
}