package com.example.MoviesSearcing.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String favouriteMovies;

    public User(Long id, String username, String email, String password, String favouriteMovies) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.favouriteMovies = favouriteMovies;
    }

    public User() {

    }
    public String getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(String favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}