package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.services.MovieService;
import com.example.MoviesSearcing.models.Movie;
import com.google.gson.Gson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/movies")
public class MovieBackController {
    private final MovieService movieService;

    public MovieBackController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/filter")
    public String filterMovies(
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "year", required = false) String year,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "callback", required = true) String callback,
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
        String json = new Gson().toJson(resultPage.getContent());
        return callback + "(" + json + ");";
    }

    @GetMapping("/search")
    public String searchMovies(
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "callback", required = true) String callback,
            Pageable pageable) {
        Page<Movie> resultPage;

        if (title.startsWith("partial:")) {
            String partialTitle = title.substring("partial:".length());
            resultPage = movieService.findByTitleIgnoreCaseContaining(partialTitle, pageable);
        } else {
            resultPage = movieService.findByTitle(title, pageable);
        }
        String json = new Gson().toJson(resultPage.getContent());
        return callback + "(" + json + ");";
    }
}