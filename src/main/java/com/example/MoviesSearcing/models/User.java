package com.example.MoviesSearcing.models;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Entity 
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="USERID_SEQ")
    @SequenceGenerator(name="USERID_SEQ", sequenceName="USERID_SEQ", allocationSize=1)
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
}