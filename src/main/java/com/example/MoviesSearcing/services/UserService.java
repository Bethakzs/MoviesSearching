package com.example.MoviesSearcing.services;

import com.example.MoviesSearcing.models.User;
import com.example.MoviesSearcing.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Boolean loginUser(User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb != null) {
            return passwordEncoder.matches(user.getPassword(), userFromDb.getPassword());
        } else {
            return false;
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
