package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

import java.util.ArrayList;
import java.util.List;

public class PermuteString {
    public String permute (String data, Integer blockSize) {
        StringBuilder output = new StringBuilder();
        ArrayList<String> splittedStrings;
        Integer[] key = new Integer[blockSize];

        String returnValue = new String();

        //populate the "key"
        for (int i = 0; i < blockSize; ++i) { key[i] = i; }

        splittedStrings = splitIntoStrings(data, blockSize);

        List<List <Integer>> permutations = getPermutations(key);

        for (List<Integer> p: permutations) {
            for (String str: splittedStrings) {
                output.append(permuteOne(str, p));
            }

            ArrayList<String> out = splitIntoStrings(output.toString(), blockSize);

            for(int i = 0; i < out.size(); i++)
                returnValue += (out.get(i));
        }

        return returnValue;
    }

    private static ArrayList<String> splitIntoStrings (String data, Integer blockSize) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> output = new ArrayList<>();
        int count = 0;

        data+="\n\n"; /**THIS LINE SPLITS EACH PERMUTATION*/

        for (Character c: data.toCharArray()) {
            sb.append(c);

            ++count;
            if (count == blockSize) {
                count = 0;
                output.add(sb.toString());

                sb = new StringBuilder();
            }
        }

        if (!sb.toString().equals("")) {
            while ( (sb.toString().length() % blockSize) != 0 ) {
                sb.append(" ");
            }

            output.add(sb.toString());
        }

        return output;
    }

    private static String permuteOne (String block, List<Integer> key) {
        StringBuilder output = new StringBuilder();

        for (Integer k: key) {
            output.append(block.charAt(k));
        }

        return output.toString();
    }

    //for permuting the key. probably need to move it to the appropriate function.
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
