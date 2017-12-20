package modules;

import com.Utility;

import java.util.ArrayList;
import java.util.List;

public class RectKeySubstitution {
    public static String encrypt (String plaintext, String key) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String otp;
        key = key.replaceAll("[^A-Za-z]", "");
        key = removeDuplicates(key.toLowerCase());

        System.out.println("key = " + key);

        ArrayList<Character> keyArray = new ArrayList<>(key.length());
        for (Character c: key.toCharArray()) {
            keyArray.add(c);
        }

        List<List<Character>> rectangle = new ArrayList<>();
        rectangle.add(keyArray);

        ArrayList<Character> row = new ArrayList<>();
        int counter = 0;
        for (Character c: alphabet.toCharArray()) {
            if (!contains(c, key)) {
                row.add(c);
                ++counter;
            }

            if (counter >= key.length() || c == alphabet.charAt(alphabet.length() - 1)) {
                rectangle.add(row);
                counter = 0;

                row = new ArrayList<>();
            }
        }

        StringBuilder sb = new StringBuilder();
//        ArrayList<Character> subs = new ArrayList<>();
        rectangle = flip(rectangle);
        for (List<Character> x: rectangle) {
            for (Character y: x) {
                sb.append(y);
//                subs.add(y);
            }
        }

        otp = sb.toString();
        System.out.println("otp = " + otp);

        sb = new StringBuilder();
        Character cipherChar;
        for (Character c: plaintext.toCharArray()) {
            cipherChar = otp.charAt(Utility.getIndex(c));
            sb.append(cipherChar);
        }

        return sb.toString();
    }

    private static boolean contains (Character c, String str) {
        for (Character x: str.toCharArray()) {
            if (c == x)
                return true;
        }

        return false;
    }

    private static String removeDuplicates (String in) {
        ArrayList<Character> charList = new ArrayList<>();
        StringBuilder out = new StringBuilder();

        for (Character c: in.toCharArray()) {
            if (!charList.contains(c))
                charList.add(c);
        }

        for (Character c: charList) {
            out.append(c);
        }

        return out.toString();
    }

    private static List<List<Character>> flip(List<List<Character>> input) {
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
}
