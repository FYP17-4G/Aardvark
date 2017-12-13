import mUtil.Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Krypto {
    private static final Character[] OPTIONS = {'f', 'g','i', 'l', 'p', 'q',
            's', 'S', 'T', 't', 'B', 'b', 'u', 'z', 'r', 'w'};

    private static String ORIGINAL_DATA = readFile("plain.txt");
    private static String MODIFIED_DATA = "";
    public static void main(String[] args) {
        String response;
        String[] params;

        MODIFIED_DATA = ORIGINAL_DATA;
        do {
            response = displayMenu();
            params = response.split(" ");

            switch (params[0]) {
                case "i": {
                    indexOfCoincidence(params);
                    break;
                }

                case "f": {
                    frequencyAnalysis(params);
                    break;
                }
            }
        } while (!response.equals ("q"));
    }

    //print the required output for `i <n>`
    private static void indexOfCoincidence (String[] params) {
        int period;
        try {
            if (params.length < 2) throw new NumberFormatException();
            period = Integer.parseInt(params[1]);
        } catch (NumberFormatException ne) {
            period = 1;
        }

        ArrayList<Double> ICs = getIC(period, MODIFIED_DATA);

        int counter = 1;
        for (Double ic: ICs) {
            if (ICs.size() > 1)
                System.out.println("IC = " + ic);
            else
                System.out.println("IC " + (counter++) + " = " + ic);
        }
    }

    //print the required output for `f <seqLen> <n>`
    private static void frequencyAnalysis (String[] params) {
        //commonly you'll search for trigrams, so default is 3.
        Integer sequenceLength = 3, maxEntries = 0;
        String data = MODIFIED_DATA.toLowerCase().replaceAll("[^A-Za-z]", "");

        if (params.length > 1) {
            try {
                sequenceLength = Integer.parseInt(params[1]);

                //if maxEntries exists
                if (params.length > 2) {
                    try {
                        maxEntries = Integer.parseInt(params[2]);
                    } catch (NumberFormatException _maxEntries_) {
                        //if 0, then show all
                        maxEntries = 0;
                    }
                }
            } catch (NumberFormatException _sequenceLength_) {
                sequenceLength = 3;
            }
        }

        String checkSequence, currentSequence;
        Set<String> sequenceSet = new HashSet<>();

        //find all possible sequences
        for (int i = 0; i < data.length(); ++i) {
            //make sure it doesn't go out of bounds.
            if (i + sequenceLength >= data.length())
                break;

            checkSequence = data.substring(i, i + sequenceLength);
            sequenceSet.add(checkSequence);
        }

        List<String> sequences = new ArrayList<>(sequenceSet);
        Integer[] seqCounter = new Integer[sequences.size()];
        Arrays.fill(seqCounter, 0);

        for (int j = 0; j < data.length(); ++j) {
            if (j + sequenceLength >= data.length())
                break;

            currentSequence = data.substring(j, j + sequenceLength);

            if (sequences.contains(currentSequence)) {
                int index = sequences.indexOf(currentSequence);
                ++seqCounter[index];
            }
        }

        ArrayList<Integer> sequenceCounter = new ArrayList<>(Arrays.asList(seqCounter));

        if (maxEntries == 0 || maxEntries > sequenceCounter.size()) {
            maxEntries = sequenceCounter.size();
        }

        for (int i = 0; i < maxEntries; ++i) {
            int highest = getHighestIndex(sequenceCounter);
            String output = sequences.get(highest) + ": " + sequenceCounter.get(highest);

            sequenceCounter.remove(highest);
            sequences.remove(highest);

            System.out.println(output);
        }
    }

    private static Integer getHighestIndex (ArrayList<Integer> input) {
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

    //get IC for a bunch of data, reading from every nth letter.
    //data entered here is the whole set of data. the function will split the string accordingly.
    private static ArrayList<Double> getIC (Integer n, String data) {
        ArrayList<StringBuilder> splitStrings = getEveryNthLetter(n, data);
        ArrayList<Double> IC = new ArrayList<>(n);

        for (StringBuilder sb: splitStrings) {
            IC.add(getSingleIC(sb.toString()));
        }

        return IC;
    }

    //get IC for a particular string of data
    private static double getSingleIC (String data) {
        int[] counts = new int[26];
        Arrays.fill(counts, 0);

        int index, totalChars = 0;
        double numer = 0.0, denom;
        data = data.toLowerCase();

        for (Character c: data.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                index = c - 'a';
                ++counts[index];
                ++totalChars;
            }
        }

        for (Integer n: counts) {
            numer += (n * (n - 1));
        }

        denom = totalChars * (totalChars - 1);
        
        return BigDecimal.valueOf( (numer / denom) ).setScale(3, RoundingMode.HALF_UP).doubleValue();
    }

    //compiles every nth letter into an ArrayList of strings, according to n.
    //data entered here is the whole set of data. the function will split the string accordingly.
    //ignores punctuation and spaces.
    //e.g. getEveryNthLetter (4, hello world!) results in:
    //1. hol    2. ewd
    //3. lo     4. lr
    private static ArrayList<StringBuilder> getEveryNthLetter(Integer n, String data) {
        data = data.replaceAll("[^A-Za-z]", "");
        ArrayList<StringBuilder> split = new ArrayList<>();
        int counter = 0;

        for (int i = 0; i < n; ++i) {
            split.add(new StringBuilder());
        }

        for (Character c: data.toLowerCase().toCharArray()) {
            split.get(counter).append(c);
            ++counter;

            if (counter >= n)
                counter = 0;
        }

        return split;
    }

    private static String readFile (String name) {
        File infile = new File(name);
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(infile));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (IOException io) {
            System.out.println("Cannot find " + name + "!");
        }

        return sb.toString();
    }

    private static String displayMenu () {
        Boolean success = true;
        String response;
        do {
            if (!success)
                System.out.println("Invalid option! Try again.");

            response = Utility.getString("Enter function");
            String first = response.split(" ")[0];
            for (Character c: OPTIONS) {
                if (first.charAt(0) == c) {
                    success = true;
                    break;
                } else {
                    success = false;
                }
            }
        } while (!success);

        return response;
    }
}
