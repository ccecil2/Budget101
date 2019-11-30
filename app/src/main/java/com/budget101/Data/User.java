package com.budget101.Data;

import android.graphics.Bitmap;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Holds data for a user object.
 */
public final class User {
    private final String name;
    private String password;
    private Bitmap picture;


    /**
     * Creates a user object with given data.
     *
     * @param name     - Name of user
     * @param password - Hashed password for user
     * @param image    - Image of user
     */
    public User(String name, String password, Bitmap image)
    {
        this.name = name;
        this.password = password;
        this.picture = image;
    }


    /**
     * Returns the name of the user.
     *
     * @return Name of user
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Returns the password for the user.
     * Should be hashed in SHA256.
     *
     * @return Hashed password
     */
    public String getPassword()
    {
        return this.password;
    }


    /**
     * Sets the password as a SHA256 hash.
     * Saves "" on failure of hashing process.
     * @param pass - New password as plaintext
     * @return Success of saving hashed password.
     */
    public boolean setPassword(String pass)
    {
        boolean success = false;

        try
        {
            String hashed = hash(pass);
            this.password = hashed;
            success = true;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            this.password = "";
        }

        return success;
    }


    /**
     * Performs SHA-256 hash to given input.
     * @param pass - Password to hash
     * @return Hashed version of input
     * @throws NoSuchAlgorithmException
     */
    public static String hash(String pass) throws NoSuchAlgorithmException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(pass.getBytes(StandardCharsets.UTF_8)); // Encode data

        BigInteger num = new BigInteger(1, bytes);
        StringBuilder hex = new StringBuilder(num.toString(16)); // Base 16

        while(hex.length() < 0) // Pad extra space
            hex.insert(0, '0');

        return hex.toString(); // Convert back to string
    }


    /**
     * Bitmap image of the user.
     * @return Bitmap
     */
    public Bitmap getPicture()
    {
        return this.picture;
    }
}
