package org.acme.resources;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.acme.models.Dataset;
import org.acme.models.Record;
import org.acme.services.DatasetService;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ReconcileResourceTest {

        @Inject
        DatasetService datasetService;

        @Test
        public void reconcileTest() throws FileNotFoundException {
                Dataset dataset1 = datasetService.loadCsvDatasetIntoMongo("DatasetTest1.csv",
                                new FileInputStream("src/test/resources/DatasetTest1.csv"));
                Dataset dataset2 = datasetService.loadCsvDatasetIntoMongo("DatasetTest2.csv",
                                new FileInputStream("src/test/resources/DatasetTest2.csv"));

                given()
                                .when()
                                .post("/reconcile/" + dataset1.id + "/" + dataset2.id)
                                .then()
                                .statusCode(HttpStatus.SC_NO_CONTENT);

                String query = "{ 'datasetName': ?1, 'matches': { '$exists': true } }";
                assertEquals(6, Record.count(query, dataset1.id.toString()));
                assertEquals(6, Record.count(query, dataset2.id.toString()));
        }
}