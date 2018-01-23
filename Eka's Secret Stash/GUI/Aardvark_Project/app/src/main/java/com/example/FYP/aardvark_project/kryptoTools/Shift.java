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

package com.example.FYP.aardvark_project.kryptoTools;


public class Shift implements Cipher {
    private final String cipherDescription = "Shifts the input by 'key'. Allows encryption & decryption.";
    private final String cipherName = "Shift Cipher";

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
    public String getDescription() { return cipherDescription; }

    @Override
    public String getName() {
        return cipherName;
    }

    @Override
    public Boolean checkKey(String key) throws InvalidKeyException {
        try { Integer.parseInt(key); } catch (NumberFormatException nfe) {
            throw new InvalidKeyException(key + " is not a valid key!");
        }

        return true;
    }
}
