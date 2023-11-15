package com.example.MoviesSearcing.controllers;

import com.example.MoviesSearcing.models.Movie;
import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
        if(user.getEmail() == null || user.getPassword() == null || user.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(String email, String password, Pageable pageable) {
        if(email != null && password != null) {
            User user = userService.findByEmail(email);
            if(user != null && user.getPassword().equals(password)) {
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

}
