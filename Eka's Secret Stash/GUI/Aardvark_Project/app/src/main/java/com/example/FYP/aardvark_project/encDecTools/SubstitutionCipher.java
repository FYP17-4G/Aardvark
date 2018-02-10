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

package com.example.FYP.aardvark_project.encDecTools;

// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

public class SubstitutionCipher extends AbstractCipher implements CipherInterface {
    private static final String DESC = "SubstitutionCipher Cipher";
    private static final String NAME = "SubstitutionCipher Cipher";

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
    public String encrypt(String plaintext, String key)  {
        String output = plaintext;

        if (checkKey(key)) {
            for (int i = 0; i < ALPHABETS.length(); ++i) {
                char currLetter = ALPHABETS.charAt(i);
                char currKey    = key.charAt(i);

                output = byCharacter(currLetter, currKey, output, plaintext);
            }
        }

        return output;
    }

    @Override
    public String decrypt(String ciphertext, String key)  {
        String output = ciphertext;

        if (checkKey(key)) {
            for (int i = 0; i < ALPHABETS.length(); ++i) {
                char currLetter = ALPHABETS.charAt(i);
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
}
