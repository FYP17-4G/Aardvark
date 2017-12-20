import com.Utility;
import modules.RectangularTransposition;

import java.util.ArrayList;
import java.util.List;

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
        init (Utility.readFile("res/plain.txt"));
//        init ("What does this all mean?");
        Utility.line('-', 30, "Original Text");
        displayOriginalString();

        MODIFIED_DATA = RectangularTransposition.encrypt(MODIFIED_DATA, 3);
        displayModifiedString();

        MODIFIED_DATA = RectangularTransposition.decrypt(MODIFIED_DATA, 3);
        displayModifiedString();
    }

    private static void init (String originalInput) {
        ORIGINAL_DATA = originalInput;

        originalInput = originalInput.toLowerCase();
        originalInput = originalInput.replaceAll("[^A-Za-z]", "");
        MODIFIED_DATA = originalInput;
    }

    private static void substitute (Character oldChar, Character newChar) {
        char[] original = ORIGINAL_DATA.toLowerCase()
                .replaceAll("[^A-Za-z]", "").toCharArray();
        char[] modified = MODIFIED_DATA.toCharArray();

        for (int i = 0; i < modified.length; ++i) {
            if (oldChar == modified[i] && modified[i] == original[i]) {
                modified[i] = newChar;
            }
        }

        MODIFIED_DATA = new String (modified);
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

    //for permuting the key. probably need to move it to the appropriate function.
    private static List<List<Integer>> permute(Integer[] key) {
        //use lists because they accept inserting stuff in the middle, unlike a normal array.
        List<List<Integer>> permutations = new ArrayList<>();

        // Add an empty list so that the middle for loop runs
        permutations.add(new ArrayList<>());

        for ( int i = 0; i < key.length; ++i) {
            // create a temporary container to hold the new permutations
            // while iterating over the old ones
            List<List<Integer>> current = new ArrayList<>();

            for ( List<Integer> permutation : permutations ) {
                for ( int j = 0, n = permutation.size() + 1; j < n; ++j ) {
                    List<Integer> temp = new ArrayList<>(permutation);
                    temp.add(j, key[i]);
                    current.add(temp);
                }
            }
            permutations = new ArrayList<>(current);
        }

        return permutations;
    }
}
