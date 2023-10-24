package com.example.MoviesSearcing.repo;

import com.example.MoviesSearcing.models.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Movie> findByReleaseYear(int releaseYear, Pageable pageable);

    Page<Movie> findAll(Pageable pageable);

    Page<Movie> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
}
