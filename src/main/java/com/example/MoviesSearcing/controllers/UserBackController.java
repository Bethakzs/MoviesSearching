package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.services.UserService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
public class UserBackController {
    private final UserService userService;

    public UserBackController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if (user.getUsername() == null || user.getEmail() == null  || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        User newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestParam String email, @RequestParam String password, @RequestParam(required = false) String callback) {
        if (email == null || password == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User existingUser = userService.findByEmail(email);
        if (existingUser != null && existingUser.getPassword().equals(password)) {
            String json = new Gson().toJson(existingUser);
            return ResponseEntity.ok().contentType(new MediaType("application", "javascript")).body(callback + "(" + json + ");");
        }
        String json = new Gson().toJson("Invalid email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(new MediaType("application", "javascript")).body(callback + "(" + json + ");");
    }

    @GetMapping("/{email}/favourite-movies")
    public String getFavoriteMovies(@PathVariable String email, @RequestParam(name = "callback", required = true) String callback) {
        User user = userService.findByEmail(email);
        String favouriteMoviesString = user.getFavouriteMovies();
        List<String> favouriteMovies;
        if (favouriteMoviesString != null) {
            favouriteMovies = Arrays.asList(favouriteMoviesString.split(","));
        } else {
            favouriteMovies = new ArrayList<>();
        }
        String json = new Gson().toJson(favouriteMovies);
        return callback + "(" + json + ");";
    }

    @PostMapping("/{email}/add-favourite-movie")
    public ResponseEntity<User> addFavoriteMovie(@RequestBody String movie, @PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null || movie == null || movie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String favoriteMovies = user.getFavouriteMovies();
        if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
            List<String> movieList = Arrays.asList(favoriteMovies.split(", "));
            if (movieList.contains(movie)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            user.setFavouriteMovies(favoriteMovies + ", " + movie);
        } else {
            user.setFavouriteMovies(movie);
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }

    @PutMapping("/{email}/remove-favourite-movie/{movie}")
    public ResponseEntity<User> removeFavoriteMovie(@PathVariable String movie, @PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null || movie == null || movie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String favoriteMovies = user.getFavouriteMovies();
        if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
            List<String> movieList = new ArrayList<>(Arrays.asList(favoriteMovies.split(", ")));
            if (movieList.contains(movie)) {
                movieList.remove(movie);
                user.setFavouriteMovies(String.join(", ", movieList));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(savedUser);
    }
}