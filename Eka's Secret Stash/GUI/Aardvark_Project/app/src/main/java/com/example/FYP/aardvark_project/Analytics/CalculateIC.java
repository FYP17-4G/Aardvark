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

package com.example.FYP.aardvark_project.Analytics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculateIC {
    //get IC for a bunch of data, reading from every nth letter.
    //data entered here is the whole set of data. the function will split the string accordingly.
    //returns an ArrayList <Double>: the IC for each subset of characters (n subsets) in data.
    //the last value in the ArrayList should be the Average of all the ICs.
    public static List<Double> getIC(String data, Integer p) {
        ArrayList<StringBuilder> splitStrings = getEveryNthLetter(p, data);
        ArrayList<Double> IC = new ArrayList<>(p);
        Double sum = 0.0;

        for (StringBuilder sb : splitStrings) {
            Double ic = calculate(sb.toString());
            IC.add(ic);
            sum += ic;
        }

        IC.add (sum / IC.size());

        return IC;
    }

    public static List<Double> getIC(String data) {
        return getIC(data, 1);
    }

    //get IC for a particular string of data
    private static double calculate(String data) {

        try{
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
            return BigDecimal.valueOf((numer / denom)).setScale(15, RoundingMode.HALF_UP).doubleValue();
        }catch(NumberFormatException e) {
            return 0;
        }
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
