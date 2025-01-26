package com.example.LibraryManagement.database;

import com.example.LibraryManagement.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class UserRepository {
    private static String DB_URL;
    private static UserRepository user_repository_instance = null;

    private UserRepository(@Value("${db.url}") String dbUrl) {
        // Initializing DB_URL from application.properties
        DB_URL = dbUrl;
    }

    public static UserRepository getInstance()
    {
        if (user_repository_instance == null)
            user_repository_instance = new UserRepository(DB_URL);

        return user_repository_instance;
    }

    public boolean registerUser(User user) {
        // Checks if there is already a registered user with the same username
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


    public Optional<User> findUserByUsername(String username) {
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
