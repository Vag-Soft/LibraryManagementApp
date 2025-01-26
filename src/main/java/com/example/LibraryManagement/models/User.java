package com.example.LibraryManagement.models;

/**
 * The User class represents a user in the library system.
 * It contains an id, username, password hash, and admin status.
 */
public class User {
    /**
     * The unique id of the user
     */
    private int id;
    /**
     * The username of the user
     */
    private String username;
    /**
     * The password hash of the user
     */
    private String passwordHash;
    /**
     * The admin status of the user
     */
    private Boolean admin;

    /**
     * Default constructor for the User class
     */
    public User() {}

    /**
     * Constructor for the User class
     *
     * @param username the username of the user
     * @param passwordHash the password hash of the user
     * @param admin the admin status of the user
     */
    public User(String username, String passwordHash, Boolean admin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.admin = admin;
    }

    /**
     * Constructor for the User class
     *
     * @param id the id of the user
     * @param username the username of the user
     * @param passwordHash the password hash of the user
     * @param admin the admin status of the user
     */
    public User(int id, String username, String passwordHash, Boolean admin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.admin = admin;
    }

    /**
     * Getter for the id of the user
     *
     * @return the id of the user
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the id of the user.
     *
     * @param id the new id of the user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the username of the user
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username of the user.
     *
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password hash of the user
     *
     * @return the password hash of the user
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Setter for the password hash of the user.
     *
     * @param passwordHash the new password hash of the user
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Getter for the admin status of the user
     *
     * @return the admin status of the user
     */
    public Boolean getAdmin() {
        return admin;
    }

    /**
     * Setter for the admin status of the user.
     *
     * @param admin the new admin status of the user
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of the user
     */
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
