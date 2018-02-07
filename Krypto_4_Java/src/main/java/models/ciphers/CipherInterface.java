/**
 * CipherInterface.java
 * Defines all the common methods a Cipher should have
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package models.ciphers;
/**
 * Defines all the common methods a Cipher should have.
 */
interface CipherInterface {

    /**
     * Returns the name of the cipher to identify the cipher after polymorphism.
     * @return name of the cipher (e.g. "Shift", "Transposition")
     */
    String getName();

    /**
     * Returns a description that informs users how the cipher roughly works.
     * @return description of the cipher
     * (e.g. "adds the value of each character in the plaintext and key (mod 26)
     * to produce a ciphertext")
     */
    String getDescription();

    /**
     * Checks if the key is in a valid format (e.g. numerical, alphabetical)
     * @param key String that will determine how input text is transformed
     * @return true if the key is valid for this cipher
     */
    Boolean checkKey(String key);

    /**
     * Transforms a copy of the plaintext using the key to produce ciphertext.
     * @param plaintext the String input that will be transformed
     * @param key determines how the key will be transformed
     * @return ciphertext produce by transforming the plaintext using the key
     */
    String encrypt(String plaintext, String key);

    /**
     * Transforms a copy of the ciphertext using the key to produce plaintext.
     * @param ciphertext the String input that will be transformed
     * @param key determines how the key will be transformed
     * @return plaintext produce by transforming the plaintext using the key
     */
    String decrypt(String ciphertext, String key);
}

