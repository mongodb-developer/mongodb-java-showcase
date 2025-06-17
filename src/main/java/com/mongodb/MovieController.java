package com.mongodb;

import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

	private final MovieService movieService;

	MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping("/search")
	public ResponseEntity<List<Document>> search(
			@RequestParam String plot1,
			@RequestParam String plot2
	) {
		List<Document> result = movieService.search(plot1, plot2);
		return ResponseEntity.ok(result);
	}
}