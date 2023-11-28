package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.models.Movie;
import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping("/search-all-users")
    @ResponseBody
    public List<User> getAllUsersJSON(Pageable pageable) {
        Page<User> userPage = userService.getAllUsers(pageable);
        return userPage.getContent();
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(String email, String password, Pageable pageable) {
        if (email != null && password != null) {
            User user = userService.findByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                Page<User> userPage = userService.getAllUsers(pageable);
                return ResponseEntity.ok(userPage.getContent());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @GetMapping("/mainLoged")
    public String loged() {
        return "mainLoged.html";
    }

    @GetMapping("/favourite-movies-json")
    @ResponseBody
    public List<String> FavoriteMoviesJson(@RequestParam String email) {
        User user = userService.findByEmail(email);
        String favouriteMoviesString = user.getFavouriteMovies();
        if (favouriteMoviesString != null) {
            return Arrays.asList(favouriteMoviesString.split(","));
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/favourite-movies")
    public String FavouriteMovies() {
        return "favourite-movies.html";
    }

    @PostMapping("/add-favorite-movie")
    public ResponseEntity<User> addFavoriteMovie(@RequestParam String movie, @RequestParam String email) {
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

    @PutMapping("/remove-favorite-movie")
    public ResponseEntity<User> removeFavoriteMovie(@RequestParam String movie, @RequestParam String email) {
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
