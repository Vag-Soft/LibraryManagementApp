package com.example.LibraryManagement.database;

import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:library_db.sqlite";

    public DatabaseInitializer() {
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

    private void createBooksTable(Connection connection){
        String query = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                capacity INTEGER NOT NULL
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Checked/Created 'books' table.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createUsersTable(Connection connection){
        String query = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                admin BOOLEAN NOT NULL
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Checked/Created 'users' table.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createRentalsTable(Connection connection){
        String query = """
            CREATE TABLE IF NOT EXISTS rentals (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                book_id INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (book_id) REFERENCES books(id)
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            System.out.println("Checked/Created 'rentals' table.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}