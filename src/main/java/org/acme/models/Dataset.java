package org.acme.models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class Dataset extends PanacheMongoEntity {
    @Schema(type = SchemaType.STRING, implementation = String.class)
    public ObjectId id;
    public String name;
    public LocalDateTime createdAt = LocalDateTime.now();
    public long totalRecords;

    public Dataset() {
    }
}
