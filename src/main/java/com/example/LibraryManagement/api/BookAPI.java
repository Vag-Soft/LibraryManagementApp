package com.example.LibraryManagement.api;

import com.example.LibraryManagement.database.BookRepository;
import com.example.LibraryManagement.database.UserRepository;
import com.example.LibraryManagement.models.Book;
import com.example.LibraryManagement.models.User;
import com.example.LibraryManagement.security.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

/**
 * The BookAPI class handles book-related API endpoints.
 * It provides methods to retrieve, add, update, and delete books.
 */
@RestController
@RequestMapping("/api/books")
public class BookAPI {
    /**
     * The BookRepository instance used to interact with the database.
     */
    private final BookRepository bookRepository;
    /**
     * The UserRepository instance used to interact with the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for the BookAPI class.
     * Initializes the BookRepository and UserRepository instances.
     */
    public BookAPI() {
        this.bookRepository = BookRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * API endpoint to retrieve a list of all books from the database.
     *
     * @return an ArrayList of Book objects representing all books in the database
     */
    @GetMapping("")
    public ArrayList<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    /**
     * API endpoint to retrieve a list of all books from the database that match the given title.
     *
     * @param title the title of the books to search for
     * @return an ArrayList of Book objects that have the specified title
     */
    @GetMapping("title/{title}")
    public ArrayList<Book> getBooksByTitle(@PathVariable String title) {
        return bookRepository.findBooksByTitle(title);
    }

    /**
     * API endpoint to retrieve a list of all books from the database that match the given author.
     *
     * @param author the author of the books to search for
     * @return an ArrayList of Book objects that have the specified author
     */
    @GetMapping("author/{author}")
    public ArrayList<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findBooksByAuthor(author);
    }

    /**
     * API endpoint to retrieve a specific book from the database based on its id.
     *
     * @param id the id of the book to retrieve
     * @return the Book object with the specified id
     */
    @GetMapping("id/{id}")
    public Book getBooksByAuthor(@PathVariable int id) {
        return bookRepository.findBookById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    /**
     * API endpoint to add a new book to the database.
     * Requires Basic HTTP Authentication.
     *
     * @param book the Book object to be added
     * @return a ResponseEntity with an appropriate status code and message
     */
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
        // Checking if the user is an admin
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

    /**
     * API endpoint to delete a book from the database based on its id.
     * Requires Basic HTTP Authentication.
     *
     * @param id the id of the book to delete
     * @return a ResponseEntity with an appropriate status code and message
     */
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
        // Checking if the user is an admin
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

    /**
     * API endpoint to update a book in the database based on its id.
     * Requires Basic HTTP Authentication.
     *
     * @param id the id of the book to update
     * @param newBook the new Book object with the desired values
     * @return a ResponseEntity with an appropriate status code and message
     */
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
        // Checking if the user is an admin
        else if(!user.getAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not an admin");
        }
        else {
            if(bookRepository.updateBookById(id, newBook)) {
                return ResponseEntity.status(HttpStatus.OK).body("Book updated successfully");
            }
            // Checking if the book exists
            else if(bookRepository.findBookById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Book not updated");
            }
        }
    }
}
