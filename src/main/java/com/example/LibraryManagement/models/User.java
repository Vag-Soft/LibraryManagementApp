package com.example.LibraryManagement.models;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private Boolean admin;

    public User() {}

    public User(String username, String passwordHash, Boolean admin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.admin = admin;
    }

    public User(int id, String username, String passwordHash, Boolean admin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", admin=" + admin +
                '}';
    }
}
