package com.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

	@Bean
	public MongoDatabase mongoDatabase(
			MongoClient mongoClient,
			@Value("${spring.data.mongodb.database}") String dbName) {
		return mongoClient.getDatabase(dbName);
	}
}
