/*
 * ---------
 * Module Name: Graph.java
 * Displays a graph showing the frequency distribution of characters in the distance and period.
 * g [<d> <p>] in Krypto.exe
 * ---------
 * @params: data    -> input
 *          d       -> distance
 *          p       -> period
 * Returns an Integer[26], which contains the counts for each of the 26 characters of the English alphabet.
 * ---------
 */

package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

import java.util.Arrays;

public class Graph {
    //don't need to check for integer, because Eka will be using a combo-box or something similar
    //the inputs will be controlled. Or we can do some checks before even submitting the request.
    private static Integer[] count = new Integer[26];

    public static Integer[] displayGraph(String input) {
        Arrays.fill(count, 0);
        for (Character c: input.toLowerCase().toCharArray()) {
            ++count[c - 'a'];
        }

        return count;
    }

    public static Integer[] displayGraph (String input, Integer distance, Integer period) {
        Arrays.fill(count, 0);
        int max = input.length();

        for (int i = distance; i < input.length(); i += period) {
            char c = input.charAt(i);
            ++count[c - 'a'];
        }

        return count;
    }
}
