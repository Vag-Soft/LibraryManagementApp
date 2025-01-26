package com.example.LibraryManagement.models;

/**
 * The Rental class represents a rental between a user and a book.
 * It contains the unique id of the user renting the book and the unique id of the book being rented.
 */
public class Rental {
    /**
     * The unique id of the user renting the book
     */
    private int bookId;
    /**
     * The unique id of the book being rented
     */
    private int userId;

    /**
     * Default constructor for the Rental class
     */
    public Rental() {}

    /**
     * Constructor for the Rental class
     *
     * @param bookId the unique id of the book being rented
     * @param userId the unique id of the user renting the book
     */
    public Rental(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    /**
     * Getter for the unique id of the book.
     *
     * @return the unique id of the book
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Setter for the unique id of the book.
     *
     * @param userId the unique id of the book
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Getter for the unique id of the book being rented.
     *
     * @return the unique id of the book being rented
     */
    public int getBookId() {
        return bookId;
    }

    /**
     * Setter for the unique id of the book being rented.
     *
     * @param bookId the unique id of the book being rented
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * Returns a string representation of the rental.
     *
     * @return a string representation of the rental
     */
    @Override
    public String toString() {
        return "Rental{" +
                "userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }
}
