package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

	private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
	private static final String MOVIES_COLLECTION = "movies";
	private static final String EMBEDDED_MOVIES_COLLECTION = "embedded_movies";

	private final MongoDatabase mongoDatabase;

	MovieService(MongoDatabase mongoDatabase) {
		this.mongoDatabase = mongoDatabase;
	}

	public List<Document> getMovies(String plot) {
		var start = System.currentTimeMillis();
		MongoCollection<Document> collection = getMoviesCollection();

		List<Document> result = collection.aggregate(List.of(
				new Document("$lookup", new Document("from", EMBEDDED_MOVIES_COLLECTION)
						.append("localField", "title")
						.append("foreignField", "title")
						.append("as", "result")),
				new Document("$match", new Document("fullplot", new Document("$regex", plot).append("$options", "i"))),
				new Document("$project", new Document("title", 1)
						.append("year", 1)
						.append("fullplot", 1)
						.append("plot_embedding", "$result.plot_embedding")),
				new Document("$sort", new Document("year", -1))
		)).into(new ArrayList<>());

		long duration = System.currentTimeMillis() - start;
		logger.info("Duration: {} ms, Returned: {} documents", duration, result.size());
		return result;
	}

	public List<Document> findByTitleAndYear(String title, int year) {
		return getMoviesCollection()
				.find(new Document("title", title).append("year", year))
				.into(new ArrayList<>());
	}

	private MongoCollection<Document> getMoviesCollection() {
		return mongoDatabase.getCollection(MOVIES_COLLECTION);
	}
}

