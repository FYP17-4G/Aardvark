/*
 * ---------
 * Module Name: Substitution.java
 * Performs the "Substitution Cipher"
 * ---------
 * @params: plaintext/ciphertext -> the text to be encrypted/decrypted.
 *          key -> the key to use to perform the encryption/decryption.
 *
 * Encrypt: Returns the ciphertext, when the oldCharacters in  plaintext have been substituted by newCharacters, OR
 * Decrypt: Returns the  plaintext, when the newCharacters in ciphertext have been substituted by oldCharacters.
 *
 * byCharacter
 * @params: oldCharacter -> the character to replace (for encrypt / decrypt)
 *          newCharacter -> the character to replace oldCharacter with.
 *          modifiedData -> the data which will be modified.
 *          originalData -> the data which controls the modification, to ensure there aren't any conflicts.
 * Allows the user to substitute a character with another, making sure we don't overwrite any previous changes.
 * ---------
*/

package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Substitution implements Cipher {
    private String DESC = "Substitution Cipher";
    private String NAME = "Substitution Cipher";
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";

    //allows the user to substitute a single character
    //returns a String with all the oldCharacters changed to the newCharacters.
    //modifiedData = String that will be modified
    //originalData = String that will be used as a control, so there aren't any conflicts when subbing.
    public String byCharacter (Character oldCharacter, Character newCharacter,
                             String modifiedData, String originalData) {
        char[] original = originalData.toLowerCase()
                .replaceAll("[^A-Za-z]", "").toCharArray();
        char[] modified = modifiedData.toCharArray();

        for (int i = 0; i < modified.length; ++i) {
            if (oldCharacter == modified[i] && modified[i] == original[i]) {
                modified[i] = newCharacter;
            }
        }

        return new String (modified);
    }

    @Override
        public String encrypt(String plaintext, String key) throws InvalidKeyException {
        String output = plaintext;

        if (checkKey(key)) {
            for (int i = 0; i < alphabet.length(); ++i) {
                char currLetter = alphabet.charAt(i);
                char currKey    = key.charAt(i);

                output = byCharacter(currLetter, currKey, output, plaintext);
            }
        }

        return output;
    }

    @Override
    public String decrypt(String ciphertext, String key) throws InvalidKeyException {
        String output = ciphertext;

        if (checkKey(key)) {
            for (int i = 0; i < alphabet.length(); ++i) {
                char currLetter = alphabet.charAt(i);
                char currKey    = key.charAt(i);

                output = byCharacter(currKey, currLetter, output, ciphertext);
            }
        }

        return output;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Boolean checkKey(String key) throws InvalidKeyException {
        //check key length
        if (key.length() != alphabet.length())
            throw new InvalidKeyException("Key must be exactly " + alphabet.length() + " characters!");

        //check if key has duplicate characters
        String newKey = Utility.removeDuplicates(key);
        if (key.length() != newKey.length())
            throw new InvalidKeyException("Key cannot contain duplicates!");

        //check if key contains illegal characters
        Pattern pattern = Pattern.compile("[^A-Za-z]");
        Matcher matcher = pattern.matcher(key);
        if (matcher.find())
            throw new InvalidKeyException("Key can only contain [A-Z/a-z]!");

        return true;
    }
}
