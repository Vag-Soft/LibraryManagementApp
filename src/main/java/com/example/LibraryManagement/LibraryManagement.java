package com.example.LibraryManagement;

import com.example.LibraryManagement.database.BookRepository;
import com.example.LibraryManagement.database.RentalRepository;
import com.example.LibraryManagement.database.UserRepository;
import com.example.LibraryManagement.models.Book;
import com.example.LibraryManagement.models.Rental;
import com.example.LibraryManagement.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryManagement {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagement.class, args);


		UserRepository userRepository = UserRepository.getInstance();
		BookRepository bookRepository = BookRepository.getInstance();
		RentalRepository rentalRepository = RentalRepository.getInstance();

		userRepository.registerUser(new User("admin", "admin", true));
		userRepository.registerUser(new User("user", "user", false));

		bookRepository.addBook(new Book("The Lord of the Rings", "J.R.R. Tolkien"));
		bookRepository.addBook(new Book("The Hobbit", "J.R.R. Tolkien"));


		System.out.println(rentalRepository.rentBook(new Rental(1, 1)));




		System.out.println(userRepository.getAllUsers());

		System.out.println(bookRepository.getAllBooks());

		System.out.println(rentalRepository.getAllRentals());

	}

}
