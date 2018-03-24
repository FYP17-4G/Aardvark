package com.example.FYP.aardvark_project.Analytics;

import java.util.*;

public class Analysis {
    private String DATA;
    private List<String> SEQUENCE;
    private List<Integer> FREQUENCY;
    private TreeMap<String, Integer> SORTEDLIST;

    private List<Pair> pairList;

    public Analysis () {}
    public Analysis (String newData, List<String> newSequence, List<Integer> newFrequency)
    {
        setDATA(newData);
        setSEQUENCE(newSequence);
        setFREQUENCY(newFrequency);

        SORTEDLIST = new TreeMap<>();
        for (int i = 0; i < FREQUENCY.size(); ++i) {
            SORTEDLIST.put(SEQUENCE.get(i), FREQUENCY.get(i));
        }

        setPairList();
    }

    public void sortPairList()
    {
        //sort the list
        Collections.sort(pairList, (t1, t2) -> {
            //contains sorting logic
            //this sorts from highest to lowest
            return t2.getFreq() - t1.getFreq();
        });
    }
    public void sortPairListAlphabet()
    {
        //sort the list
        Collections.sort(pairList, (t1, t2) -> {
            //contains sorting logic
            //this sorts from highest to lowest
            return t2.getSeq().charAt(0) - t1.getSeq().charAt(0);
        });

        Collections.reverse(pairList);
    }

    private void setPairList()
    {
        pairList = new ArrayList();

        //fill the list
        for(int i = 0; i < dataLength(); i++)
            pairList.add(new Pair(SEQUENCE.get(i), FREQUENCY.get(i)));

        //sortPairList();
    }

    private void setSEQUENCE(List<String> SEQUENCE) { this.SEQUENCE = SEQUENCE; }
    private void setFREQUENCY(List<Integer> FREQUENCY) { this.FREQUENCY = FREQUENCY; }
    private void setDATA(String DATA) { this.DATA = DATA; }

    public List<Integer> getFREQUENCY() { return FREQUENCY; }
    public List<String> getSEQUENCE() { return SEQUENCE; }
    public String getData () { return DATA; }

    /**this will return the character and its corresponding frequency*/
    public String getFrequencyAt (int index) {
        //return (SEQUENCE.get(index) + ":" + FREQUENCY.get(index));

        return (pairList.get(index).getSeq() + ":" + pairList.get(index).getFreq());
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

    public int dataLength()
    {
        return this.FREQUENCY.size();
    }

    //pair for sequence - frequency
    class Pair
    {
        private String seq;
        private int freq;

        public Pair(String seq, int freq)
        {
            this.seq = seq;
            this.freq = freq;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        public int getFreq() {
            return freq;
        }

        public void setFreq(int freq) {
            this.freq = freq;
        }
    }
}
