package org.acme.services;

import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.merge;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.expr;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.size;
import static java.util.Arrays.asList;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.models.Record;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Variable;

@ApplicationScoped
public class ReconcileService {

        @Inject
        MongoClient mongoClient;

        @ConfigProperty(name = "quarkus.mongodb.database")
        String mongoDatabase;

        public void reconcile(String dataset1, String dataset2) {
                mongoClient
                                .getDatabase(mongoDatabase)
                                .runCommand(
                                                buildAggregateCommand(
                                                                buildPipeline(dataset1, dataset2)));
        }

        private Bson buildAggregateCommand(List<Bson> pipeline) {
                return new Document()
                                .append("aggregate", Record.class.getSimpleName())
                                .append("pipeline", pipeline)
                                .append("cursor", new BsonDocument());

        }

        private List<Bson> buildPipeline(String dataset1, String dataset2) {
                return asList(
                                match(and(in("datasetName", dataset1, dataset2), exists("matches", false))),
                                lookup(
                                                Record.class.getSimpleName(),
                                                asList(
                                                                new Variable<>("datasetName", "$datasetName"),
                                                                new Variable<>("transactionAmount",
                                                                                "$transactionAmount"),
                                                                new Variable<>("transactionDate", "$transactionDate"),
                                                                new Variable<>("transactionReference",
                                                                                "$transactionReference")),
                                                asList(
                                                                match(
                                                                                and(
                                                                                                in("datasetName",
                                                                                                                dataset1,
                                                                                                                dataset2),
                                                                                                exists("matches",
                                                                                                                false),
                                                                                                expr(
                                                                                                                and(
                                                                                                                                new Document().append(
                                                                                                                                                "$ne",
                                                                                                                                                asList("$datasetName",
                                                                                                                                                                "$$datasetName")),
                                                                                                                                new Document().append(
                                                                                                                                                "$eq",
                                                                                                                                                asList("$transactionAmount",
                                                                                                                                                                "$$transactionAmount")),
                                                                                                                                new Document().append(
                                                                                                                                                "$eq",
                                                                                                                                                asList("$transactionReference",
                                                                                                                                                                "$$transactionReference")),
                                                                                                                                Document.parse(
                                                                                                                                                "{$eq:[0,{$dateDiff:{startDate:\"$transactionDate\",endDate:\"$$transactionDate\",unit:\"day\"}}]}")))))),
                                                "matches"),
                                match(size("matches", 1)),
                                merge(Record.class.getSimpleName()));
        }
}
