package com.example.LibraryManagement.database;

import com.example.LibraryManagement.models.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

/**
 * BookRepository class is responsible for managing book data in the SQLite database.
 * It provides methods for adding, deleting, updating and finding books.
 */
@Repository
public class BookRepository {
    /**
     * The URL of the SQLite database to connect to.
     */
    private static String DB_URL;
    /**
     * The singleton instance of BookRepository.
     */
    private static BookRepository book_repository_instance = null;


    /**
     * Constructor for BookRepository class.
     *
     * @param dbUrl the URL of the SQLite database to connect to. This value is obtained from the application.properties file.
     */
    private BookRepository(@Value("${db.url}") String dbUrl) {
        // Initializing DB_URL from application.properties
        DB_URL = dbUrl;
    }

    /**
     * Returns the singleton instance of BookRepository.
     * Initializes the instance if it hasn't been created yet.
     *
     * @return the singleton instance of BookRepository
     */
    public static BookRepository getInstance()
    {
        if (book_repository_instance == null)
            book_repository_instance = new BookRepository(DB_URL);

        return book_repository_instance;
    }

    /**
     * Adds a book to the database.
     *
     * @param book the book to be added
     * @return true if the addition is successful, false otherwise
     */
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

    /**
     * Deletes a book from the database based on its ID.
     * Ensures that foreign key constraints are enforced during the operation.
     *
     * @param id the unique id of the book to be deleted
     * @return true if the book was successfully deleted, false otherwise
     */
    public boolean deleteBookByID(int id) {
        try(Connection connection = DriverManager.getConnection(DB_URL)) {
            connection.prepareStatement("PRAGMA foreign_keys = ON").execute();

            String query = """
                DELETE FROM books
                WHERE id=?
                """;

            try(PreparedStatement prepStatement = connection.prepareStatement(query)) {
                prepStatement.setInt(1 ,id);

                return prepStatement.executeUpdate() > 0;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a book in the database based on its ID.
     * If the new book object is null, or if it is equal to the old book object,
     * this method returns false. Otherwise, it updates the book with the new
     * values. If the title or author of the new book is null, the old values are
     * used instead.
     *
     * @param id the unique id of the book to be updated
     * @param newBook the new book object with the desired values
     * @return true if the book was successfully updated, false otherwise
     */
    public boolean updateBookById(int id, Book  newBook) {
        Book oldBook = findBookById(id).orElse(null);
        if(oldBook == null || newBook == null || oldBook.equals(newBook)) {
            return false;
        }

        // If the new values are null, the old ones are used
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

    /**
     * Updates the availability of a book in the database.
     * If the book is not found in the database, this method returns false.
     * Otherwise, it updates the availability of the book with the given id
     * and returns true.
     *
     * @param id the unique id of the book to be updated
     * @param availability the new value for the availability of the book
     * @return true if the book was successfully updated, false otherwise
     */
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

    /**
     * Retrieves all books from the database.
     *
     * @return an ArrayList containing all books in the database
     */
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

/**
 * Retrieves a list of books from the database that match the given title.
 *
 * @param title the title of the books to search for
 * @return an ArrayList of Book objects that have the specified title
 */
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

    /**
     * Retrieves a list of books from the database that match the given author.
     *
     * @param author the author of the books to search for
     * @return an ArrayList of Book objects that have the specified author
     */
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

    /**
     * Finds a book by its unique id.
     *
     * @param id the id of the book to be found
     * @return an Optional containing the found book, or an empty Optional if no such book exists
     */
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

    /**
     * Checks if a book is available.
     * A book is available if it exists and its availability is true.
     *
     * @param id the id of the book to be checked
     * @return true if the book is available, false otherwise
     */
    public boolean isAvailable(int id) {
        Book book = findBookById(id).orElse(null);
        return book != null && book.getAvailability();
    }

}
