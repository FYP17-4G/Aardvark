package modules;

import java.util.*;

public class ColumnarTransposition {
    private static List <mUtil.mPair<Character, Integer>> prepareKey (String input) {
        List <mUtil.mPair<Character, Integer>> key = new ArrayList<>();
        String preKey = removeDuplicates(input); //replace with getString()

        for (int i = 0; i < preKey.length(); ++i) {
            char c = preKey.charAt(i);
            mUtil.mPair<Character, Integer> temp = new mUtil.mPair<Character, Integer>(c, i);
            key.add(temp);
        }

        return key;
    }

    public static List<List<Character>> encrypt (String plaintext, String preKey, List<List <Character>> cipher) {
        //convert the preKey string to a proper key
        List <mUtil.mPair<Character, Integer>> key = prepareKey(preKey);
        int maxCols = key.size();
        int right = 0;

        StringBuilder sb = new StringBuilder(plaintext);
        while ((sb.length() % key.size()) != 0) {
            sb.append(' ');
        }
        plaintext = sb.toString();

        System.out.println("plaintext = " + plaintext);

        //populate grid
        List<Character> row = new ArrayList<>();
        for (Character c: plaintext.toCharArray()) {
            row.add(c);
            ++right;

            if (right >= maxCols) {
                right = 0;
                cipher.add(row);
                row = new ArrayList<>();
            }
        }

        //Mix columns
       cipher = mixColumns(cipher, key);

        //flip and output
        return flip(cipher);
    }

    public static List<List<Character>> decrypt (String ciphertext, String preKey,
                                                 List<List <Character>> plain) {
        List <mUtil.mPair<Character, Integer>> key = prepareKey(preKey);
        int maxCols = key.size();
        int right = 0;

        List<Character> row = new ArrayList<>();
        for (Character c: ciphertext.toCharArray()) {
            row.add(c);
            ++right;

            if (right >= maxCols) {
                right = 0;
                plain.add(row);
                row = new ArrayList<>();
            }
        }

        plain = mixBack(plain, key);

        return flip(plain);
    }

    private static List<List<Character>> mixColumns (List<List<Character>> input,
                                                     List <mUtil.mPair<Character, Integer>> key) {
        //rotate table 90 anti-clockwise
        List<List<Character>> tempOut = flip(input);
        List<List<Character>> output = new ArrayList<>(tempOut.size());

        for (mUtil.mPair <Character, Integer> k: key) {
            System.out.print("k.first = " + k.first + ", ");
            System.out.println("k.second = " + k.second);
        }

        //sort key by value of key.first
        key.sort(Comparator.comparingInt(lhs -> lhs.first));

        for (mUtil.mPair <Character, Integer> k: key) {
            System.out.print("k.first = " + k.first + ", ");
            System.out.println("k.second = " + k.second);
        }

        //swap rows
        int temp;
        for (mUtil.mPair k : key) {
            temp = (int)k.second;
//            System.out.println(key.get(i).second);
            output.add(tempOut.get(temp));
        }

        //rotate table 90 clockwise
        return flip(output);
    }

    private static List<List<Character>> mixBack (List<List<Character>> input,
                                                  List <mUtil.mPair<Character, Integer>> key) {
        //rotate table 90 anti-clockwise
        List<List<Character>> tempOut = flip(input);
        List<List<Character>> output = new ArrayList<>(tempOut.size());
        
        for (mUtil.mPair <Character, Integer> k: key) {
            System.out.print("k.first = " + k.first + ", ");
            System.out.println("k.second = " + k.second);
        }

        //swap rows
        int temp;
        for (mUtil.mPair k : key) {
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
}
