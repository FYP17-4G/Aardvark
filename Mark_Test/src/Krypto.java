import mUtil.Utility;

public class Krypto {
    private static final Character[] OPTIONS = {'f', 'g','i', 'l', 'p', 'q', 's', 'S', 'T', 't', 'B', 'b', 'u', 'z', 'r', 'w'};
    public static void main(String[] args) {
        String response;
        do {
            response = displayMenu();
        } while (response.equals ("q"));
    }

    private static String displayMenu () {
        Boolean success = true;
        do {
            if (!success)
                System.out.println("Invalid option! Try again.");

            String response = Utility.getString("Enter function");
            String first = response.split(" ")[0];
            for (Character c: OPTIONS) {
                if (first.charAt(0) == c) {
                    success = true;
                    break;
                } else {
                    success = false;
                }
            }
        } while (!success);

        return response;
    }
}
