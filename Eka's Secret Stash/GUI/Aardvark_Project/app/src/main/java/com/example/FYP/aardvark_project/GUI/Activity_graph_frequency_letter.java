/**
 * Programmer: Eka Nugraha Pratama
 *
 * Contains the source code for Frequency letter activity that does the mapping of specified letter length (Singular character, bigram, trigram)
 * */

package com.example.FYP.aardvark_project.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.FYP.aardvark_project.R;
import com.example.FYP.aardvark_project.Analytics.Analysis;
import com.example.FYP.aardvark_project.Analytics.FrequencyAnalysis;
import com.example.FYP.aardvark_project.Common.Utility;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;


public class Activity_graph_frequency_letter extends AppCompatActivity {
    private final int DATA_LIMIT = 26;

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

    private App_Framework framework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        framework = new App_Framework(this, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_frequency_letter);

        cipherText = getIntent().getStringExtra("cipherText");

        setGraph();

        graph.setTitle("Frequency Graph (Letter)");
        this.setTitle("Frequency Graph (Letter)");
    }

    private void setGraph() {
        graph = findViewById(R.id.graph_freq_lot);
        setTextFrequencyTool();

        commonOccurenceSeries.setSpacing(50);

        graph.addSeries(commonOccurenceSeries);
        commonOccurenceSeries.resetData(calculateLetterFrequency(SINGULAR));
        setGraphLabel(commonOccurenceWords.toArray(new String[commonOccurenceWords.size()]));

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
    }

    private void setGraphLabel(String[] label) {
        /**set the graph to be scrollable*/
        graph.getViewport().setScrollable(true); //horizontal
        graph.getViewport().setScrollableY(true); //vertical

        /**set up x axis labels*/
        StaticLabelsFormatter staticLabels = new StaticLabelsFormatter(graph);
        staticLabels.setHorizontalLabels(label);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabels);
    }

    /**
     * Gets frequency of occurence of letter with given length
     * eg:
     * "The quick brown fox jumps over the lazy dog and another fox"
     * frequencyAnalysis(text, 3) >>> the:3, fox: 2, dog: 1, <etc>;
     * */
    private DataPoint[] calculateLetterFrequency(int SEQUENCE_LENGTH) {
        Utility util = Utility.getInstance();
        String cipherText = util.processText(this.cipherText); //this erases spaces, non alphabetic symbols, and new lines from the cipher text

        frequencyAnalysis = FrequencyAnalysis.frequencyAnalysis(cipherText, SEQUENCE_LENGTH);
        frequencyAnalysis.sortPairList();

        int dataLen = frequencyAnalysis.dataLength();
        if(dataLen > DATA_LIMIT)
            dataLen = DATA_LIMIT;

        DataPoint[] dp = new DataPoint[dataLen];

        LinearLayout layout = findViewById(R.id.frequency_letter_linear_layout);
        layout.removeAllViews();
        commonOccurenceWords.clear();
        for(int i = 0; i < dataLen; i++) {
            String val = frequencyAnalysis.getFrequencyAt(i);

            String[] split = val.split("\\:");
            dp[i] = new DataPoint(i, Integer.parseInt(split[1])); //add the frequency of a word
            commonOccurenceWords.add(split[0]); // add the word

            addDetailList(layout, split[0], split[1]);

            if(Integer.parseInt(split[1]) > LARGEST_FREQUENCY_VALUE)
                LARGEST_FREQUENCY_VALUE = Integer.parseInt(split[1]);
        }

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
        String[] list = {"Singular", "Bigram", "Trigram", "Custom..."};

        frequencySpinner = findViewById(R.id.graph_frequency_spinner);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        frequencySpinner.setAdapter(arrayAdapter);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String value = list[position];

                LARGEST_FREQUENCY_VALUE = 0;

                if(value.equals(list[0])) { //SINGULAR
                    commonOccurenceSeries.resetData(calculateLetterFrequency(SINGULAR));

                    graph.getGridLabelRenderer().resetStyles();
                }

                else if(value.equals(list[1])) { //BIGRAM
                    commonOccurenceSeries.resetData(calculateLetterFrequency(BIGRAM));

                    graph.getGridLabelRenderer().setTextSize(30);
                    graph.getGridLabelRenderer().reloadStyles();
                }
                else if(value.equals(list[2])) { //TRIGRAM
                    commonOccurenceSeries.resetData(calculateLetterFrequency(TRIGRAM));

                    graph.getGridLabelRenderer().setTextSize(20);
                    graph.getGridLabelRenderer().reloadStyles();
                }
                else if(value.equals(list[3])) // CUSTOM
                    framework.popup_getNumber_show("Custom Char length", "Char length", (dialogInterface, i) -> commonOccurenceSeries.resetData(calculateLetterFrequency(Integer.parseInt(framework.popup_getInput()))), 10);

                /**Re adjust Graph' Y axis minimum and maximum value*/
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMaxY(LARGEST_FREQUENCY_VALUE);
                graph.getViewport().setMinY(0);

                setGraphLabel(commonOccurenceWords.toArray(new String[commonOccurenceWords.size()]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
