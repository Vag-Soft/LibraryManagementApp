package com.example.LibraryManagement.database;

import com.example.LibraryManagement.models.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class BookRepository {
    private static String DB_URL;
    private static BookRepository book_repository_instance = null;


    private BookRepository(@Value("${db.url}") String dbUrl) {
        // Initializing DB_URL from application.properties
        DB_URL = dbUrl;
    }

    public static BookRepository getInstance()
    {
        if (book_repository_instance == null)
            book_repository_instance = new BookRepository(DB_URL);

        return book_repository_instance;
    }

    public boolean addBook(Book book) {
        String query = """
                INSERT INTO books (title, author)
                VALUES (?,?)
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1 ,book.getTitle());
            prepStatement.setString(2 ,book.getAuthor());

            return prepStatement.executeUpdate() > 0;

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteBookByID(int id) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            connection.prepareStatement("PRAGMA foreign_keys = ON").execute();

            String query = """
                DELETE FROM books
                WHERE id=?
                """;

            try(PreparedStatement prepStatement = connection.prepareStatement(query)) {
                prepStatement.setInt(1 ,id);

                if(prepStatement.executeUpdate() > 0) {
                    return true;
                }
                return false;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateBookById(int id, Book  newBook) {
        Book oldBook = findBookById(id).orElse(null);
        if(oldBook == null || newBook == null || oldBook.equals(newBook)) {
            return false;
        }

        if(newBook.getTitle() == null)
            newBook.setTitle(oldBook.getTitle());
        if(newBook.getAuthor() == null)
            newBook.setAuthor(oldBook.getAuthor());

        String query = """
                UPDATE books
                SET title=?, author=?
                WHERE id=?
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setString(1 , newBook.getTitle());
            prepStatement.setString(2 , newBook.getAuthor());
            prepStatement.setInt(3 ,id);

            return prepStatement.executeUpdate() > 0;

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateBookAvailability(int id, boolean availability) {
        String query = """
                UPDATE books
                SET availability=?
                WHERE id=?
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setBoolean(1 , availability);
            prepStatement.setInt(2 ,id);

            return prepStatement.executeUpdate() > 0;

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Book> getAllBooks() {
        String query = """
                    SELECT *
                    FROM books
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = prepStatement.executeQuery();

            ArrayList<Book> allBooks = new ArrayList<>();
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getBoolean("availability")
                );

                allBooks.add(book);
            }
            return allBooks;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Book> findBooksByTitle(String title) {
        String query = """
                    SELECT *
                    FROM books
                    WHERE title=?
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1, title);

            ResultSet resultSet = prepStatement.executeQuery();

            ArrayList<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getBoolean("availability")
                );

                books.add(book);
            }
            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Book> findBooksByAuthor(String author) {
        String query = """
                    SELECT *
                    FROM books
                    WHERE author=?
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1, author);

            ResultSet resultSet = prepStatement.executeQuery();

            ArrayList<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getBoolean("availability")
                );

                books.add(book);
            }
            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Book> findBookById(int id) {
        String query = """
                    SELECT *
                    FROM books
                    WHERE id=?
                    """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, id);

            ResultSet resultSet = prepStatement.executeQuery();

            Book book = null;
            while (resultSet.next()) {
                book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getBoolean("availability")
                );
            }
            return Optional.ofNullable(book);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAvailable(int id) {
        Book book = findBookById(id).orElse(null);
        return book != null && book.getAvailability();
    }

}
