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
    public String getAllMovies(Model model, Pageable pageable) {
        Page<Movie> moviePage = movieService.getAllMovies(pageable);
        model.addAttribute("movies", moviePage.getContent());
        return "all-movies";
    }

    @GetMapping("/all-movies-json")
    @ResponseBody
    public List<Movie> getAllMoviesJson(Pageable pageable) {
        Page<Movie> moviePage = movieService.getAllMovies(pageable);
        return moviePage.getContent();
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Page<Movie>> searchMovies(
            @RequestParam(name = "title", required = true) String title,
            Pageable pageable) {
        Page<Movie> resultPage;

        if (title.startsWith("partial:")) {
            String partialTitle = title.substring("partial:".length());
            resultPage = movieService.findByTitleIgnoreCaseContaining(partialTitle, pageable);
        } else {
            resultPage = movieService.findByTitle(title, pageable);
        }

        return resultPage.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(resultPage);
    }


    @PostMapping("/add") // in the future
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.saveMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }
}
