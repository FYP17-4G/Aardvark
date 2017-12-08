import mUtil.Utility;
import mUtil.mPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Transpose {

    public static void main(String[] args) {
        List<List <Character>> grid = new ArrayList<>();
        String preKey = removeDuplicates("kesiceincieny"); //replace with getString()
        List <mPair <Character, Integer>> key = new ArrayList<>();

        for (int i = 0; i < preKey.length(); ++i) {
            char c = preKey.charAt(i);
            mPair<Character, Integer> temp = new mPair<>(c, i);
            key.add(temp);
        }

        String plaintext = readFile("plain.txt");
        System.out.println("plaintext = " + plaintext);

        plaintext = process(plaintext, key.size());

        Utility.line('~', 30, "String");
        grid = encrypt(plaintext, key, grid);

        StringBuilder output = new StringBuilder();
        for (List <Character> tRow: grid) {
            for (Character ch: tRow) {
                output.append(ch);
            }
        }

        System.out.println("output = " + output + "\n");

        Utility.line('~', 30, "Grid");
        for (List<Character> row: grid) {
            System.out.println(row);
        }

    }

    private static List<List<Character>> encrypt (String plaintext,
                                   List <mPair <Character, Integer>> key,
                                   List<List <Character>> cipher) {
        int maxCols = key.size();
        int right = 0;

        //populate grid
        List<Character> row = new ArrayList<>(maxCols);
        for (Character c: plaintext.toCharArray()) {
            row.add(c);
            ++right;

            if (right == maxCols) {
//                System.out.println(row);
                cipher.add(row);
                right = 0;
                row = new ArrayList<>(maxCols);
            }
        }

        //Mix columns
       cipher = mixColumns(cipher, key);

        //flip and output
        return flip(cipher);
    }

    private static List<List<Character>> mixColumns (List<List<Character>> input,
                                                     List <mPair <Character, Integer>> key) {
        //rotate table 90 anti-clockwise
        List<List<Character>> tempOut = flip(input);
        List<List<Character>> output = new ArrayList<>(tempOut.size());

        //sort key by value of key.first
        key.sort(Comparator.comparingInt(lhs -> lhs.first));

        //swap rows
        int temp;
        for (mPair k : key) {
            temp = (int)k.second;
//            System.out.println(key.get(i).second);
            output.add(tempOut.get(temp));
        }

        //rotate table 90 clockwise
        return flip(output);
    }

    private static List<List<Character>> flip(List<List<Character>> input) {
        List<List<Character>> output = new ArrayList<>();
        List<Character> newRow;

        int maxCols = input.get(0).size();

        for (int i = 0; i < maxCols; ++i) {
            newRow = new ArrayList<>();

            for (List<Character> row : input) {
                newRow.add(row.get(i));
            }

            output.add(newRow);
        }

        return output;
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
        String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz01234567890";
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
