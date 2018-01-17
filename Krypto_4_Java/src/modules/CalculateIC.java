/*
 * ---------
 * Module Name: CalculateIC.java
 * Calculates the Index of Coincidence (IC) of every period.
 * i [<p>] in Krypto.exe
 * ---------
 * @params: String  data -> the input text 
 *          Integer p -> period
 * Returns either a List<Double> of all the ICs of the specified <p> from <data>.
 * The LAST element of the list is the AVERAGE IC.
 * ---------
 */

package modules;

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
            Double ic = getIC(sb.toString());
            IC.add(ic);
            sum += ic;
        }

        IC.add (sum / IC.size());

        return IC;
    }

    //get IC for a particular string of data
    private static double getIC(String data) {
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

        Double IC = BigDecimal.valueOf((numer / denom)).setScale(3, RoundingMode.HALF_UP).doubleValue();

        return IC;
    }

    //compiles every nth letter into an ArrayList of strings, according to n.
    //data entered here is the whole set of data. the function will split the string accordingly.
    //ignores punctuation and spaces.
    //e.g. getEveryNthLetter (4, hello world!) results in:
    //1. hol    2. ewd
    //3. lo     4. lr
    private static ArrayList<StringBuilder> getEveryNthLetter(Integer n, String data) {
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
