package com.example.MoviesSearcing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class UserController {
    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping("/mainLoged")
    public String loged() {
        return "mainLoged.html";
    }

    @GetMapping("/favourite-movies")
    public String FavouriteMovies() {
        return "favourite-movies.html";
    }
}
