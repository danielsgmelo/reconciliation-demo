rs.initiate({
  _id: "rs0",
  members: [
    { _id: 0, host: "mongo1:27017", priority: 2 },
    { _id: 1, host: "mongo2:27017" },
    { _id: 2, host: "mongo3:27017" },
  ],
});

db = connect("mongodb://mongo1:27017,mongo2:27017,mongo3:27017/db");

db.Record.createIndex({
  datasetName: 1,
  transactionReference: 1,
  transactionAmount: 1,
  transactionDate: 1,
});

// db.Record.createIndex({ createdAt: 1 }, { expireAfterSeconds: 600 });
// db.Dataset.createIndex({ createdAt: 1 }, { expireAfterSeconds: 600 });
