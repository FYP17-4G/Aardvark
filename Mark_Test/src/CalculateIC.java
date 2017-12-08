import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class CalculateIC {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Insufficient arguments.");
            System.out.println("Usage: CalculateIC <file> <period(?)>");
        } else {
            String fiName = args[0];
            Integer period;
            try {
                period = Integer.parseInt(args[1]);
            } catch (NumberFormatException ne) {
                period = 1;
            }

            ArrayList<StringBuilder> strings;
            String data = readFile(fiName);
            System.out.println("data = " + data);

            ArrayList<Double> ICs = getIC(period, data);

            int counter = 1;
            for (Double ic: ICs) {
                if (ICs.size() > 1)
                    System.out.println("IC " + counter++ + " = " + ic);
                else
                    System.out.println("IC " + counter++ + " = " + ic);
            }
        }
        

    }

    //get IC for a bunch of data, reading from every nth letter.
    //data entered here is the whole set of data. the function will split the string accordingly.
    private static ArrayList<Double> getIC (Integer n, String data) {
        ArrayList<StringBuilder> splitStrings = getEveryNthLetter(n, data);
        ArrayList<Double> IC = new ArrayList<>(n);

        for (StringBuilder sb: splitStrings) {
            IC.add(getIC(sb.toString()));
        }

        return IC;
    }

    //get IC for a particular string of data
    private static double getIC (String data) {
        int[] counts = new int[26];
        Arrays.fill(counts, 0);
        
        int index, totalChars = 0;
        double numer = 0.0, denom;
        data = data.toLowerCase();

        for (Character c: data.toLowerCase().toCharArray()) {
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


        Double IC = BigDecimal.valueOf( (numer / denom) ).setScale(3, RoundingMode.HALF_UP).doubleValue();

        return IC;
    }

    //compiles every nth letter into an ArrayList of strings, according to n.
    //data entered here is the whole set of data. the function will split the string accordingly.
    //ignores punctuation and spaces.
    //e.g. getEveryNthLetter (4, hello world!) results in:
    //1. hol    2. ewd
    //3. lo     4. lr
    private static ArrayList<StringBuilder> getEveryNthLetter(Integer n, String data) {
        data = data.replaceAll("[^A-Za-z]", "");
        ArrayList<StringBuilder> split = new ArrayList<>(n);
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

    //reads a file, returns the contents in a single string.
    //apparently a String object can hold 4 billion characters, maybe we should test that.
    private static String readFile (String name) {
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

        return sb.toString();
    }
}
