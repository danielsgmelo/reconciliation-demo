package org.acme.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;

import org.acme.models.Dataset;
import org.acme.models.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;

@QuarkusTest
public class ReconcileServiceTest {

    @InjectSpy
    ReconcileService reconcileService;

    Dataset dataset1;
    Dataset dataset2;
    Record record1;
    Record record2;

    final String query = "{ 'datasetName': ?1, 'matches': { '$exists': true } }";

    @BeforeEach
    public void setup() {
        dataset1 = new Dataset();
        dataset2 = new Dataset();
        Dataset.persist(dataset1, dataset2);
        record1 = new Record();
        record1.transactionAmount = 100;
        record1.transactionDate = LocalDateTime.parse("2007-12-03T10:15:30");
        record1.transactionDescription = "Inspire Fitness Co";
        record1.transactionReference = "0cb51ed2-540a-4886-9b90-8a49c2903ee2";
        record1.datasetName = dataset1.id.toString();
        record2 = new Record();
        record2.transactionAmount = 100;
        record2.transactionDate = LocalDateTime.parse("2007-12-03T10:15:30");
        record2.transactionDescription = "Inspire Fitness Co";
        record2.transactionReference = "0cb51ed2-540a-4886-9b90-8a49c2903ee2";
        record2.datasetName = dataset2.id.toString();
        Record.persist(record1, record2);
    }

    @Test
    public void identicalRecordsTest() {
        reconcileService.reconcile(dataset1.id.toString(), dataset2.id.toString());
        assertEquals(1, Record.count(query, dataset1.id.toString()));
        assertEquals(1, Record.count(query, dataset2.id.toString()));

        Mockito.verify(reconcileService, Mockito.times(1)).reconcile(anyString(), anyString());
    }

    @Test
    public void differentTransactionTimeButSameDayShouldNotMatterTest() {
        record2.transactionDate = LocalDateTime.parse("2007-12-03T12:15:30");
        record2.update();
        reconcileService.reconcile(dataset1.id.toString(), dataset2.id.toString());
        assertEquals(1, Record.count(query, dataset1.id.toString()));
        assertEquals(1, Record.count(query, dataset2.id.toString()));
    }

    @Test
    public void differentTransactionDayTest() {
        record2.transactionDate = LocalDateTime.parse("2007-12-01T12:15:30");
        record2.update();
        reconcileService.reconcile(dataset1.id.toString(), dataset2.id.toString());
        assertEquals(0, Record.count(query, dataset1.id.toString()));
        assertEquals(0, Record.count(query, dataset2.id.toString()));
    }

    @Test
    public void differentTransactionDescriptionShouldNotMatterTest() {
        record2.transactionDescription = "****Inspire **** Co";
        record2.update();
        reconcileService.reconcile(dataset1.id.toString(), dataset2.id.toString());
        assertEquals(1, Record.count(query, dataset1.id.toString()));
        assertEquals(1, Record.count(query, dataset2.id.toString()));
    }

    @Test
    public void differentTransactionReferenceTest() {
        record2.transactionReference = "0cb****2-540a-4886-9b90-8a49c****ee2";
        record2.update();
        reconcileService.reconcile(dataset1.id.toString(), dataset2.id.toString());
        assertEquals(0, Record.count(query, dataset1.id.toString()));
        assertEquals(0, Record.count(query, dataset2.id.toString()));
    }

    @Test
    public void differentTransactionAmountTest() {
        record2.transactionAmount = 101;
        record2.update();
        reconcileService.reconcile(dataset1.id.toString(), dataset2.id.toString());
        assertEquals(0, Record.count(query, dataset1.id.toString()));
        assertEquals(0, Record.count(query, dataset2.id.toString()));
    }

}
