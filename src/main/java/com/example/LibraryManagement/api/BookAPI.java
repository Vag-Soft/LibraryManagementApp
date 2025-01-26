package com.example.LibraryManagement.api;

import com.example.LibraryManagement.database.BookRepository;
import com.example.LibraryManagement.database.UserRepository;
import com.example.LibraryManagement.models.Book;
import com.example.LibraryManagement.models.User;
import com.example.LibraryManagement.security.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Base64;

@RestController
@RequestMapping("/api/books")
public class BookAPI {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookAPI() {
        this.bookRepository = BookRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    @GetMapping("")
    public ArrayList<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @GetMapping("title/{title}")
    public ArrayList<Book> getBooksByTitle(@PathVariable String title) {
        return bookRepository.findBooksByTitle(title);
    }

    @GetMapping("author/{author}")
    public ArrayList<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findBooksByAuthor(author);
    }

    @GetMapping("id/{id}")
    public Book getBooksByAuthor(@PathVariable int id) {
        return bookRepository.findBookById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Book book, HttpServletRequest request) {
        // Decoding the username and password
        String[] decodedCredentials = Utils.decodeAuthHeader(request.getHeader("Authorization"));
        String username = decodedCredentials[0];
        String hashPassword = decodedCredentials[1];

        // Authenticating the user
        User user = userRepository.authenticateUser(username, hashPassword).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong user credentials");
        }
        else if(!user.getAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not an admin");
        }
        else {
            if(bookRepository.addBook(book)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Book added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Book already exists");
            }
        }

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id, HttpServletRequest request) {
        // Decoding the username and password
        String[] decodedCredentials = Utils.decodeAuthHeader(request.getHeader("Authorization"));
        String username = decodedCredentials[0];
        String hashPassword = decodedCredentials[1];

        // Authenticating the user
        User user = userRepository.authenticateUser(username, hashPassword).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong user credentials");
        }
        else if(!user.getAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not an admin");
        }
        else {
            if(bookRepository.deleteBookByID(id)) {
                return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
            }
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateBookById(@PathVariable int id, @RequestBody Book newBook, HttpServletRequest request) {
        // Decoding the username and password
        String[] decodedCredentials = Utils.decodeAuthHeader(request.getHeader("Authorization"));
        String username = decodedCredentials[0];
        String hashPassword = decodedCredentials[1];

        // Authenticating the user
        User user = userRepository.authenticateUser(username, hashPassword).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong user credentials");
        }
        else if(!user.getAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not an admin");
        }
        else {
            if(bookRepository.updateBookById(id, newBook)) {
                return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
            }
            else if(bookRepository.findBookById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Book not updated");
            }
        }
    }
}
