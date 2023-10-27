package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.MovieService;
import com.example.MoviesSearcing.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String  main() {
        return "main";
    }

    @GetMapping("/all-movies")
    public ResponseEntity<List<Movie>> getAllMovies(Model model, Pageable pageable) {
        Page<Movie> moviePage = movieService.getAllMovies(pageable);
        model.addAttribute("movies", moviePage);

        return ResponseEntity.ok(moviePage.getContent());
    }

    @GetMapping("/search") //сюди пошук
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

    @PostMapping("/add")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.saveMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }
}
