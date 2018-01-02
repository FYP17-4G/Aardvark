package modules;

import java.io.IOException;
import java.util.ArrayList;

public class Substitute {
    public static String ch (Character oldC, Character newC, String mData, String oData) {
        char[] original = oData.toLowerCase()
                .replaceAll("[^A-Za-z]", "").toCharArray();
        char[] modified = mData.toCharArray();

        for (int i = 0; i < modified.length; ++i) {
            if (oldC == modified[i] && modified[i] == original[i]) {
                modified[i] = newC;
            }
        }

        return new String (modified);
    }

    //doesn't check with the original text to only change modified text
    public static String str (String oldStr, String newStr, String mData, String oData) throws IOException {
        if (oldStr.length() != newStr.length()) {
            System.err.println("Strings are not the same length.");
            throw new IOException();
        }

        ArrayList<String> original, modified;
        original = new ArrayList<>();



        return mData.replaceAll(oldStr, newStr).toLowerCase();
    }
}
