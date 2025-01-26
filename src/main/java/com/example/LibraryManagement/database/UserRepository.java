package com.example.LibraryManagement.database;

import com.example.LibraryManagement.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * UserRepository class is responsible for managing user data in the SQLite database.
 * It provides methods for registering, finding, and authenticating users.
 */
@Component
public class UserRepository {
    /**
     * The URL of the SQLite database to connect to.
     */
    private static String DB_URL;
    /**
     * The singleton instance of UserRepository.
     */
    private static UserRepository user_repository_instance = null;

    /**
     * Constructor for UserRepository class.
     *
     * @param dbUrl the URL of the SQLite database to connect to. This value is obtained from the application.properties file.
     */
    private UserRepository(@Value("${db.url}") String dbUrl) {
        // Initializing DB_URL from application.properties
        DB_URL = dbUrl;
    }

    /**
     * Returns the singleton instance of UserRepository.
     * Initializes the instance if it hasn't been created yet.
     *
     * @return the singleton instance of UserRepository
     */
    public static UserRepository getInstance()
    {
        if (user_repository_instance == null)
            user_repository_instance = new UserRepository(DB_URL);

        return user_repository_instance;
    }

    /**
     * Registers a user into the database.
     * The user is only registered if no user with the same username already exists.
     *
     * @param user the user to be registered
     * @return true if the user was registered successfully, false otherwise
     */
    public boolean registerUser(User user) {
        // Checking if there is already a registered user with the same username
        Optional<User> registeredUser = findUserByUsername(user.getUsername());
        if(registeredUser.isPresent()) {
            return false;
        }

        String query = """
                    INSERT INTO users (username, password_hash, admin)
                    VALUES (?,?,?)
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1, user.getUsername());
            prepStatement.setString(2, user.getPasswordHash());
            prepStatement.setBoolean(3, user.getAdmin());

            return prepStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Finds a user by username.
     *
     * @param username the username of the user to be found
     * @return an Optional containing the found user, or an empty Optional if no such user exists
     */
    private Optional<User> findUserByUsername(String username) {
        String query = """
                    SELECT *
                    FROM users
                    WHERE username=?
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1, username);

            ResultSet resultSet = prepStatement.executeQuery();

            User user = null;
            while (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getBoolean("admin")
                );
            }
            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a user by their unique id.
     *
     * @param id the unique id of the user to be found
     * @return an Optional containing the found user, or an empty Optional if no such user exists
     */
    public Optional<User> findUserById(int id) {
        String query = """
                    SELECT *
                    FROM users
                    WHERE id=?
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, id);

            ResultSet resultSet = prepStatement.executeQuery();

            User user = null;
            while (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getBoolean("admin")
                );
            }
            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Authenticates a user.
     *
     * @param username the username of the user to be authenticated
     * @param hashedPassword the hashed password of the user to be authenticated
     * @return an Optional containing the authenticated user, or an empty Optional if no matching user was found
     */
    public Optional<User> authenticateUser(String username, String hashedPassword) {
        String query = """
                    SELECT *
                    FROM users
                    WHERE username=? AND password_hash=?
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1, username);
            prepStatement.setString(2, hashedPassword);

            ResultSet resultSet = prepStatement.executeQuery();

            User user = null;
            while (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getBoolean("admin")
                );
            }
            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Finds all users in the database.
     *
     * @return an ArrayList containing all users in the database
     */
    public ArrayList<User> getAllUsers() {
        String query = """
                    SELECT *
                    FROM users
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = prepStatement.executeQuery();

            ArrayList<User> allUsers = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getBoolean("admin")
                );

                allUsers.add(user);
            }
            return allUsers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
