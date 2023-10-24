package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.MovieService;
import com.example.MoviesSearcing.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.saveMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @GetMapping("/all-movies")
    public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
        Page<Movie> resultPage = movieService.getAllMovies(pageable);

        return resultPage.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(resultPage);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Movie>> searchMovies(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "year", required = false) Integer year,
            Pageable pageable) {
        Page<Movie> resultPage;

        if (title != null) {
            if (title.startsWith("partial:")) {
                String partialTitle = title.substring("partial:".length());
                resultPage = movieService.findByTitleIgnoreCaseContaining(partialTitle, pageable);
            } else {
                resultPage = movieService.findByTitle(title, pageable);
            }
        } else if (year != null) {
            resultPage = movieService.findByYear(year, pageable);
        } else {
            resultPage = movieService.getAllMoviesSortedByYear(pageable);
        }

        return resultPage.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(resultPage);
    }
}
