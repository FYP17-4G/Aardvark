

package models.analytics;

import java.util.*;

public class Analysis {
    private String DATA;
    private List<String> SEQUENCE;
    private List<Integer> FREQUENCY;
    private TreeMap<String, Integer> SORTEDLIST;

    public Analysis () {}
    public Analysis (String newData, List<String> newSequence, List<Integer> newFrequency) {
        setDATA(newData);
        setSEQUENCE(newSequence);
        setFREQUENCY(newFrequency);

        SORTEDLIST = new TreeMap<>();
        for (int i = 0; i < FREQUENCY.size(); ++i) {
            SORTEDLIST.put(SEQUENCE.get(i), FREQUENCY.get(i));
        }
    }

    private void setSEQUENCE(List<String> SEQUENCE) { this.SEQUENCE = SEQUENCE; }
    private void setFREQUENCY(List<Integer> FREQUENCY) { this.FREQUENCY = FREQUENCY; }
    private void setDATA(String DATA) { this.DATA = DATA; }

    public List<Integer> getFREQUENCY() { return FREQUENCY; }
    public List<String> getSEQUENCE() { return SEQUENCE; }
    public String getData () { return DATA; }

    public String getFrequencyAt (int index) {
        return (SEQUENCE.get(index) + ":" + FREQUENCY.get(index));
    }

    //supposedly returns the frequency of a selected string.
    public Integer getFrequencyOf (String seq) {
        int index = SEQUENCE.lastIndexOf(seq);
        return FREQUENCY.get(index);
    }

    //prints the list out in this format:
//    [ 1	aha, 1 ]
//    [ 2	all, 1 ]
//    [ 3	and, 1 ]
//    [ 4	ark, 1 ]
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int counter = 1;

        String temp;
        for (Map.Entry<String, Integer> entry: SORTEDLIST.entrySet()) {
            String  key = entry.getKey();
            Integer val = entry.getValue();

            temp = "[ " + counter + "\t" + key + ", " + val + " ]\n";
            builder.append(temp);
            ++counter;
        }

        return builder.toString();
    }
}
