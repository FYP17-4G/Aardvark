/*
 * ---------
 * Module Name: FrequencyAnalysis.java
 * Displays the <maxEntries> most frequently occurring sequences of length <sequenceLength>
 * f [<sequenceLength> <n>] in Krypto.exe
 * ---------
 * @params: data            -> input
 *          sequenceLength  -> length of sequence to check for
 *          maxEntries      -> max number of entries to display
 * Does not return anything; prints directly.
 * Can probably make it return something if necessary.
 * ---------
 */

package models.analytics;

import java.util.*;
public class FrequencyAnalysis {
    public static Analysis frequencyAnalysis (String data, Integer sequenceLength) {
        String checkSequence, currentSequence;
        Set<String> sequenceSet = new HashSet<>();
        Analysis analysis;

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

        analysis = new Analysis(data, sequences, sequenceCounter);
        return analysis;
    }
}
