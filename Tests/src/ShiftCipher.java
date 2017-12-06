import java.io.*;

public class ShiftCipher {
    public static void main(String[] args) {

        //we can do error checking when we actually implement it in the Android code
        switch (args[0]) {
            case "-e": {
                // like this for example, if the user enters something not an integer, or out of range
                shift(args[1], args[2], Integer.parseInt(args[3]));
                break;
            }
            case "-d": {
                //same for here
                shift(args[1], args[2], (26 - (Integer.parseInt(args[3])) ));
                break;
            }
            default: {
                System.out.println("Invalid option!");
                break;
            }
        }

//        int shift = 5;
//        Character input = 'Z', output;
//        output = (char)(input + shift);
//
//        if (Character.isAlphabetic(input)) {
//            if (Character.isUpperCase(input)) {
//                if (output > 'Z')
//                    output = (char) (input - (26 - shift));
//            } else {
//                if (output > 'z')
//                    output = (char) (input - (26 - shift));
//            }
//        }
//
//
//        System.out.println("input = " + input);
//        System.out.println("output = " + output);
    }

    //encrypt / decrypt
    public static void shift (String fIn, String fOut,  Integer offset) {
        File input = new File(fIn);
        File output = new File(fOut);
        BufferedReader reader;
        BufferedWriter writer;

        try {
            reader = new BufferedReader(new FileReader(input));
            writer = new BufferedWriter(new FileWriter(output));

            int t;
            while ((t = reader.read()) != -1) {
                Character in = (char)t, out;

                if (Character.isAlphabetic(in)) {
                    out = (char) (in + offset);
                    if (Character.isUpperCase(in)) {
                        if (out > 'Z')
                            out = (char) (in - (26 - offset));
                    } else {
                        if (out > 'z')
                            out = (char) (in - (26 - offset));
                    }
                } else {
                    //don't do anything if the input is not alphabetic
                    out = in;
                }

                writer.write(out);
            }

            reader.close();
            writer.close();
        } catch (IOException io) {
            System.out.println("Couldn't read " + input.getAbsolutePath());
        }
    }
}
