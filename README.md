# reconciliation-demo

This demo project performs a financial reconciliation between csv datasets.

Datasets uploaded to the server are saved to a MongoDB collection.

Records in the csv file are matched according to the reconciliation stategy below, in a MongoDB Aggregation Pipeline.

### Reconciliation strategy

* `transactionDate`: The date portion of the timestamp must be an exact match. The time portion is ignored
* `transactionAmount`: Must be an exact match
* `transactionDescription`: Ignored
* `transactionReference`: Must be an exact match

### Local development quick start

Requirements:

* VS Code + Dev Containers extension
* docker
* docker-compose

Steps:

1. Open this project in VS Code, inside a dev container

2. Open a new terminal in VS Code and run:

```bash
 quarkus dev
 ```

3. Go to <http://localhost:8080/q/swagger-ui> and play with the available endpoints.
