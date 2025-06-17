package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
public class MovieService {

	private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

	private final MongoDatabase mongoDatabase;

	MovieService(MongoDatabase mongoDatabase) {
		this.mongoDatabase = mongoDatabase;
	}

	public void simulateConcurrentSearch(String plot1, String plot2) {
		MongoCollection<Document> movies = mongoDatabase.getCollection("movies");
		int numberOfExecutions = 100;
		var executor = Executors.newFixedThreadPool(10);

		IntStream.range(0, numberOfExecutions)
				.mapToObj(i -> CompletableFuture.runAsync(() -> {
					List<Document> result = movies.aggregate(List.of(
							new Document("$match", new Document("fullplot", new Document("$regex", plot1).append("$options", "i"))),
							new Document("$lookup", new Document("from", "embedded_movies")
									.append("localField", "title")
									.append("foreignField", "title")
									.append("as", "result")),
							new Document("$unwind", "$result"),
							new Document("$match", new Document("result.fullplot", new Document("$regex", plot2).append("$options", "i"))),
							new Document("$project", new Document("title", 1)
									.append("year", 1)
									.append("fullplot", 1)
									.append("result.directors", 1))
					)).into(new java.util.ArrayList<>());

					logger.info("Run {}: returned {}", i, result.size() + " documents.");

				}, executor))
				.toList()
				.forEach(CompletableFuture::join);

		executor.shutdown();
	}

}
