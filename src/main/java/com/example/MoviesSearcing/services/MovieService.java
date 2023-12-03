package com.example.MoviesSearcing.services;

import com.example.MoviesSearcing.models.Movie;
import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.repo.MovieRepository;
import com.example.MoviesSearcing.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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
}