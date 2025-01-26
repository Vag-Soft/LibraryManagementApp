package com.example.LibraryManagement.models;

/**
 * The RegisterInfo class represents the information required to register a new user
 * It contains the username, password, and admin status
 */
public class RegisterInfo {
    /**
     * The username of the user to be registered
     */
    private String username;
    /**
     * The password of the user to be registered
     */
    private String password;
    /**
     * Whether the user is an admin or not
     */
    private boolean admin;

    /**
     * Default constructor for the RegisterInfo class
     */
    public RegisterInfo() {}

    /**
     * Constructor for the RegisterInfo class
     *
     * @param username the username of the user to be registered
     * @param password the password of the user to be registered
     * @param admin whether the user is an admin or not
     */
    public RegisterInfo(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    /**
     * Getter for the username field.
     *
     * @return the username of the user to be registered.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username field.
     *
     * @param username the username of the user to be registered.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password field.
     *
     * @return the password of the user to be registered.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password field.
     *
     * @param password the password of the user to be registered.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the admin field.
     *
     * @return whether the user is an admin or not.
     */
    public boolean getAdmin() {
        return admin;
    }

    /**
     * Setter for the admin field.
     *
     * @param admin whether the user is an admin or not.
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
