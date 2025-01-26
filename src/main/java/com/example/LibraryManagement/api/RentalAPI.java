package com.example.LibraryManagement.api;

import com.example.LibraryManagement.database.RentalRepository;
import com.example.LibraryManagement.database.UserRepository;
import com.example.LibraryManagement.models.Rental;
import com.example.LibraryManagement.models.User;
import com.example.LibraryManagement.security.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Base64;

@RestController
@RequestMapping("/api/rentals")
public class RentalAPI {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalAPI() {
        this.rentalRepository = RentalRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    @GetMapping("")
    public ArrayList<Rental> getAllRentals() {
        return rentalRepository.getAllRentals();
    }

    @PostMapping("/rent/{bookId}")
    public ResponseEntity<String> rentBook(@PathVariable int bookId, HttpServletRequest request) {
        // Decoding the username and password
        String[] decodedCredentials = Utils.decodeAuthHeader(request.getHeader("Authorization"));
        String username = decodedCredentials[0];
        String hashedPassword = decodedCredentials[1];

        // Authenticating the user
        User user = userRepository.authenticateUser(username, hashedPassword).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong user credentials");
        }
        else {
            // Renting the book
            Rental rental = new Rental(bookId, user.getId());
            if(rentalRepository.rentBook(rental)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Book rented successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Book already rented");
            }

        }
    }


    @PostMapping("/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable int bookId, HttpServletRequest request) {
        // Decoding the username and password
        String[] decodedCredentials = Utils.decodeAuthHeader(request.getHeader("Authorization"));
        String username = decodedCredentials[0];
        String hashedPassword = decodedCredentials[1];

        // Authenticating the user
        User user = userRepository.authenticateUser(username, hashedPassword).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong user credentials");
        }
        else {
            // Returning the book
            Rental rental = new Rental(bookId, user.getId());
            if(rentalRepository.returnBook(rental)) {
                return ResponseEntity.status(HttpStatus.OK).body("Book returned successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rental not found");
            }
        }
    }
}
