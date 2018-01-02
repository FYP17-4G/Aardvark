package modules;

import java.io.IOException;

public class Substitute {
    public static String ch (Character oldC, Character newC, String mData) {
        return mData.replaceAll(oldC.toString(), newC.toString()).toLowerCase();
    }

    public static String str (String oldStr, String newStr, String mData) throws IOException {
        if (oldStr.length() != newStr.length()) {
            throw new IOException();
        }

        return mData.replaceAll(oldStr, newStr).toLowerCase();
    }
}
