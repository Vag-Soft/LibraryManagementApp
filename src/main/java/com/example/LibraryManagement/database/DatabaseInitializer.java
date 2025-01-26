package com.example.LibraryManagement.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseInitializer class is responsible for initializing the SQLite database and creating the necessary tables.
 * It connects to the database and creates the tables if they don't already exist.
 * It is automatically initialized when the application starts because of the @Component annotation.
 */
@Component
public class DatabaseInitializer {
    /**
     * The URL of the SQLite database to connect to.
     */
    private static String DB_URL;

    /**
     * Constructor for DatabaseInitializer class.
     * Initializes the database connection and creates the necessary tables.
     *
     * @param dbUrl The URL of the SQLite database to connect to. This value is obtained from the application.properties file.
     */
    public DatabaseInitializer(@Value("${db.url}") String dbUrl) {
        DB_URL = dbUrl;
        // Connecting to the Database or automatically creating it if it doesn't exist
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            System.out.println("Connected to SQLite database");

            // Creating tables if they don't exist
            createBooksTable(connection);
            createUsersTable(connection);
            createRentalsTable(connection);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Creates the 'books' table in the database if it doesn't already exist.
     * The table includes columns for book ID, title, author, and availability.
     * Ensures that each combination of title and author is unique.
     *
     * @param connection The database connection used to create the table.
     */
    private void createBooksTable(Connection connection){
        String query = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                availability BOOLEAN DEFAULT 1,
                UNIQUE (title, author) ON CONFLICT IGNORE
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Checked/Created 'books' table");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the 'users' table in the database if it doesn't already exist.
     * The table includes columns for user ID, username, password hash, and admin status.
     * Ensures that each username is unique.
     *
     * @param connection The database connection used to create the table.
     */
    private void createUsersTable(Connection connection){
        String query = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                admin BOOLEAN NOT NULL
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Checked/Created 'users' table");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the 'rentals' table in the database if it doesn't already exist.
     * The table includes columns for user ID and book ID.
     * Each combination of user ID and book ID is unique and represents a rental.
     * The table has foreign key constraints referencing the 'users' and 'books' tables.
     * If a book entry is deleted, related rentals are also deleted (ON DELETE CASCADE).
     *
     * @param connection The database connection used to create the table.
     */
    private void createRentalsTable(Connection connection){
        String query = """
            CREATE TABLE IF NOT EXISTS rentals (
                user_id INTEGER NOT NULL,
                book_id INTEGER NOT NULL,
                PRIMARY KEY (user_id, book_id),
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Checked/Created 'rentals' table");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}