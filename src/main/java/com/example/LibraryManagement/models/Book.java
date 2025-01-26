package com.example.LibraryManagement.models;

import java.util.Objects;

/**
 * The Book class represents a book in the library.
 * It contains an id, title, author, and availability.
 */
public class Book {
    /**
     * The unique id of the book.
     */
    private int id;

    /**
     * The title of the book.
     */
    private String title;

    /**
     * The author of the book.
     */
    private String author;

    /**
     * Indicates whether the book is available for borrowing.
     */
    private boolean availability;

    /**
     * Default constructor for the Book class
     */
    public Book() {}

    /**
     * Constructor for the Book class
     *
     * @param title the title of the book
     * @param author the author of the book
     */
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    /**
     * Constructor for the Book class
     *
     * @param id the id of the book
     * @param title the title of the book
     * @param author the author of the book
     * @param availability the availability of the book
     */
    public Book(int id, String title, String author, boolean availability) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.availability = availability;
    }

    /**
     * Getter for the id of the book
     *
     * @return the id of the book
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the id of the book.
     *
     * @param id the new id of the book
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title of the book.
     *
     * @param title the new title of the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the author of the book.
     *
     * @return the author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Setter for the author of the book.
     *
     * @param author the new author of the book
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter for the availability of the book.
     *
     * @return true if the book is available; false otherwise
     */
    public boolean getAvailability() {
        return availability;
    }

    /**
     * Setter for the availability of the book.
     *
     * @param availability true if the book is available; false otherwise
     */
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    /**
     * Returns a string representation of the book.
     *
     * @return a string representation of the book
     */
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", availability=" + availability +
                '}';
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     * Two books are considered equal if and only if their titles and authors are equal.
     *
     * @param o the object to compare with
     * @return true if the object is equal to this book; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author, book.author);
    }

    /**
     * Returns a hash code for this book.
     * The hash code is a sum of hash codes of the title and author of the book.
     *
     * @return a hash code for this book
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }
}
