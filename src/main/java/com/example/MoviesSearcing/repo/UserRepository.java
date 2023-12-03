package com.example.MoviesSearcing.repo;

import com.example.MoviesSearcing.models.Movie;
import com.example.MoviesSearcing.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    User findByEmail(String email);
}