package modules;

public class Substitute {
    public static String ch (Character oldC, Character newC, String mData, String oData) {
        StringBuilder sb = new StringBuilder();
        for (Character x: mData.toCharArray()) {
            if (x == oldC)
                sb.append(newC);
            else
                sb.append(x);
        }

        return sb.toString();
    }
}
