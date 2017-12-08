import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Vigenere {
    public static void main(String[] args) {
        String plaintext    = readFile(args[0]);
        String key          = readFile(args[1]);

        System.out.println("key = " + key);
        System.out.println("plaintext = " + plaintext);

        String ciphertext = encrypt(plaintext, key);
        System.out.println("Encrypted = " + ciphertext);

        String decrypted = decrypt(ciphertext, key);
        System.out.println("Decrypted = " + decrypted);
    }

    private static String readFile (String filename) {
        File infile = new File (filename);
        BufferedReader reader;
        StringBuilder output = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(infile));
            output = new StringBuilder();

            String line;
            while ( (line = reader.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }

            reader.close();
        } catch (IOException o) {
            System.out.println("Couldn't find file: " + filename);
        }

        return output.toString();
    }

    private static String encrypt (String plaintext, String key) {
        key = key.replaceAll("[^a-zA-Z0-9]", "");
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

    private static String decrypt (String ciphertext, String key) {
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
        String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
        int res, pt, k;
        if (Character.isUpperCase(key)) {
            k = key - 'A';
        } else if (Character.isLowerCase(key)) {
            k = key - 'a';
        } else {
            char temp = ALPHABETS.charAt((int)key);
            k = temp - 'a';
        }

        if (Character.isLowerCase(plaintext)) {
            pt = plaintext - 'a';

            res = pt;
            res += k;
            res %= ALPHABETS.length();

            return ALPHABETS.charAt(res);
        } else if (Character.isUpperCase(plaintext)) {
            pt = plaintext - 'A';

            res = pt;
            res += k;
            res %= ALPHABETS.length();

            return ALPHABETS.toUpperCase().charAt(res);
        } else {
            return plaintext;
        }
    }

    //decrypt ONE character
    private static Character decryptOne (Character plaintext, Character key) {
        String ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
        int res, pt, k;
        if (Character.isUpperCase(key)) {
            k = key - 'A';
        } else if (Character.isLowerCase(key)) {
            k = key - 'a';
        } else {
            char temp = ALPHABETS.charAt((int)key);
            k = temp - 'a';
        }

        if (Character.isLowerCase(plaintext)) {
            pt = plaintext - 'a';

            res = pt;
            res -= k;
            res %= ALPHABETS.length();
            if (res < 0) {
                res += ALPHABETS.length();
            }

            return ALPHABETS.charAt(res);
        } else if (Character.isUpperCase(plaintext)) {
            pt = plaintext - 'A';

            res = pt;
            res -= k;
            res %= ALPHABETS.length();

            if (res < 0) {
                res += ALPHABETS.length();
            }

            return ALPHABETS.toUpperCase().charAt(res);
        } else {
            return plaintext;
        }
    }
}
