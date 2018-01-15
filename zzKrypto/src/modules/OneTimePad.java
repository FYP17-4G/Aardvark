package modules;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class OneTimePad {
    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private static String getPad(int size) {
        int tmp;
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            tmp = rnd.nextInt(alphabet.length());

            sb.append(alphabet.toCharArray()[tmp]);
        }

        return sb.toString();
    }

    public static ArrayList<String> encrypt(String plaintext) {

        ArrayList<String> output = new ArrayList<>();
        String pad = getPad(plaintext.length());
        output.add(pad);
        StringBuilder ciphertext = new StringBuilder();

        char p, c, out;
        for (int i = 0; i < plaintext.length(); ++i) {
            p = plaintext.charAt(i);
            c = pad.charAt(i);

            out = alphabet.charAt(( (p - 'a') + (c - 'a')) % 26);
            ciphertext.append(out);
        }

        output.add(ciphertext.toString());
        return output;
    }

    public static String decrypt (String pad, String ciphertext) throws IOException {
        if (ciphertext.length() != pad.length())
            throw new IOException("Ciphertext and Pad are not compatible!");

        StringBuilder sb = new StringBuilder();
        int c, p, out;
        for (int i = 0; i < ciphertext.length(); ++i) {
            c = ciphertext.charAt(i);
            p = pad.charAt(i);
            out =  (c - 'a') - (p - 'a');

            if (out < 0)
                out += 26;

            out %= 26;
            sb.append(alphabet.charAt(out));
        }

        return sb.toString();
    }
}
