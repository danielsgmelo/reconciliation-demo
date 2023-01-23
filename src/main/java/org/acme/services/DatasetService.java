package org.acme.services;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.acme.models.Dataset;
import org.acme.models.Record;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.opencsv.bean.CsvToBeanBuilder;

@ApplicationScoped
public class DatasetService {

    @ConfigProperty(defaultValue = "100")
    Integer batchSize;

    public Dataset loadCsvDatasetIntoMongo(String name, InputStream inputStream) {
        Dataset dataset = new Dataset();
        dataset.name = name;
        dataset.persist();
        AtomicInteger counter = new AtomicInteger();
        new CsvToBeanBuilder<Record>(new InputStreamReader(inputStream))
                .withType(Record.class)
                .withSkipLines(1)
                .build()
                .stream()
                .parallel()
                .map(r -> r.withDataset(dataset))
                .collect(Collectors.groupingByConcurrent(gr -> counter.getAndIncrement() / batchSize))
                .values()
                .forEach(list -> Record.persist(list));
        dataset.totalRecords = counter.longValue();
        dataset.update();
        return dataset;
    }
}
