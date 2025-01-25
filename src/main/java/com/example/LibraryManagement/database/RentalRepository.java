package com.example.LibraryManagement.database;

import com.example.LibraryManagement.models.Book;
import com.example.LibraryManagement.models.Rental;
import com.example.LibraryManagement.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class RentalRepository {
    private static final String DB_URL = "jdbc:sqlite:library_db.sqlite";
    private final UserRepository user_repository_instance;
    private final BookRepository book_repository_instance;
    private static RentalRepository rental_repository_instance = null;


    private RentalRepository() {
        user_repository_instance = UserRepository.getInstance();
        book_repository_instance = BookRepository.getInstance();
    }

    public static RentalRepository getInstance() {
        if (rental_repository_instance == null)
            rental_repository_instance = new RentalRepository();

        return rental_repository_instance;
    }

    public boolean rentBook(Rental rental) {
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

            if(prepStatement.executeUpdate() > 0) {
                book_repository_instance.updateBookById(rental.getBookId(), new Book(rental.getBookId(), null, null, false));
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean returnBook(Rental rental) {
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

            if(prepStatement.executeUpdate() > 0) {
                book_repository_instance.updateBookById(rental.getBookId(), new Book(rental.getBookId(), null, null, true));
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
                        resultSet.getInt("user_id"),
                        resultSet.getInt("book_id")
                );

                allRentals.add(rental);
            }
            return allRentals; //TODO: Check null

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
