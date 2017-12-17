package modules;

import com.Utility;
import java.util.*;

public class FrequencyAnalysis {
    public static void getFrequency (String data, Integer sequenceLength, Integer maxEntries ) {
        String checkSequence, currentSequence;
        Set<String> sequenceSet = new HashSet<>();

        //find all possible sequences
        for (int i = 0; i < data.length(); ++i) {
            //make sure it doesn't go out of bounds.
            if (i + sequenceLength >= data.length())
                break;

            checkSequence = data.substring(i, i + sequenceLength);
            sequenceSet.add(checkSequence);
        }

        List<String> sequences = new ArrayList<>(sequenceSet);
        Integer[] seqCounter = new Integer[sequences.size()];
        Arrays.fill(seqCounter, 0);

        for (int j = 0; j < data.length(); ++j) {
            if (j + sequenceLength >= data.length())
                break;

            currentSequence = data.substring(j, j + sequenceLength);

            if (sequences.contains(currentSequence)) {
                int index = sequences.indexOf(currentSequence);
                ++seqCounter[index];
            }
        }

        ArrayList<Integer> sequenceCounter = new ArrayList<>(Arrays.asList(seqCounter));

        if (maxEntries == 0 || maxEntries > sequenceCounter.size()) {
            maxEntries = sequenceCounter.size();
        }

        for (int i = 0; i < maxEntries; ++i) {
            int highest = Utility.getHighestIndex(sequenceCounter);
            String output = sequences.get(highest) + ": " + sequenceCounter.get(highest);

            sequenceCounter.remove(highest);
            sequences.remove(highest);

            System.out.println(output);
        }
    }
}
