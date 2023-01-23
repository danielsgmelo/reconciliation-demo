package org.acme.models;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Record extends PanacheMongoEntity {
    @Schema(type = SchemaType.STRING, implementation = String.class)
    public ObjectId id;
    public List<Record> matches;
    public String datasetName;
    public LocalDateTime createdAt = LocalDateTime.now();

    @CsvBindByPosition(position = 0)
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime transactionDate;
    @CsvBindByPosition(position = 1)
    public Integer transactionAmount;
    @CsvBindByPosition(position = 2)
    public String transactionDescription;
    @CsvBindByPosition(position = 3)
    public String transactionReference;

    public Record withDataset(Dataset dataset) {
        this.datasetName = dataset.id.toString();
        return this;
    }
}
