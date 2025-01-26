package com.example.LibraryManagement.api;

import com.example.LibraryManagement.database.BookRepository;
import com.example.LibraryManagement.database.UserRepository;
import com.example.LibraryManagement.models.Book;
import com.example.LibraryManagement.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserAPI {
    private final UserRepository userRepository;

    public UserAPI() {
        this.userRepository = UserRepository.getInstance();
    }

    @GetMapping("")
    public ArrayList<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if(userRepository.registerUser(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
    }
}
