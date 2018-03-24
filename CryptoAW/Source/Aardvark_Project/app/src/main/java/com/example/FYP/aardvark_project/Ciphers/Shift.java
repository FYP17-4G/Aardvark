/*
 * ---------
 * Module Name: Shift.java
 * Performs the "Shift Cipher"
 * ---------
 * @params: key -> shift value
 * Returns the ciphertext(String), when the plaintext is shifted by key, OR
 * Returns the plaintext(String), when the ciphertext is shifted by key.
 *
 * If no key is provided, the program will assume a key of '3', which is, essentially, a Caesar Cipher.
 * ---------
*/

package com.example.FYP.aardvark_project.Ciphers;


import com.example.FYP.aardvark_project.Analytics.Cipher;
import com.example.FYP.aardvark_project.Analytics.InvalidKeyException;
import com.example.FYP.aardvark_project.Common.Utility;

public class Shift implements Cipher {
    private final String NAME = "Shift Cipher";
    private static final String DESC = "A common example of the Shift Cipher is the CAESAR CIPHER. It works by shifting" +
            " the characters <x> characters to the right to encrypt it, and <x> characters to the left to decrypt." + "\n" +
            "EXAMPLE" + "\n" +
            "-------" + "\n" +
            "KEY: 3" + "\n" +
            "PT:  THIS IS THE PLAINTEXT" + "\n" +
            "CT:  WKLV LV WKH SODLQWHAW";

    //public Shift(){}

    //caesar cipher by default
    public String encrypt (String plaintext) {
        StringBuilder sb = new StringBuilder();

        Integer offset = 3;
        int output;
        for (Character c: plaintext.toCharArray()) {
            output = c - 'a' + offset;
            output %= 26;

            sb.append(Utility.alphabet.charAt(output));
        }

        return sb.toString();
    }

    public String decrypt (String ciphertext) {
        StringBuilder sb = new StringBuilder();

        Integer offset = 3;
        int output;

        for (Character c: ciphertext.toCharArray()) {
            output = c - 'a' - offset;

            if (output < 0) {
                output += 26;
            }
            output %= 26;

            sb.append(Utility.alphabet.charAt(output));
        }

        return sb.toString();
    }

    @Override
    public String encrypt (String plaintext, String key) throws InvalidKeyException {
        StringBuilder sb = new StringBuilder();

        if (checkKey(key)) {
            Integer offset = Integer.parseInt(key);
            int output;
            for (Character c: plaintext.toCharArray()) {
                output = c - 'a' + offset;
                output %= 26;

                sb.append(Utility.alphabet.charAt(output));
            }
        } else throw new InvalidKeyException(key + " is not a valid key!");

        return sb.toString();
    }

    @Override
    public String decrypt (String ciphertext, String key) throws InvalidKeyException {
        StringBuilder sb = new StringBuilder();

        if (checkKey(key)) {
            Integer offset = Integer.parseInt(key);
            int output;

            for (Character c: ciphertext.toCharArray()) {
                output = c - 'a' - offset;

                if (output < 0) {
                    output += 26;
                }
                output %= 26;

                sb.append(Utility.alphabet.charAt(output));
            }
        } else throw new InvalidKeyException(key + " is not a valid key!");

        return sb.toString();
    }

    @Override
    public String getDescription() { return DESC; }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Boolean checkKey(String key) throws InvalidKeyException {
        try { Integer.parseInt(key); } catch (NumberFormatException nfe) {
            throw new InvalidKeyException(key + " is not a valid key!");
        }

        return true;
    }
}
