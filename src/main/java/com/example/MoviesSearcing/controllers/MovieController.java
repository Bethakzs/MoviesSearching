package com.example.MoviesSearcing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class MovieController {

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/all-movies")
    public String getAllMovies() {
        return "all-movies";
    }
}


