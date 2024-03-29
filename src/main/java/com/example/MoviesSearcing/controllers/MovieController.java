package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.services.MovieService;
import com.example.MoviesSearcing.models.Movie;
import com.example.MoviesSearcing.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Movie>> filterMovies(
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "sort", required = false) String sort,
            Pageable pageable) {
        if (sort != null) {
            if (sort.equals("rating_asc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("rating").ascending());
            } else if (sort.equals("rating_desc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("rating").descending());
            }
        }
        Page<Movie> resultPage;
        if (genre != null && year != null) {
            resultPage = movieService.findByGenreAndReleaseYear(genre, year, pageable);
        } else if (genre != null) {
            resultPage = movieService.findByGenre(genre, pageable);
        } else if (year != null) {
            resultPage = movieService.findByReleaseYear(year, pageable);
        } else {
            resultPage = movieService.getAllMovies(pageable);
        }
        return ResponseEntity.ok(resultPage.getContent());
    }

    @GetMapping("/search")
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
        return resultPage.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultPage);
    }

    @PostMapping("")
    public ResponseEntity<List<Movie>> getMovies(@RequestBody User user, Pageable pageable) {
        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            Page<Movie> movies = movieService.getAllMoviesRandom(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
            System.out.println("test1");
            return ResponseEntity.ok(movies.getContent());
        } else {
            System.out.println("test2");
            Page<Movie> movies = movieService.getAllMoviesWithFavourites(user.getEmail(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
            return ResponseEntity.ok(movies.getContent());
        }
    }

}
