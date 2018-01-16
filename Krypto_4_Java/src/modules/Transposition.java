package modules;

import com.Cipher;
import com.InvalidKeyException;
import com.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transposition implements Cipher {
    private static final String DESC = "Transposition Cipher";
    private static final String NAME = "Transposition Cipher";

    @Override
    public String encrypt(String plaintext, String key) throws InvalidKeyException {
        key = key.toLowerCase();

        if (checkKey(key)) {
            plaintext = Utility.pad(plaintext, key.length());
            ArrayList<Integer> intKey = convertToKey(key); // <- never = 3, 1, 5, 2, 4

            List<List<Character>> rect = getRectangle(plaintext, key.length());     //01
            rect = flip(rect);                                                      //02
            rect = mix(rect, intKey);                                               //03
            rect = flip(rect);                                                      //04

    //       01.                  02.                  03.                  04.
    //        1, 2, 3, 4, 5       1  [w, o, i, m]      3  [a, s, a, a]       3, 1, 5, 2, 4
    //       [w, h, a, t, d]      2  [h, e, s, e]      1  [w, o, i, m]      [a, w, d, h, t]
    //       [o, e, s, t, h]  ->  3  [a, s, a, a]  ->  5  [d, h, l, g]  ->  [s, o, h, e, t]
    //       [i, s, a, l, l]      4  [t, t, l, n]      2  [h, e, s, e]      [a, i, l, s, l]
    //       [m, e, a, n, g]      5  [d, h, l, g]      4  [t, t, l, n]      [a, m, g, e, n]

            StringBuilder ciphertext = new StringBuilder();
            for (List<Character> row : rect) {
                for (Character ch : row) {
                    ciphertext.append(ch);
                }
            }

            return ciphertext.toString();
        }

        return "Failed.";
    }

    @Override
    public String decrypt(String ciphertext, String key) throws InvalidKeyException {
        key = key.toLowerCase();

        ArrayList<Integer> intKey = convertToKey(key);

        if (checkKey(key)) {
            List<List<Character>> rect = getRectangle(ciphertext, key.length());     //01
            rect = flip(rect);                                                       //02
            rect = unmix(rect, intKey);                                              //03
            rect = flip(rect);                                                       //04

            //       04.                  03.                  02.                  01.
            //        1, 2, 3, 4, 5       1  [w, o, i, m]      3  [a, s, a, a]       3, 1, 5, 2, 4
            //       [w, h, a, t, d]      2  [h, e, s, e]      1  [w, o, i, m]      [a, w, d, h, t]
            //       [o, e, s, t, h]  <-  3  [a, s, a, a]  <-  5  [d, h, l, g]  <-  [s, o, h, e, t]
            //       [i, s, a, l, l]      4  [t, t, l, n]      2  [h, e, s, e]      [a, i, l, s, l]
            //       [m, e, a, n, g]      5  [d, h, l, g]      4  [t, t, l, n]      [a, m, g, e, n]

            StringBuilder plaintext = new StringBuilder();
            for (List<Character> row : rect) {
                for (Character ch : row) {
                    plaintext.append(ch);
                }
            }

            return plaintext.toString();
        }

        return "Failed.";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Boolean checkKey(String key) throws InvalidKeyException {
        if (key.length() <= 0) throw new InvalidKeyException("Key too short!");

        for (Character c: key.toCharArray()) {
            if (!Character.isAlphabetic(c)) throw new InvalidKeyException("Illegal character(s) in key!");
        }

        return true;
    }

    private List<List<Character>> getRectangle (String plaintext, int keylength) {
        List<List<Character> > rectangle = new ArrayList<>();

        int counter = 0;
        List<Character> row = new ArrayList<>();
        for (Character c: plaintext.toCharArray()) {
            row.add(c);

            if (++counter >= keylength) {
                rectangle.add(row);
                row = new ArrayList<>();
                counter = 0;
            }
        }

        if (counter != 0)
            rectangle.add(row);

        return rectangle;
    }

    private List<List<Character>> flip(List<List<Character>> input) {
        List<List<Character>> output = new ArrayList<>();
        List<Character> newRow;

        int maxCols = input.get(0).size();

        for (int i = 0; i < maxCols; ++i) {
            newRow = new ArrayList<>();

            for (List<Character> row : input) {
                if (i < row.size())
                    newRow.add(row.get(i));
            }

            output.add(newRow);
        }

        return output;
    }

    private List<List<Character>> mix (List<List<Character>> input, ArrayList<Integer> key) {
        List<List <Character>> output = new ArrayList<>();

        for (Integer x: key) {
            output.add(input.get(x - 1));
        }

        return output;
    }

    private List<List<Character>> unmix (List<List <Character>> input, ArrayList<Integer> key ) {
        List<List<Character>> output = new ArrayList<>();

        int current = 1;
        int row = 0;
        while (current <= key.size()) {
            row = 0;
            for (Integer k : key) {
                if (k == current) {
                    output.add(input.get(row));
                }

                ++row;
            }
            ++current;
        }

        return output;
    }

    private ArrayList<Integer> convertToKey (String key) {
        String temp = key.toLowerCase();

        String sorted = sortString(temp);
        System.out.println("sorted = " + sorted);
        ArrayList<Integer> intKey = new ArrayList<>(temp.length());
        ArrayList<Integer> returnable = new ArrayList<>();
        for (int i = 1; i <= temp.length(); ++i) { intKey.add(i); }

        int ret, index;
        for (Character c: temp.toCharArray()) {
             index = find (c, sorted); //get index of the first c in sorted;

            //remove first instance of c
            sorted = sorted.replaceFirst(c.toString(), ""); //remove c from sorted;

//            System.out.println("index = " + index);
            ret = intKey.get(index);
            intKey.remove(index);

//            System.out.println("key = " + sorted);
//            System.out.println("intKey = " + intKey);
//
//            System.out.println("ret = " + ret);
            returnable.add(ret);
        }

        return returnable;
    }

    //returns the first instance of ch in str
    private int find (Character ch, String str) {
        int count = 0;
        for (Character c: str.toCharArray()) {
            if (ch == c) return count;

            ++count;
        }

        return -1;
    }

    private String sortString (String key) {
        char[] temp = key.toCharArray();
        Arrays.sort(temp);
        return new String (temp);
    }
}
