package com;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Utility {
    private static final Scanner sc = new Scanner(System.in);

    //to prevent instantiation
    private Utility(){}

    public static String getString (String prompt) {
        System.out.print(prompt + ": ");

        return sc.nextLine();
    }

    public static Integer getInt(String prompt, int max) {
        Integer input;
        Boolean success = true;
        do {
            if (!success)
                System.out.println("Invalid response. Try again.");

            success = true;

            try {
                System.out.print(prompt);
                input = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ne) {
                success = false;
                input = -1;
            }

            if (input > max || input < 0)
                success = false;
        } while (!success);

        return input;
    }

    public static Double getDouble(String prompt) {
        String t;
        Double d = 0.0;
        Boolean invalid = false;

        do {
            if (invalid) {
                System.out.println("Invalid double! Try again.");
                invalid = false;
            }

            System.out.println(prompt + ": ");
            t = sc.nextLine();
            try {
                d = Double.parseDouble(t);
            } catch (NumberFormatException n) {
                invalid = true;
            }
        } while (invalid);

        sc.close();
        return d;
    }

    //display a menu, returns a userChoice
    public static Integer simpleMenu (String prompt, String[] options, String menuName) {
        line ('~', 30, menuName);
        for (int i = 1; i <= options.length; ++i) {
            System.out.println(i + ") " + options[i - 1]);
        }

        return getInt("Select an option: ", options.length);
    }

    //draw a simple divider
    public static void line (Character c, int len) {
        for (int i = 0; i < len; ++i)
            System.out.print(c);

        System.out.println();
    }

    //draw a divider with a title
    public static void line (Character c, int len, String title) {
        if (len > title.length()) {
            len = len - title.length();

            for (int i = 0; i < len; ++i) {
                if (i == len / 2) {
                    System.out.print(title);
                } else { System.out.print(c); }
            }
        } else {
            System.out.println(title);
        }

        System.out.println();
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

    public static String processData (String data) {
        return data.replace("[^A-Za-z]", "");
    }

    //reads a file, returns the contents in a single string.
    //apparently a String object can hold 4 billion characters, maybe we should test that.
    public static String readFile (String name) {
        File infile = new File(name);
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        int c;
        try {
            reader = new BufferedReader(new FileReader(infile));
            while ( (c = reader.read()) != -1 ) {
                sb.append((char)c);
            }
        } catch (IOException io) {
            System.out.println("name = " + name);
            System.out.println("Not found!");
            System.exit(-1);
        }

//        System.out.println("Successfully read " + name + "!");
//        System.out.println("Data = " + sb.toString());
        return sb.toString();
    }

    //writes to the specified file.
    public static void writeFile (String name, String data) throws IOException {
        File outfile = new File(name);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));

        writer.write(data);
        System.out.println(name + " successfully created!");
    }

    public static Integer getIndex (Character ch) {
        return Character.toLowerCase(ch) - 'a';
    }

    public static Character getRandAlphabet () {
        String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz";
        Integer random = (int) (Math.random() * (ALPHANUMERIC.length()));
        return ALPHANUMERIC.charAt(random);
    }

    public static String pad (String original, Integer keyLength) {
        StringBuilder padded = new StringBuilder(original);

        while ( (padded.length() % keyLength) != 0) {
            padded.append(getRandAlphabet());
        }

        return padded.toString();
    }
}
