package com.example.LibraryManagement.database;

import com.example.LibraryManagement.models.Rental;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;

/**
 * RentalRepository class is responsible for managing rental data in the SQLite database.
 * It provides methods for adding, returning, and retrieving rentals.
 */
@Component
public class RentalRepository {
    /**
     * The URL of the SQLite database to connect to.
     */
    private static String DB_URL;
    /**
     * The singleton instance of UserRepository.
     */
    private final UserRepository user_repository_instance;
    /**
     * The singleton instance of BookRepository.
     */
    private final BookRepository book_repository_instance;
    /**
     * The singleton instance of RentalRepository.
     */
    private static RentalRepository rental_repository_instance = null;


    /**
     * Constructor for RentalRepository class.
     *
     * @param dbUrl the URL of the SQLite database to connect to. This value is obtained from the application.properties file.
     */
    private RentalRepository(@Value("${db.url}") String dbUrl) {
        // Initializing DB_URL from application.properties
        DB_URL = dbUrl;

        user_repository_instance = UserRepository.getInstance();
        book_repository_instance = BookRepository.getInstance();
    }

    /**
     * Returns the singleton instance of RentalRepository.
     * Initializes the instance if it hasn't been created yet.
     *
     * @return the singleton instance of RentalRepository
     */
    public static RentalRepository getInstance() {
        if (rental_repository_instance == null)
            rental_repository_instance = new RentalRepository(DB_URL);

        return rental_repository_instance;
    }

    /**
     * Adds a rental to the database.
     * The rental is added if the user and book both exist and the book is available.
     * The book's availability is set to false after a successful rental.
     *
     * @param rental the rental to be added
     * @return true if the rental is successful, false otherwise
     */
    public boolean rentBook(Rental rental) {
        // Checking if the user exists and the book is available
        if(user_repository_instance.findUserById(rental.getUserId()).isEmpty() ||
        !book_repository_instance.isAvailable(rental.getBookId())) {
            return false;
        }

        String query = """
                INSERT INTO rentals (user_id, book_id)
                VALUES (?,?)
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, rental.getUserId());
            prepStatement.setInt(2, rental.getBookId());

            // If the rental is successful, the book's availability is set to false
            if(prepStatement.executeUpdate() > 0) {
                return book_repository_instance.updateBookAvailability(rental.getBookId(), false);
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a rented book in the database.
     * The rental is removed if it exists and the book's availability is set to true.
     *
     * @param rental the rental to be returned
     * @return true if the return is successful, false otherwise
     */
    public boolean returnBook(Rental rental) {
        // Checking if the rental exists
        if(!existsRental(rental)) {
            return false;
        }

        String query = """
                DELETE FROM rentals
                WHERE user_id=? AND book_id=?
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, rental.getUserId());
            prepStatement.setInt(2, rental.getBookId());

            // If the return is successful, the book's availability is set to true
            if(prepStatement.executeUpdate() > 0) {
                return book_repository_instance.updateBookAvailability(rental.getBookId(), true);
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a rental exists in the database.
     * The rental is checked by user ID and book ID.
     *
     * @param rental the rental to be checked
     * @return true if the rental exists, false otherwise
     */
    public boolean existsRental(Rental rental) {
        String query = """
                SELECT *
                FROM rentals
                WHERE user_id=? AND book_id=?
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, rental.getUserId());
            prepStatement.setInt(2, rental.getBookId());

            return prepStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all rentals from the database.
     * Each rental is represented by a combination of user ID and book ID.
     *
     * @return an ArrayList of Rental objects containing all rentals in the database
     */
    public ArrayList<Rental> getAllRentals() {
        String query = """
                    SELECT *
                    FROM rentals
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = prepStatement.executeQuery();

            ArrayList<Rental> allRentals = new ArrayList<>();
            while (resultSet.next()) {
                Rental rental = new Rental(
                        resultSet.getInt("book_id"),
                        resultSet.getInt("user_id")
                );

                allRentals.add(rental);
            }
            return allRentals;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
