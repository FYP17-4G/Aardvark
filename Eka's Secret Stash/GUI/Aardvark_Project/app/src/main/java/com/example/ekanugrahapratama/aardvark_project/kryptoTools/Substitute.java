package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

public class Substitute {
    public static String sub (Character oldC, Character newC, String mData) {
        StringBuilder sb = new StringBuilder();
        for (Character x: mData.toCharArray()) {
            if (x == oldC)
                sb.append(newC);
            else
                sb.append(x);
        }

        return sb.toString();
    }
}
