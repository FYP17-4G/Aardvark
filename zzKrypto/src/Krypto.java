import com.Utility;
import modules.CalculateIC;

import java.util.ArrayList;

public class Krypto {
    private static String ORIGINAL_DATA;
    private static String MODIFIED_DATA;
    private static Integer MAX_REVERTS = 10;
    private static ArrayList<String> ACTION_QUEUE = new ArrayList<>(10);

    // Do not directly modify ORIGINAL_DATA and MODIFIED_DATA.
    // Use only outputs from the various functions.
    // Initialize and set new ORIGINAL_DATA with the init function.
    // It will automatically set the MODIFIED_DATA as well.
    public static void main(String[] args) {
//        init (Utility.readFile("res/abc.txt"));
//        init ("What does this all mean?");
        init ("Asd");
        Utility.line('-', 30, "Original Text");
        displayOriginalString();

        displayModifiedString();
        Double blah = CalculateIC.getIC(MODIFIED_DATA);
        System.out.println("blah = " + blah);

//        ArrayList<String> otp = OneTimePad.encrypt(MODIFIED_DATA);
//        MODIFIED_DATA = otp.get(1);
//        displayModifiedString();
//        String pad = otp.get(0);
//        try {
//            MODIFIED_DATA = OneTimePad.decrypt(pad, MODIFIED_DATA);
//
//            displayModifiedString();
//        } catch (IOException io) {
//            System.out.println("io = " + io);
//        }
    }

    private static void init (String originalInput) {
        ORIGINAL_DATA = originalInput;

        originalInput = originalInput.toLowerCase();
        originalInput = originalInput.replaceAll("[^A-Za-z]", "");
        MODIFIED_DATA = originalInput;
    }

    //for all the display functions, we can just return a string if needed, instead of printing directly.
    private static void displayOriginalString() {
        System.out.println(ORIGINAL_DATA);
        System.out.println();
    }

    private static void displayModifiedString () {
        StringBuilder output = new StringBuilder();

        int counter = 0;
        for (Character c: ORIGINAL_DATA.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                output.append(' ');
            } else if (Character.isUpperCase(c)) {
                output.append(Character.toUpperCase(MODIFIED_DATA.charAt(counter)));
                ++counter;
            } else if (!Character.isAlphabetic(c)) {
                output.append(c);
            } else {
                output.append(MODIFIED_DATA.charAt(counter));
                ++counter;
            }
        }

        System.out.println(output.toString());
        System.out.println();
    }

    private static void displayModifiedString (int blockSize, int blocksPerLine) {
        int charCounter = 0, blockCounter = 0;

        for (Character c: MODIFIED_DATA.toCharArray()) {
            System.out.print(c);
            ++charCounter;
            if (charCounter == blockSize) {
                System.out.print(" ");
                ++blockCounter;
                charCounter = 0;
            }

            if (blockCounter == blocksPerLine) {
                System.out.println();
                blockCounter = 0;
            }
        }

        System.out.println();
    }

    //go back to the original data, discard all changes.
    private static void revert () {
        String originalInput = ORIGINAL_DATA;

        originalInput = originalInput.toLowerCase();
        originalInput = originalInput.replaceAll("[^A-Za-z]", "");
        MODIFIED_DATA = originalInput;
    }
}