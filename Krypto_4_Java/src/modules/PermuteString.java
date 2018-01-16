package modules;

import com.Utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class PermuteString {
    public static List<String> permute (String input) {
        List<String> splittedStrings;
        Integer blockSize = input.length();
        Integer[] key = new Integer[blockSize];
        StringBuilder output = new StringBuilder();

        Collection<String> uniquePermutations = new HashSet<>();
        //populate the "key" <- which is the number of characters in the input
        //e.g. 6 characters: key -> [0, 1, 2, 3, 4, 5]
        for (int i = 0; i < blockSize; ++i) { key[i] = i; }
        splittedStrings = Utility.splitStrings(input, blockSize);
        List<List <Integer>> permutations = getPermutations(key);

        for (List<Integer> p: permutations) {
            for (String str: splittedStrings) {
                output.append(permuteOne(str, p));
            }

            List<String> out = Utility.splitStrings(output.toString(), blockSize);
            uniquePermutations.addAll(out);
        }

        return new ArrayList<>(uniquePermutations);
    }

    //rearrange the characters of the input block into the order defined by the key
    //uses the output from getPermutations to do one permutation.
    private static String permuteOne (String block, List<Integer> key) {
        StringBuilder output = new StringBuilder();

        for (Integer k: key) {
            output.append(block.charAt(k));
        }

        return output.toString();
    }

    //gets all possible permutations of the key
    //this is used in the main permute function as an easier way to manage each character in a string.
    private static List<List<Integer>> getPermutations(Integer[] key) {
        //use lists because they accept inserting stuff in the middle, unlike a normal array.
        List<List<Integer>> permutations = new ArrayList<>();

        // Add an empty list so that the middle for loop runs
        permutations.add(new ArrayList<>());

        for ( int i = 0; i < key.length; ++i) {
            // create a temporary container to hold the new permutations
            // while iterating over the old ones
            List<List<Integer>> current = new ArrayList<>();

            for ( List<Integer> permutation : permutations ) {
                for ( int j = 0, n = permutation.size() + 1; j < n; ++j ) {
                    List<Integer> temp = new ArrayList<>(permutation);
                    temp.add(j, key[i]);
                    current.add(temp);
                }
            }
            permutations = new ArrayList<>(current);
        }

        return permutations;
    }
}
