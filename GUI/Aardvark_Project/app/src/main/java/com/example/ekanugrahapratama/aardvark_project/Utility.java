package com.example.ekanugrahapratama.aardvark_project;

import java.util.Scanner;

public final class Utility {
    private static Scanner sc = new Scanner(System.in);

    //to prevent instantiation
    private Utility(){}

    public static String getString (String prompt) {
        System.out.print(prompt + ": ");

        String o = sc.nextLine();
        sc.close();
        return o;
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

        sc.close();
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
    public static Integer menuOption (String prompt, String[] options, String menuName) {
        line ('~', 30, menuName);
        for (int i = 1; i <= options.length; ++i) {
            System.out.println(i + ") " + options[i - 1]);
        }

        sc.close();
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
}
