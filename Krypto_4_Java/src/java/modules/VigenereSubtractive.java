/*
 * ---------
 * Module Name: VigenereSubtractive.java
 * An additive cipher where the ciphertext's value is obtained by addiong the value of the plaintext
 * to the value of the repeating key (mod 26). CT_i = K_i - PT_i (mod 26)
 * i [<p>] in Krypto.exe
 * ---------
 * @params: data            -> input
 *          sequenceLength  -> length of sequence to check for
 *          maxEntries      -> max number of entries to display
 * Returns the ciphertext (if encrypting) or the plaintext (if decrypting)
 * ---------
 */

package modules;

import com.Cipher;
import com.InvalidKeyException;
import com.Utility;

public class VigenereSubtractive implements Cipher {
    private static final String DESC = "Subtractive Vigenere Cipher";
    private static final String NAME = "Subtractive Vigenere Cipher";

    @Override
    public String encrypt(String plaintext, String key) throws InvalidKeyException {
        key = key.toLowerCase();
        if (checkKey(key)) {
            StringBuilder out = new StringBuilder();
            int position = 0;

            for (Character c: plaintext.toCharArray()) {
//            System.out.println("key.charAt(position) = " + key.charAt(position));

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
        }

        return "Failed.";
    }

    @Override
    public String decrypt(String ciphertext, String key) throws InvalidKeyException {
        key = key.toLowerCase();

        if (checkKey(key)) {
            StringBuilder out = new StringBuilder();
            int position = 0;
            for (Character c: ciphertext.toCharArray()) {
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

        return "Failed";
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
        if (key.length() < 1) throw new InvalidKeyException("Key is too short!");
        for (Character c: key.toCharArray()) {
            if (!Character.isAlphabetic(c)) throw new InvalidKeyException("Illegal characters in key!");
        }

        return true;
    }

    //encrypt ONE character
    private static Character encryptOne (Character plaintext, Character key) {
        int res;

        res = key - 'a';
        res -= plaintext - 'a';

        if (res < 0)
            res += Utility.alphabet.length();

        res %= Utility.alphabet.length();

        return Utility.alphabet.charAt(res);
    }

    //decrypt ONE character
    private static Character decryptOne (Character plaintext, Character key) {
        int res;

        res = key - 'a';
        res -= plaintext - 'a';

        if (res < 0)
            res += Utility.alphabet.length();

        res %= Utility.alphabet.length();

        return Utility.alphabet.charAt(res);
    }
}
