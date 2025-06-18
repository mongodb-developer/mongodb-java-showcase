# mongodb-java-showcase
A collection of practical examples using MongoDB with Java. This showcase includes code samples, experiments, and common patterns for working with MongoDB in different scenarios

# Getting Started
## Set your connection string
Before running, set the MONGODB_URI environment variable with your connection string:

```
export MONGODB_URI="mongodb+srv://<username>:<password>@<cluster>"
```

## Run the application
Use Spring Boot to start the app:

```
./mvnw spring-boot:run
```

Or, if you prefer using Maven directly:

```
mvn spring-boot:run
```

## Access the endpoints
Once running, you can test the endpoints like:

```
GET http://localhost:8080/movies/enriched-details?plot=snow

GET http://localhost:8080/movies/by-title-year?title=Titanic&year=1997
```