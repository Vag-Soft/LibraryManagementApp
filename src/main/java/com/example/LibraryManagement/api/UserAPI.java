package com.example.LibraryManagement.api;

import com.example.LibraryManagement.database.UserRepository;
import com.example.LibraryManagement.models.RegisterInfo;
import com.example.LibraryManagement.models.User;
import com.example.LibraryManagement.security.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * The UserAPI class handles user-related API endpoints.
 * It provides methods to retrieve all users register a new user.
 */
@RestController
@RequestMapping("/api/users")
public class UserAPI {
    /**
     * The UserRepository instance used to interact with the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for the UserAPI class.
     * Initializes the UserRepository instance.
     */
    public UserAPI() {
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * API endpoint to retrieve a list of all users from the database.
     *
     * @return an ArrayList of User objects representing all users in the database
     */
    @GetMapping("")
    public ArrayList<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    /**
     * API endpoint to register a new user in the database.
     *
     * @param registerInfo the register information of the user to be registered
     * @return a ResponseEntity with an appropriate status code and message
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterInfo registerInfo) {
        // Hashing the given password
        String hashedPassword = Utils.hashString(registerInfo.getPassword());

        User user = new User(registerInfo.getUsername(), hashedPassword, registerInfo.getAdmin());
        if(userRepository.registerUser(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
    }
}