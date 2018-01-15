package com;

import java.io.*;
import java.util.*;

public class Utility {
    private static Utility ourInstance = new Utility();
    public static Utility getInstance() {
        return ourInstance;
    }
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private Utility() {}

    public String processText(String original) {
        //remove the numbers, punctuation, etc from the original text
        return original.toLowerCase().replaceAll("[^A-Za-z]", "");
    }

    public String readFile (String filename) {
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
}
