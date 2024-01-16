package com.example.MoviesSearcing.services;

import com.example.MoviesSearcing.models.Movie;
import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.repo.MovieRepository;
import com.example.MoviesSearcing.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserService userService;

    @Autowired
    public MovieService(MovieRepository movieRepository, UserService userService) {
        this.movieRepository = movieRepository;
        this.userService = userService;
    }

    public Page<Movie> findByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Movie> findByTitleIgnoreCaseContaining(String title, Pageable pageable) {
        return movieRepository.findByTitleIgnoreCaseContaining(title, pageable);
    }

    public Page<Movie> findByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenreContainingIgnoreCase(genre, pageable);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Page<Movie> findByGenreAndReleaseYear(String genre, String releaseYear, Pageable pageable) {
        return movieRepository.findByGenreContainingIgnoreCaseAndReleaseYearContaining(genre, releaseYear, pageable);
    }

    public Page<Movie> findByReleaseYear(String releaseYear, Pageable pageable) {
        return movieRepository.findByReleaseYearContaining(releaseYear, pageable);
    }

    public Page<Movie> getAllMoviesWithFavourites(String email, Pageable pageable) {
        User existingUser = userService.findByEmail(email);
        String favouriteMoviesString = existingUser.getFavouriteMovies();
        List<String> favouriteMovies = List.of(favouriteMoviesString.split(", "));
        List<String> genres = new ArrayList<>();
        for (String favouriteMovie : favouriteMovies) {
            List<Movie> movies = this.findByTitleIgnoreCaseContaining(favouriteMovie, pageable).getContent();
            if (!movies.isEmpty()) {
                String genre = movies.get(0).getGenre();
                genres.add(genre);
            }
        }
        Page<Movie> similarMovies = this.getAllMoviesByGenres(genres, PageRequest.of(0, 5));
        similarMovies.forEach(movie -> movie.setRecommended(true));
        Page<Movie> allMovies = this.getAllMovies(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Movie> allMoviesList = new ArrayList<>(allMovies.getContent());
        Collections.shuffle(allMoviesList);
        List<Movie> resultMovies = new ArrayList<>(similarMovies.getContent());
        resultMovies.addAll(allMoviesList);
        return new PageImpl<>(resultMovies, pageable, resultMovies.size());
    }

    private Page<Movie> getAllMoviesByGenres(List<String> genres, PageRequest of) {
        return movieRepository.findAllByGenreInOrderByRatingDesc(genres, of);
    }

    public Page<Movie> getAllMoviesRandom(Pageable pageable) {
        Page<Movie> allMovies = this.getAllMovies(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<Movie> allMoviesList = new ArrayList<>(allMovies.getContent());
        Collections.shuffle(allMoviesList);
        return new PageImpl<>(allMoviesList, pageable, allMoviesList.size());
    }
}