package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

import java.util.ArrayList;
import java.util.List;

public class RectangularTransposition
{
    private RectangularTransposition() {}


    //decryption, assume read left to right, top to bottom.
    public static String encrypt(String data, Integer columns) {
        List<List <Character> > output = new ArrayList<>();
        data = Utility.pad(data, columns);

        int colCounter = 0;
        List<Character> temp = new ArrayList<>();
        for (Character c: data.toCharArray()) {
            temp.add(c);
            ++colCounter;

            if (colCounter >= columns) {
                colCounter = 0;
                output.add(temp);
                temp = new ArrayList<>();
            }
        }

        output = flip(output);
        StringBuilder sb = new StringBuilder();

        for (List<Character> row: output) {
            for (Character c: row) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String decrypt (String data, Integer columns) {
        List<List <Character> > output = new ArrayList<>();
        int cols = data.length() / columns;

        int colCounter = 0;
        List<Character> temp = new ArrayList<>();
        for (Character c: data.toCharArray()) {
            temp.add(c);
            ++colCounter;

            if (colCounter == cols) {
                colCounter = 0;
                output.add(temp);
                temp = new ArrayList<>();
            }
        }

        output = flip(output);
        StringBuilder sb = new StringBuilder();

        for (List<Character> row: output) {
            for (Character c: row) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    private static List<List<Character>> flip(List<List<Character>> input) {
        List<List<Character>> output = new ArrayList<>();
        List<Character> newRow;

        int maxCols = input.get(0).size();

        for (int i = 0; i < maxCols; ++i) {
            newRow = new ArrayList<>();

            for (List<Character> row : input) {
                newRow.add(row.get(i));
            }

            output.add(newRow);
        }

        return output;
    }
}
