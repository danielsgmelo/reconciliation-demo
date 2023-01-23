package org.acme;

import org.acme.models.Dataset;
import org.acme.models.Record;

public class TestUtils {
    public static String createDatasetAndGetId() {
        Dataset dataset = new Dataset();
        dataset.persist();
        return dataset.id.toString();
    }

    public static String createRecordAndGetId(String datasetId) {
        Record record = new Record();
        record.datasetName = datasetId;
        record.persist();
        return record.id.toString();
    }
}
