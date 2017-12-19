package modules;

import com.Utility;

import java.util.ArrayList;
import java.util.List;

public class RectKeySubstitution {
    public static void encrypt (String plaintext, String key) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String otp;
        key = key.replaceAll("[^A-Za-z]", "");
        key = removeDuplicates(key.toLowerCase());

        StringBuilder sb = new StringBuilder(alphabet);

        while ((sb.length() % key.length()) != 0)
            sb.append(Utility.randomAlphaNumeric());

        alphabet = sb.toString();

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

            if (counter >= key.length()) {
                rectangle.add(row);
                counter = 0;

                row = new ArrayList<>();
            }
        }

        sb = new StringBuilder();
        ArrayList<Character> subs = new ArrayList<>();
        for (List<Character> x: rectangle) {
            for (Character y: x) {
                sb.append(y);
                subs.add(y);
            }
        }

        otp = sb.toString();

        for (Character c: plaintext.toCharArray()) {

        }
    }

    public static boolean contains (Character c, String str) {
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
}
