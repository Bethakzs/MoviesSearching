package com.example.MoviesSearcing.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String releaseYear;
    private String description;
    private String genre;
    private double rating;
    private String poster;
    private boolean Reccomended;

    public Movie(Long id, String title, String releaseYear, String description, String genre, double rating, String poster, boolean recommended) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.poster = poster;
        this.Reccomended = recommended;
    }

    public Movie() {
    }

    public void setRecommended(boolean reccomended) {
        this.Reccomended = reccomended;
    }
}
