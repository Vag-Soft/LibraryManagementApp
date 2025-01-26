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

import java.util.ArrayList;

/**
 * The RentalAPI class handles rental-related API endpoints.
 * It provides methods for getting all rentals, renting a book, and returning a book.
 */
@RestController
@RequestMapping("/api/rentals")
public class RentalAPI {
    /**
     * The RentalRepository instance used to interact with the database.
     */
    private final RentalRepository rentalRepository;
    /**
     * The UserRepository instance used to interact with the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for the RentalAPI class.
     * Initializes the RentalRepository and UserRepository instances.
     */
    public RentalAPI() {
        this.rentalRepository = RentalRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * API endpoint to retrieve a list of all rentals from the database.
     *
     * @return an ArrayList of Rental objects representing all rentals in the database
     */
    @GetMapping("")
    public ArrayList<Rental> getAllRentals() {
        return rentalRepository.getAllRentals();
    }

    /**
     * API endpoint to rent a book to a user in the database.
     * Requires Basic HTTP Authentication.
     *
     * @param id the id of the book to be rented
     * @param request the HTTP request containing the username and password
     * @return a ResponseEntity with an appropriate status code and message
     */
    @PostMapping("/rent/{id}")
    public ResponseEntity<String> rentBook(@PathVariable int id, HttpServletRequest request) {
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
            Rental rental = new Rental(id, user.getId());
            if(rentalRepository.rentBook(rental)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Book rented successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Book already rented");
            }

        }
    }

    /**
     * API endpoint to return a book from the database.
     * Requires Basic HTTP Authentication.
     *
     * @param id the id of the book to be returned
     * @param request the HTTP request containing the username and password
     * @return a ResponseEntity with an appropriate status code and message
     */
    @PostMapping("/return/{id}")
    public ResponseEntity<String> returnBook(@PathVariable int id, HttpServletRequest request) {
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
            Rental rental = new Rental(id, user.getId());
            if(rentalRepository.returnBook(rental)) {
                return ResponseEntity.status(HttpStatus.OK).body("Book returned successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rental not found");
            }
        }
    }
}
