import com.*;
import modules.*;

public class Krypto {
    private static String ORIGINAL_TEXT;
    private static String MODIFIED_TEXT;
    private static Integer TEXT_COUNT;
    private static Utility util = Utility.getInstance();

    public static void main(String[] args) {
        Cipher cipher = new TranspositionPeriodic();
        String key = "Never";
        init("Jack and Jill ran up the hill");

        System.out.println("Original: " + displayOriginalString());

        try {
            MODIFIED_TEXT = cipher.encrypt(MODIFIED_TEXT, key);
        } catch (InvalidKeyException ike) {
            System.err.println(ike.getMessage());
            System.exit (-1);
        }

//        System.out.println(displayModifiedString() + "\n");
        System.out.println("Encrypted: " + displayModifiedString(5, 10));

        try {
            //Utility.unpad is important, gets rid of the random padded characters in the end.
            MODIFIED_TEXT = Utility.unpad(cipher.decrypt(MODIFIED_TEXT, key), TEXT_COUNT);
        } catch (InvalidKeyException ike) {
            System.err.println(ike.getMessage());
            System.exit (-1);
        }

//        System.out.println(displayModifiedString() + "\n");
        System.out.println("Decrypted: " + displayModifiedString(5, 10));
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
