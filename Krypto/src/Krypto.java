import com.Utility;
import modules.ColumnarTransposition;

import java.util.ArrayList;
import java.util.List;

public class Krypto {
    private static String ORIGINAL_DATA;
    private static String MODIFIED_DATA;
    private static Integer MAX_REVERTS = 10;
    private static ArrayList<String> ACTION_QUEUE = new ArrayList<>(10);


    public static void main(String[] args) {
        ORIGINAL_DATA = Utility.readFile("res/abc.txt");
        MODIFIED_DATA = Utility.processData(ORIGINAL_DATA);
        StringBuilder ciphertext = new StringBuilder();
        StringBuilder plaintext  = new StringBuilder();

        String data = MODIFIED_DATA.toLowerCase().replaceAll("[^a-zA-Z]", "");

        List<List<Character>> encryption = new ArrayList<>();
        String key = "party";
        encryption = ColumnarTransposition.encrypt(data, key,  encryption);
        for (List<Character> row: encryption) {
            System.out.println(row);
            for (Character c: row)
                ciphertext.append(c);
        }

        Utility.line('~', 30);

        List<List<Character>> decryption = new ArrayList<>();
        decryption = ColumnarTransposition.decrypt(ciphertext.toString(), key,  decryption);
        for (List<Character> row: decryption) {
            System.out.println(row);
            for (Character c: row)
                plaintext.append(c);
        }

        System.out.println("ciphertext = " + ciphertext.toString());
        System.out.println("decrypted  = " + plaintext.toString());
    }
}
