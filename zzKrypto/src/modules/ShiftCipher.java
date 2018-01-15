package modules;

public class ShiftCipher {
    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    //to prevent instantiation
    private ShiftCipher() {}

    public static String encrypt(String data, Integer offset) {
        StringBuilder sb = new StringBuilder();

        int output;
        for (Character c: data.toCharArray()) {
            output = c - 'a' + offset;
            output %= 26;

            sb.append(alphabet.charAt(output));
        }

        return sb.toString();
    }

    public static String decrypt (String data, Integer offset) {
        StringBuilder sb = new StringBuilder();

        int output;
        for (Character c: data.toCharArray()) {
            output = c - 'a' - offset;
            if (output < 0) {
                output += 26;
            }
            output %= 26;

            sb.append(alphabet.charAt(output));
        }

        return sb.toString();
    }
}
