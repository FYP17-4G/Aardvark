package modules;

import com.Utility;

import java.util.ArrayList;
import java.util.List;

public class RectangularTransposition
{
    private RectangularTransposition() {}

    //decryption, assume read left to right, top to bottom.
    public static List<List<Character>> encrypt(String data, Integer columns) {
        List<List <Character> > output = new ArrayList<>();
        data = Utility.pad(data, columns);

        int colCounter = 0;
        List<Character> temp = new ArrayList<>();
        for (Character c: data.toCharArray()) {
            temp.add(c);
            ++colCounter;

            if (colCounter >= columns) {
                colCounter = 0;
                output.add(temp);
                temp = new ArrayList<>();
            }
        }

        return flip(output);
    }

    private static List<List<Character>> flip(List<List<Character>> input) {
        List<List<Character>> output = new ArrayList<>();
        List<Character> newRow;

        int maxCols = input.get(0).size();

        for (int i = 0; i < maxCols; ++i) {
            newRow = new ArrayList<>();

            for (List<Character> row : input) {
                newRow.add(row.get(i));
            }

            output.add(newRow);
        }

        return output;
    }
}
