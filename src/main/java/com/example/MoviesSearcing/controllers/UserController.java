package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        User newUser = userService.saveUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.ok(existingUser);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers(Pageable pageable) {
        Page<User> userPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(userPage.getContent());
    }

    @PostMapping("")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, Pageable pageable) {
        if (user.getEmail() != null && user.getPassword() != null) {
            User existingUser = userService.findByEmail(user.getEmail());
            if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
                Page<User> userPage = userService.getAllUsers(pageable);
                return ResponseEntity.ok(userPage.getContent());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @GetMapping("/{email}/favourite-movies")
    public ResponseEntity<List<String>> getFavoriteMovies(@PathVariable String email) {
        User user = userService.findByEmail(email);
        String favouriteMoviesString = user.getFavouriteMovies();
        if (favouriteMoviesString != null) {
            return ResponseEntity.ok(Arrays.asList(favouriteMoviesString.split(",")));
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/{email}/favourite-movies")
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

    @PutMapping("/{email}/favourite-movies")
    public ResponseEntity<User> removeFavoriteMovie(@RequestBody String movie, @PathVariable String email) {
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
