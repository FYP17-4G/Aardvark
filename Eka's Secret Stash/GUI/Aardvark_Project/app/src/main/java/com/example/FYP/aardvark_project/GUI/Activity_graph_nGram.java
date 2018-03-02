package com.example.FYP.aardvark_project.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.FYP.aardvark_project.Common.AppFramework;
import com.example.FYP.aardvark_project.R;
import com.example.FYP.aardvark_project.Analytics.Analysis;
import com.example.FYP.aardvark_project.Analytics.FrequencyAnalysis;
import com.example.FYP.aardvark_project.Common.Utility;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import com.example.FYP.aardvark_project.Common.mPair;

import java.util.ArrayList;
import java.util.Collections;


public class Activity_graph_nGram extends AppCompatActivity {

    private final int GRAPH_DATA_SERIES_LIMIT = 26;

    private final int SINGULAR = 1;
    private final int BIGRAM = 2;
    private final int TRIGRAM = 3;

    private int LARGEST_FREQUENCY_VALUE = 0;

    private BarGraphSeries<DataPoint> commonOccurenceSeries = new BarGraphSeries<DataPoint>();
    private ArrayList<String> commonOccurenceWords = new ArrayList<String>();

    /**
     * Make sure 'sequenceLength' variable is no longer than the cipher text length;
     * 'sequenceLength' should be determinable by the user
     * */
    private Analysis frequencyAnalysis;

    private Spinner frequencySpinner;

    private GraphView graph;

    private String cipherText = new String();

    private AppFramework framework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        framework = new AppFramework(this, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_ngram);

        cipherText = getIntent().getStringExtra("cipherText");

        setGraph();

        this.setTitle("N-Gram Analysis");
    }

    private void setGraph() {

        /**Set up the graph view*/
        graph = findViewById(R.id.graph_freq_lot);
        setTextFrequencyTool();

        /**Set graph view parameters*/
        commonOccurenceSeries.setSpacing(50); //spacing between the series entry, value is in percentage
        commonOccurenceSeries.setDrawValuesOnTop(true);

        graph.addSeries(commonOccurenceSeries);
        commonOccurenceSeries.resetData(calculateLetterFrequency(SINGULAR));
        setGraphLabel(commonOccurenceWords.toArray(new String[commonOccurenceWords.size()]));

        graph.getGridLabelRenderer().setTextSize(25);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.getViewport().setMinY(0);

        //graph.getViewport().setScrollable(true); //enables horizontal scrolling

        graph.setTitle("Top 26 N-Gram frequency");
    }

    private void setGraphLabel(String[] label) {

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(label);

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        graph.getGridLabelRenderer().setLabelsSpace(20);
    }

    /**
     * Gets frequency of occurence of letter with given length
     * eg:
     * "The quick brown fox jumps over the lazy dog and another fox"
     * frequencyAnalysis(text, 3) >>> the:3, fox: 2, dog: 1, <etc>;
     * */
    private DataPoint[] calculateLetterFrequency(int SEQUENCE_LENGTH) {

        ArrayList<mPair<String, String>> tempList = new ArrayList<>();

        Utility util = Utility.getInstance();
        String cipherText = util.processText(this.cipherText); //this erases spaces, non alphabetic symbols, and new lines from the cipher text

        frequencyAnalysis = FrequencyAnalysis.frequencyAnalysis(cipherText, SEQUENCE_LENGTH);
        frequencyAnalysis.sortPairListAlphabet();

        int dataLen = frequencyAnalysis.dataLength();


        LinearLayout layout = findViewById(R.id.frequency_letter_linear_layout);

        layout.removeAllViews();
        tempList.clear();

        for(int i = 0; i < dataLen; i++) {
            String val = frequencyAnalysis.getFrequencyAt(i);

            String[] split = val.split("\\:");
            tempList.add(new mPair<>(split[0], split[1]));

            if(Integer.parseInt(split[1]) > LARGEST_FREQUENCY_VALUE)
                LARGEST_FREQUENCY_VALUE = Integer.parseInt(split[1]);
        }

        //sort the list
        for(int i = 0; i < tempList.size(); i++)
            for(int j = i; j < tempList.size(); j++)
                if(Integer.parseInt(tempList.get(j).second) > Integer.parseInt(tempList.get(i).second))
                    Collections.swap(tempList, i, j);

        /**
         * Add the frequency result to the detail list (the bottom card view)
         * */
        for(mPair<String, String> pair: tempList)
            addDetailList(layout, pair.first, pair.second); //add to details view layout

        /**
         * Add the frequency result to the graph view
         * but only shows the top 26 results (26 == the value of GRAPH_DATA_SERIES_LIMIT)
         * */
        commonOccurenceWords.clear();
        DataPoint[] dp = new DataPoint[GRAPH_DATA_SERIES_LIMIT - 1];

        if(dataLen < GRAPH_DATA_SERIES_LIMIT)
            dp = new DataPoint[dataLen];

        int count = 0;
        for(mPair<String, String> pair: tempList){
            if(count >= GRAPH_DATA_SERIES_LIMIT - 1)
                break;

            dp[count] = new DataPoint(count, Integer.parseInt(pair.second)); //add the FREQUENCY of a word
            commonOccurenceWords.add(pair.first); //add the word
            count++;
        }

        graph.getViewport().setMaxY(LARGEST_FREQUENCY_VALUE);

        return dp;
    }

    /**add the detailed information view using predefined an xml layout*/
    private void addDetailList(LinearLayout layout, String word, String value) {
        View detailView = getLayoutInflater().inflate(R.layout.detail_list_view, null);
        TextView textViewWord = detailView.findViewById(R.id.detail_name);
        textViewWord.setText(word);

        TextView textViewValue = detailView.findViewById(R.id.detail_value);
        textViewValue.setText(value);

        layout.addView(detailView);
    }

    private void setTextFrequencyTool() {
        String[] list = {"Unigram", "Bigram", "Trigram"};

        frequencySpinner = findViewById(R.id.graph_frequency_spinner);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        frequencySpinner.setAdapter(arrayAdapter);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String value = list[position];

                LARGEST_FREQUENCY_VALUE = 0;

                if(value.equals(list[0]))//SINGULAR
                    commonOccurenceSeries.resetData(calculateLetterFrequency(SINGULAR));
                else if(value.equals(list[1])) //BIGRAM
                    commonOccurenceSeries.resetData(calculateLetterFrequency(BIGRAM));
                else if(value.equals(list[2]))//TRIGRAM
                    commonOccurenceSeries.resetData(calculateLetterFrequency(TRIGRAM));

                setGraphLabel(commonOccurenceWords.toArray(new String[commonOccurenceWords.size()]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
