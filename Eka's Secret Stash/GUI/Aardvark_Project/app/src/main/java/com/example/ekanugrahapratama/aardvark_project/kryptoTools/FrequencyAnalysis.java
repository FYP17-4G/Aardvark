package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;

public class FrequencyAnalysis {
    private static ArrayList < mPair <Character, Integer> > frequencyTable = new ArrayList<>(26);
    
    public static void main(String[] args) {
        String filename = Utility.getString("Filename");
        File in = new File(filename);
        BufferedReader reader;
        Integer max = 7;

        ArrayList <mPair <Character, Integer> > testFrequency;

        //initialize frequencyTable (ugh)
        for (int i = 0; i < 26; ++i) {
            frequencyTable.add(new mPair<>((char)('a' + i), 0));
        }

        try {
            reader = new BufferedReader(new FileReader(in));

            int t;
            while ( (t = reader.read()) != -1 ) {
                Character c = (char)t;

                c = Character.toLowerCase(c);
                if (Character.isAlphabetic(c)) {
//                    System.out.println(c + " = " + (c - 'a'));
                    ++frequencyTable.get((c - 'a')).second;
                }
            }

            reader.close();
        } catch (Exception f) {
            System.out.println("Could not find " + filename);
            System.out.println("f = " + f);
        }

        //test the functions
        System.out.println("Most Frequent Character    = " + getMostFrequentCharacter(frequencyTable));

        testFrequency = getMostFrequentCharacter(frequencyTable, max);
        System.out.print (max + " most frequent characters = ");
        for (mPair x: testFrequency) {
            System.out.print(x.first + " ");
        }
        System.out.println();

        System.out.print ("Ascending  -> ");
        getFrequency("asc", frequencyTable);
        System.out.print ("Descending -> ");
        getFrequency("des", frequencyTable);

//        //check the original
//        for (mPair <Character, Integer> x: frequencyTable)
//            System.out.print(x.first + " ");
//
//        System.out.println();
    }

    //des or asc, defaults to des
    private static void getFrequency (String direction, ArrayList <mPair <Character, Integer> > originalTable) {
        ArrayList <mPair <Character, Integer> > tempTable = new ArrayList<>(originalTable);
        Comparator<mPair <Character, Integer> > comparator = Comparator.comparing(x -> x.second);

        if (direction.toLowerCase().equals("asc")) {
            tempTable.sort(comparator);
        } else {
            tempTable.sort(comparator.reversed());
        }

        for (mPair <Character, Integer> ch: tempTable) {
            System.out.print(ch.first + " ");
        }

        //can return
        System.out.println();
    }
    private static Character getMostFrequentCharacter(ArrayList <mPair <Character, Integer> > original) {
        ArrayList <mPair <Character, Integer> > tempTable = new ArrayList<>(original);
        Comparator<mPair <Character, Integer> > comparator = Comparator.comparing(x -> x.second);
        tempTable.sort(comparator.reversed());

        return tempTable.get(0).first;
    }

    //returns an ArrayList of the <max> most frequent characters
    private static ArrayList<mPair <Character, Integer> >
    getMostFrequentCharacter (ArrayList <mPair <Character, Integer> > original, Integer max) {
        if (max > original.size())
            max = original.size();

        ArrayList<mPair <Character, Integer> > ret = new ArrayList<>(max);

        ArrayList <mPair <Character, Integer> > tempTable = new ArrayList<>(original);
        Comparator<mPair <Character, Integer> > comparator = Comparator.comparing(x -> x.second);
        tempTable.sort(comparator.reversed());

        for (int i = 0; i < max; ++i) {
            ret.add(tempTable.get(i));
        }

        return ret;
    }
}
