package com;

import java.io.*;
import java.util.*;

public class Utility {
    private static Utility thisInstance = new Utility();
    public static Utility getInstance() {
        return thisInstance;
    }
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private Utility() {}

    public static String generateKey(int length) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            out.append(getRandAlphabet());
        }

        return out.toString();
    }

    public static String generateUniqueKey(int length) {
    	if (length > 26) length = 26;
    	
        StringBuilder out = new StringBuilder();
        int counter = 0;
        Character c;

        do {
            c = getRandAlphabet();

            if (!out.toString().contains(c.toString())) {
                out.append(c);
                ++counter;
            }

        } while (counter < length);

        return out.toString();
    }

    public static String processText(String original) {
        //remove the numbers, punctuation, etc from the original text
        return original.toLowerCase().replaceAll("[^A-Za-z]", "");
    }

    public static String readFile (String filename) {
        File infile = new File(filename);
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        int c;
        try {
            reader = new BufferedReader(new FileReader(infile));
            while ( (c = reader.read()) != -1 ) {
                sb.append((char)c);
            }
        } catch (IOException io) {
            System.out.println("name = " + filename);
            System.out.println("Not found!");
            System.exit(-1);
        }

        return sb.toString();
    }

    public static String removeDuplicates (String in) {
        Set<Character> temp = new HashSet<>();
        List<Character> revTemp = new ArrayList<>();
        StringBuilder out = new StringBuilder();

        in = in.toLowerCase();

        for (Character c: in.toCharArray()) {
            if (!revTemp.contains(c)) {
                revTemp.add(c);
                out.append(c);
            }
        }

        return out.toString();
    }

    public static Character getRandAlphabet () {
        Integer random = (int) (Math.random() * (alphabet.length()));
        return alphabet.charAt(random);
    }

    public static String pad (String plaintext, int keylength) {
        StringBuilder sb = new StringBuilder(plaintext);

        while ((sb.length() % keylength) != 0)
            sb.append(Utility.getRandAlphabet());

        return sb.toString();
    }

    public static String unpad (String paddedText, int originalLength) {
        return paddedText.substring(0, originalLength);
    }

    public static List<String> splitStrings (String input, int keylength) {
        StringBuilder temp = new StringBuilder();
        List<String> out = new ArrayList<>();
        int currentCount = 0;

        for (Character c: input.toCharArray()) {
            temp.append(c);
            ++currentCount;

            if (currentCount >= keylength) {
                out.add(temp.toString());
                temp = new StringBuilder();
                currentCount = 0;
            }
        }
        return out;
    }

    public static Integer getHighestIndex (ArrayList<Integer> input) {
        int maxVal = 0, maxInd = 0;

        int counter = 0;
        for (Integer in: input) {
            if (maxVal < in) {
                maxVal = in;
                maxInd = counter;
            }

            ++counter;
        }

        return maxInd;
    }

    public static String getVigenereSquare() {
    	StringBuilder out = new StringBuilder();
//    	for (int i = 0; i < 26; ++i) {
//    		out.append(i % 10);
//	    }
//
//	    out.append("\n");

    	for (int row = 0; row < 26; ++row) {
    		for (int col = 0; col < 26; ++col) {
    			int index = (row + col) % 26;
			    out.append(alphabet.charAt(index));
		    }
		    out.append("\n");
	    }

    	return out.toString();
    }
}
