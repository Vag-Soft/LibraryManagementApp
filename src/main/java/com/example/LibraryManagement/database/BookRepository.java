package com.example.LibraryManagement.database;

import com.example.LibraryManagement.models.Book;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class BookRepository {
    private static final String DB_URL = "jdbc:sqlite:library_db.sqlite";
    private static BookRepository book_repository_instance = null;


    private BookRepository() {

    }

    public static BookRepository getInstance()
    {
        if (book_repository_instance == null)
            book_repository_instance = new BookRepository();

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
        String query = """
                DELETE FROM books
                WHERE id=?
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1 ,id);

            return prepStatement.executeUpdate() > 0;

        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateBookById(int id, Book  newBook) {
        Book oldBook = findBookById(id).orElse(null);
        if(oldBook == null || newBook == null) {
            return false;
        }

        if(newBook.getTitle() == null)
            newBook.setTitle(oldBook.getTitle());
        if(newBook.getAuthor() == null)
            newBook.setAuthor(oldBook.getAuthor());

        String query = """
                UPDATE books
                SET title=?, author=?, availability=?
                WHERE id=?
                """;

        try(Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setString(1 , newBook.getTitle());
            prepStatement.setString(2 , newBook.getAuthor());
            prepStatement.setBoolean(3 , newBook.getAvailability());
            prepStatement.setInt(4 ,id);

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
            return allBooks; //TODO: Check null

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
