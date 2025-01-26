package com.example.LibraryManagement.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utils {
    public static String hashString(String input) {
        try {
            MessageDigest hashingAlgo = MessageDigest.getInstance("SHA-256");
            byte[] hash = hashingAlgo.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] decodeAuthHeader(String authHeader) {
        // Extracting the encoded username and password from the Authorization header
        String[] credentials = authHeader.split(" ");
        // Decoding the username and password
        String[] decodedCredentials = new String(Base64.getDecoder().decode(credentials[1])).split(":");
        String username = decodedCredentials[0];
        // Hashing the password as soon as possible
        String hashedPassword = hashString(decodedCredentials[1]);
        return new String[] { username, hashedPassword };
    }
}
