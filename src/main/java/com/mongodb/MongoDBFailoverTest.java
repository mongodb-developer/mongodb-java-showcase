package com.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDBFailoverTest {
    private static final String CONNECTION_STRING = "<YOUR_CONNECTION_STRING>";
    private static final Logger logger = LoggerFactory.getLogger(MongoDBFailoverTest.class);

    public static void main(String[] args) {
        try (MongoClient client = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase db = client.getDatabase("test");
            MongoCollection<Document> collection = db.getCollection("failover_test");

            int counter = 0;
            while (true) {
                long startTime = System.currentTimeMillis();

                try {
                    Document doc = new Document("counter", counter)
                            .append("timestamp", new java.util.Date());
                    collection.insertOne(doc);

                    collection.find().sort(new Document("counter", -1)).first();

                    long duration = System.currentTimeMillis() - startTime;

                    logger.info("{}", String.format(
                            "Attempt #%d → Write & Read completed in %dms%s",
                            counter, duration,
                            duration > 5000 ? " (This is slower than expected)" : ""
                    ));

                    counter++;

                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - startTime;
                    System.out.printf("FAIL #%d - %dms - %s%n", counter, duration, e.getMessage());
                }
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}