/*
 * ---------
 * Interface Name: Cipher.java
 * Serves as the interface for all cipher modules.
 * ---------
*/

package com;

public interface Cipher {

    //basic encryption/decryption
    String encrypt(String plaintext, String key) throws InvalidKeyException;

    String decrypt(String ciphertext, String key) throws InvalidKeyException;

    //returns a name or description of the current cipher.
    //useful if we want to construct a list or something.
    String getDescription();
    String getName();

    //check if the key is a valid.
    Boolean checkKey(String key) throws InvalidKeyException;

    /* Ciphers
     * ---------------
     * Shift
     * Transposition (columnar, rectangle, playfair)
     * Vigenere (+, -, inverse)
     * Substitution
     * OTP
     * --------------
     */
}
