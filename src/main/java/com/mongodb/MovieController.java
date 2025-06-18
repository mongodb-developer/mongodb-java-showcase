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

	@GetMapping("/enriched-details")
	public ResponseEntity<List<Document>> search(
			@RequestParam String plot
	) {
		return ResponseEntity.ok(movieService.getMovies(plot));
	}


	@GetMapping("/by-title-year")
	public ResponseEntity<List<Document>> findByTitleAndYear(
			@RequestParam String title,
			@RequestParam int year
	) {
		return ResponseEntity.ok(movieService.findByTitleAndYear(title, year));
	}

}