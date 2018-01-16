package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

import java.io.*;
import java.util.*;

public class Utility {
    private static Utility thisInstance = new Utility();
    public static Utility getInstance() {
        return thisInstance;
    }
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private Utility() {}

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
        StringBuilder out = new StringBuilder();

        for (Character c: in.toCharArray()) {
            temp.add(c);
        }

        for (Character c: temp) {
            out.append(c);
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
}
