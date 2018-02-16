/**
 * CipherInterface.java
 * Defines all the common methods a Cipher should have
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package com.example.FYP.aardvark_project.Ciphers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines all the common methods a Cipher should have.
 */
public abstract class AbstractCipher implements CipherInterface {

    /**
     * String containing all characters that will be considered.
     */
    public static final String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Returns the name of the cipher to identify the cipher after polymorphism.
     * @return name of the cipher (e.g. "Shift", "Transposition")
     */
    public abstract String getName();

    /**
     * Returns a description that informs users how the cipher roughly works.
     * @return description of the cipher
     * (e.g. "adds the value of each character in the plaintext and key (mod 26)
     * to produce a ciphertext")
     */
    public abstract String getDescription();

    /**
     * Checks if the key is in a valid format (e.g. numerical, alphabetical).
     * Without overriding, this function returns false if it finds any
     * non-alphabetic characters.
     * @param key String that will determine how input text is transformed
     * @return true if the key is valid for this cipher
     */
    public Boolean checkKey(String key) {
        if (key.length() <= 0) return false;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!(Character.isAlphabetic(ch))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Transforms a copy of the plaintext using the key to produce ciphertext.
     * @param plaintext the String input that will be transformed
     * @param key determines how the key will be transformed
     * @return ciphertext produce by transforming the plaintext using the key
     */
    public abstract String encrypt(String plaintext, String key);
    /**
     * Transforms a copy of the ciphertext using the key to produce plaintext.
     * @param ciphertext the String input that will be transformed
     * @param key determines how the key will be transformed
     * @return plaintext produce by transforming the plaintext using the key
     */
    public abstract String decrypt(String ciphertext, String key);

    public String removeDuplicates(String in) {
        Set<Character> temp = new HashSet<>();
        List<Character> revTemp = new ArrayList<>();

        StringBuilder out = new StringBuilder();

        in = in.toLowerCase();

        for (Character c : in.toCharArray()) {
            if (!revTemp.contains(c)) {
                revTemp.add(c);
                out.append(c);
            }
        }

        return out.toString();
    }
}

