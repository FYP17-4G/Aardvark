import com.*;
import modules.*;

import java.util.ArrayList;
import java.util.List;

public class Krypto {
    private static String ORIGINAL_TEXT;
    private static String MODIFIED_TEXT;
    private static Integer TEXT_COUNT;
    private static Utility util = Utility.getInstance();

    public static void main(String[] args) {
        Cipher cipher = new TranspositionPeriodic();
        String key = "Never";
//        init("Jack and Jill ran up the hill");
        init (Utility.readFile("res/abc.txt"));
        
//        System.out.println("analysis.getData() = " + analysis.getData());
//        System.out.println(analysis.getFREQUENCY());
//        System.out.println(analysis.getSEQUENCE());
    }

    private static void init (String originalInput) {
        ORIGINAL_TEXT = originalInput;
        MODIFIED_TEXT = util.processText(ORIGINAL_TEXT);
        TEXT_COUNT = MODIFIED_TEXT.length();
    }

    private static String displayOriginalString() {
        return ORIGINAL_TEXT;
    }

    private static String displayModifiedString () {
        StringBuilder output = new StringBuilder();

        int counter = 0;
        for (Character c: ORIGINAL_TEXT.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                output.append(' ');
            } else if (Character.isUpperCase(c)) {
                output.append(Character.toUpperCase(MODIFIED_TEXT.charAt(counter)));
                ++counter;
            } else if (!Character.isAlphabetic(c)) {
                output.append(c);
            } else {
                output.append(MODIFIED_TEXT.charAt(counter));
                ++counter;
            }
        }

        return output.toString();
    }

    private static String displayModifiedString (int blockSize, int blocksPerLine) {
        int charCounter = 0, blockCounter = 0;
        StringBuilder sb = new StringBuilder();

        for (Character c: MODIFIED_TEXT.toCharArray()) {
            sb.append(c);
            ++charCounter;
            if (charCounter == blockSize) {
                sb.append(" ");
                ++blockCounter;
                charCounter = 0;
            }

            if (blockCounter == blocksPerLine) {
                sb.append('\n');
                blockCounter = 0;
            }
        }

        return sb.toString();
    }
}
