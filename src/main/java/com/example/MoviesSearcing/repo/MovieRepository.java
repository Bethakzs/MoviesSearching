package com.example.MoviesSearcing.repo;

import com.example.MoviesSearcing.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Movie> findAll(Pageable pageable);

    Page<Movie> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

    Page<Movie> findByGenreContainingIgnoreCase(String genre, Pageable pageable);

    Page<Movie> findByReleaseYearContaining(String releaseYear, Pageable pageable);

    Page<Movie> findByGenreContainingIgnoreCaseAndReleaseYearContaining(String genre, String releaseYear, Pageable pageable);

    Page<Movie> findByGenreContainingIgnoreCaseAndReleaseYear(String genre, String year, Pageable pageable);
}

