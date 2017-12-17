package modules;

import java.io.*;

public class ShiftCipher {
    //to prevent instantiation
    private ShiftCipher() {}

    //encrypt / decrypt
    //fIn = inputFile, fOut = outputFile, offset = shift
    //for offset, we can use the normal shift value (x) if encrypting, and use '26 - x' for decrypting
    public static void shift (String fIn, String fOut, Integer offset) {
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
