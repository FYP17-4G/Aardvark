import com.*;
import modules.*;
import java.util.*;

public class Krypto {
    private static String ORIGINAL_TEXT;
    private static String MODIFIED_TEXT;

    //TEXT_COUNT -> Original number of characters in the string
    //used for the Utility.unpad() function.
    private static Integer TEXT_COUNT;

    public static void main(String[] args) {
        Cipher cipher = new Kamasutra();
//        init("Jack and Jill ran up the hill");
        init (Utility.readFile("res/abc.txt"));

	    String key = Utility.generateUniqueKey(26);

	    try {
		    MODIFIED_TEXT = cipher.encrypt(MODIFIED_TEXT, key);
	    } catch (InvalidKeyException e) {
		    e.printStackTrace();
	    }

	    System.out.println(displayModifiedString());
//        System.out.println(displayOriginalString());

        try {
            MODIFIED_TEXT = cipher.decrypt(MODIFIED_TEXT, key);
        } catch (InvalidKeyException e) {
            System.out.println(e.getMessage());
        }

	    System.out.println(displayModifiedString());

        List<Double> modIC = CalculateIC.getIC(MODIFIED_TEXT);
	    System.out.println("modIC = " + modIC);
    }

    private static void init (String originalInput) {
        ORIGINAL_TEXT = originalInput;
        MODIFIED_TEXT = Utility.processText(ORIGINAL_TEXT);
        TEXT_COUNT = MODIFIED_TEXT.length(); //unpad
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
