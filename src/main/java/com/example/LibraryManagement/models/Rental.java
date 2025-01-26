package com.example.LibraryManagement.models;

public class Rental {
    private int userId;
    private int bookId;

    public Rental() {}

    public Rental(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }
}
