/*
 * ---------
 * Module Name: Vigenere.java
 * A cipher where ciphertext is obtained by adding the value of the plaintext
 * to the value of the repeating key (mod 26). CT_i = PT_i + K_i (mod 26)
 * i [<p>] in Krypto.exe
 * ---------
 * @params: data            -> input
 *          sequenceLength  -> length of sequence to check for
 *          maxEntries      -> max number of entries to display
 * Returns the ciphertext (if encrypting) or the plaintext (if decrypting)
 * ---------
 */

package com.example.FYP.aardvark_project.Ciphers;

/**
 * Implementation of the Vigenere Cipher.
 */
public class VigenereCipher extends AbstractCipher implements CipherInterface {
    /**
     * Holds the description of the Viginere Cipher.
     */
    private static final String DESC = "Additive Vigenere Cipher";
    /**
     * Holds the name of the Viginere Cipher.
     */
    private static final String NAME = "Additive Vigenere Cipher";

    @Override
    public String encrypt(String plaintext, String key)  {
        key = key.toLowerCase();

       if (checkKey(key)) {
           StringBuilder out = new StringBuilder();
           int position = 0;

           for (Character c: plaintext.toCharArray()) {

               if (position >= (key.length())) {
                   position = 0;
               }

               if (Character.isAlphabetic(c)) {
                   out.append(encryptOne(c, key.charAt(position++)));
               } else {
                   out.append(c);
               }
           }

           return out.toString();
       } else {
           return "Failed.";
       }
    }

    @Override
    public String decrypt(String ciphertext, String key)  {
        key = key.toLowerCase();
        if (checkKey(key)) {
            StringBuilder out = new StringBuilder();
            int position = 0;
            for (Character c : ciphertext.toCharArray()) {
                if (position >= (key.length())) {
                    position = 0;
                }

                if (Character.isAlphabetic(c)) {
                    out.append(decryptOne(c, key.charAt(position++)));
                } else {
                    out.append(c);
                }
            }
            return out.toString();
        }

        return "Failed.";
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
    public Boolean checkKey(String key) {
        if (key.length() < 1) {
            System.out.println("Key is too short!");
        }
        
        for (Character c: key.toCharArray()) {
            if (!Character.isAlphabetic(c));
        }

        return true;
    }

    //encrypt ONE character
    private Character encryptOne (Character plaintext, Character key) {
        int res, k;
        k = key - 'a';

        res = plaintext - 'a';
        res += k;


        res %= ALPHABETS.length();

        if (res < 0)
            res += 26;

        return ALPHABETS.charAt(res);
    }

    //decrypt ONE character
    private Character decryptOne (Character plaintext, Character key) {
        int res, k;
        k = key - 'a';

        res = plaintext - 'a';
        res -= k;
        

        if (res < 0)
            res += 26;

        res %= ALPHABETS.length();

        return ALPHABETS.charAt(res);
    }
}
