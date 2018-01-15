/*
 * ---------
 * Module Name: VigenereSubtractive.java
 * An additive cipher where the ciphertext's value is obtained by addiong the value of the plaintext
 * to the value of the repeating key (mod 26). CT_i = PT_i - K_i (mod 26)
 * i [<p>] in Krypto.exe
 * ---------
 * @params: data            -> input
 *          sequenceLength  -> length of sequence to check for
 *          maxEntries      -> max number of entries to display
 * Returns the ciphertext (if encrypting) or the plaintext (if decrypting)
 * ---------
 */

package modules;

public class VigenereSubtractive {
    private static String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
    private VigenereSubtractive(){}

    public static String encrypt (String plaintext, String key) {
        key = key.replaceAll("[^a-zA-Z]", "");
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

    public static String decrypt (String ciphertext, String key) {
        key = key.replaceAll("[^a-zA-Z0-9]", "");
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


    //encrypt ONE character
    private static Character encryptOne (Character plaintext, Character key) {
        int res;

        res = key - 'a';
        res -= plaintext - 'a';

        if (res < 0)
            res += ALPHABETS.length();

        res %= ALPHABETS.length();

        return ALPHABETS.charAt(res);
    }

    //decrypt ONE character
    private static Character decryptOne (Character plaintext, Character key) {
        int res;

        res = key - 'a';
        res -= plaintext - 'a';

        if (res < 0)
            res += ALPHABETS.length();

        res %= ALPHABETS.length();

        return ALPHABETS.charAt(res);
    }
}
