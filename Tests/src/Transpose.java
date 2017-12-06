import mUtil.mPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transpose {
    private static String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz01234567890";

    public static void main(String[] args) {
        List<List <Character>> grid = new ArrayList<>();
        String preKey = removeDuplicates("kesiceincieny");
        List <mPair <Character, Integer>> key = new ArrayList<>();

        for (int i = 0; i < preKey.length(); ++i) {
            char c = preKey.charAt(i);
            mPair<Character, Integer> temp = new mPair<>(c, i);
            key.add(temp);
        }

        String plaintext = readFile("plain.txt");
        System.out.println("plaintext = " + plaintext);

        plaintext = process(plaintext, key.size());
        grid = encrypt(plaintext, key);
        for (List<Character> row: grid) {
            System.out.println(row);
        }
    }

    private static List<List<Character>> encrypt (String plaintext,
                           List <mPair <Character, Integer>> key) {
        int maxCols = key.size();
        List<List <Character>> cipher = new ArrayList<>();
        System.out.println("maxCols = " + maxCols);
        int right = 0;

        List<Character> row = new ArrayList<>(maxCols);
        for (Character c: plaintext.toCharArray()) {
            row.add(c);
            ++right;

            if (right == maxCols) {
                System.out.println(row);
                cipher.add(row);
                right = 0;
                row = new ArrayList<>(maxCols);
            }
        }

//        System.out.println("cipher.size() = " + cipher.size());
        return cipher;
    }

    private static String removeDuplicates (String in) {
        Set<Character> temp = new HashSet<>();
        StringBuilder out = new StringBuilder();

        for (Character c: in.toCharArray()) {
            temp.add(c);
        }

        for (Character c: temp) {
            out.append(c);
        }

        return out.toString();
    }

    private static String readFile (String filename) {
        File inFile = new File(filename);
        BufferedReader reader;
        StringBuilder out = new StringBuilder();
        try {
            int c;
            reader = new BufferedReader(new FileReader(inFile));
            while ( (c = reader.read()) != -1){
                out.append((char)c);
            }

            reader.close();
        } catch (IOException io) {
            System.out.println("File not found");
        }

        return out.toString();
    }

    private static Character randomAlphaNumeric () {
        Integer random = (int) (Math.random() * (ALPHANUMERIC.length()));
        return ALPHANUMERIC.charAt(random);
    }

    private static String pad (String original, Integer keyLength) {
        StringBuilder padded = new StringBuilder(original);

        while ( (padded.length() % keyLength) != 0) {
            padded.append(randomAlphaNumeric());
        }

        return padded.toString();
    }

    private static String process (String original, Integer keyLength) {
        original = original.replaceAll("[^a-zA-Z0-9]", "");
        original = pad(original, keyLength);

        return original;
    }
}
