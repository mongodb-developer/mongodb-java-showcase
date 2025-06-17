package com.mongodb;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {

	private final MongoClient mongoClient;

	MovieService(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public List<Document> search(String plot1, String plot2) {

		MongoDatabase database = mongoClient.getDatabase("sample_mflix");
		MongoCollection<Document> collection = database.getCollection("movies");

		return collection.aggregate(List.of(
				Aggregates.match(Filters.regex("fullplot", plot1, "i")),
				Aggregates.lookup("embedded_movies", "title", "title", "result"),
				Aggregates.unwind("$result"),
				Aggregates.match(Filters.regex("result.fullplot", plot2, "i")),
				Aggregates.project(Projections.fields(
						Projections.include("title", "year", "fullplot"),
						Projections.computed("directors", "$result.directors")
				))
		)).into(new ArrayList<>());

//		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$lookup",
//						new Document("from", "embedded_movies")
//								.append("localField", "title")
//								.append("foreignField", "title")
//								.append("as", "result")),
//				new Document("$match",
//						new Document("fullplot",
//								new Document("$regex", plot1)
//										.append("$options", "i"))),
//				new Document("$unwind", "$result"),
//				new Document("$match",
//						new Document("result.fullplot",
//								new Document("$regex", plot2)
//										.append("$options", "i"))),
//				new Document("$project",
//						new Document("title", 1L)
//								.append("year", 1L)
//								.append("fullplot", 1L)
//								.append("result.directors", 1L)),
//				new Document("$sort",
//						new Document("score", -1L))));
//
//		return result.into(new ArrayList<>());

	}
}
