package com.mongodb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

	private final MovieService movieService;

	MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@GetMapping("/simulate-load")
	public ResponseEntity<Void> search(
			@RequestParam String plot1,
			@RequestParam String plot2
	) {
		movieService.simulateConcurrentSearch(plot1, plot2);
		return ResponseEntity.accepted().build();
	}
}