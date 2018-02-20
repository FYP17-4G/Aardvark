/*
 * ---------
 * Module Name: CalculateIC.java
 * Calculates the Index of Coincidence (IC) of every period.
 * i [<p>] in Krypto.exe
 * ---------
 * @params: p -> Period
 * Returns either a single Double (if p is 1) OR
 * an ArrayList<Double> (if p > 1)
 * ---------
 */

package com.example.FYP.aardvark_project.kryptoTools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class CalculateIC {
    //get IC for a bunch of data, reading from every nth letter.
    //data entered here is the whole set of data. the function will split the string accordingly.
    public static ArrayList<Double> getIC(Integer n, String data) {
        ArrayList<StringBuilder> splitStrings = getEveryNthLetter(n, data);
        ArrayList<Double> IC = new ArrayList<>(n);

        for (StringBuilder sb : splitStrings) {
            IC.add(getIC(sb.toString()));
        }

        return IC;
    }

    //get IC for a particular string of data
    public static double getIC(String data) {
        int[] counts = new int[26];
        Arrays.fill(counts, 0);

        int index, totalChars = 0;
        double numer = 0.0, denom;
        data = data.toLowerCase();

        for (Character c : data.toLowerCase().toCharArray()) {
            if (Character.isAlphabetic(c)) {
                index = c - 'a';
                ++counts[index];
                ++totalChars;
            }
        }

        for (Integer n : counts) {
            numer += (n * (n - 1));
        }

        denom = totalChars * (totalChars - 1);

        Double IC;

        try
        {
            IC = BigDecimal.valueOf((numer / denom)).setScale(3, RoundingMode.HALF_UP).doubleValue();
        }catch(NumberFormatException e)
        {
            IC = 0.00;
        }

        return IC;
    }

    //compiles every nth letter into an ArrayList of strings, according to n.
    //data entered here is the whole set of data. the function will split the string accordingly.
    //ignores punctuation and spaces.
    //e.g. getEveryNthLetter (4, hello world!) results in:
    //1. hol    2. ewd
    //3. lo     4. lr
    public static ArrayList<StringBuilder> getEveryNthLetter(Integer n, String data) {
        data = data.replaceAll("[^A-Za-z]", "");
        ArrayList<StringBuilder> split = new ArrayList<>(n);
        int counter = 0;

        for (int i = 0; i < n; ++i) {
            split.add(new StringBuilder());
        }

        for (Character c : data.toLowerCase().toCharArray()) {
            split.get(counter).append(c);
            ++counter;

            if (counter >= n)
                counter = 0;
        }

        return split;
    }
}
