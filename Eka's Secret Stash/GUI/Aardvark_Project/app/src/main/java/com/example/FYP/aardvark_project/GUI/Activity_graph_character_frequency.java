package com.example.FYP.aardvark_project.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.FYP.aardvark_project.Common.AppFramework;
import com.example.FYP.aardvark_project.R;
import com.example.FYP.aardvark_project.Analytics.Analysis;
import com.example.FYP.aardvark_project.Analytics.FrequencyAnalysis;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class Activity_graph_character_frequency extends AppCompatActivity {

    private final int DATA_LIMIT = 26; // A - Z

    private Analysis frequencyAnalysis;

    private int LARGEST_FREQUENCY_VALUE = 0;

    private GraphView graph;

    private String cipherText;
    private String displayedCipherText = new String();

    private BarGraphSeries<DataPoint> commonOccurenceSeries = new BarGraphSeries<DataPoint>();
    private ArrayList<String> commonOccurenceWords = new ArrayList<String>();

    private SeekBar seekBar;
    private TextView seekBarIndicator;
    private Button periodButton;

    private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private AppFramework framework;

    private CardView lowerCardView;
    private TextView sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_char_frequency);

        framework = new AppFramework(this, true);

        setGraph();
        setSeekBar();

        lowerCardView = findViewById(R.id.frequency_period_lower_card_view);
        lowerCardView.setVisibility(View.GONE);

        sign = findViewById(R.id.frequency_period_sign);
        sign.setVisibility(View.VISIBLE);

        this.setTitle("Character Frequency");

        /**set the default period to 1*/
        calculatePeriod(1);
        displayPeriodOnGraph(1);
    }

    private void setGraph() {
        cipherText = getIntent().getStringExtra("cipherText");

        graph = findViewById(R.id.graph_period_lot);

        commonOccurenceSeries.setSpacing(50);

        setGraphLabel(alphabet);
        graph.addSeries(commonOccurenceSeries);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
    }

    private void setSeekBar() {
        seekBar = findViewById(R.id.period_seekBar);
        seekBarIndicator = findViewById(R.id.period_indicator);
        periodButton = findViewById(R.id.period_button);

        seekBarIndicator.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);

        periodButton.setText("Period: 1");

        seekBar.setProgress(0);
        seekBar.setMax(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                displayPeriodOnGraph(seekBar.getProgress() + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        periodButton.setOnClickListener(view -> framework.popup_getNumber_show("Set Period", "Period key", (dialogInterface, i) -> {

            if(framework.popup_getInput().isEmpty())
                framework.system_message_small("Input cannot be empty");
            else
                calculatePeriod(Integer.parseInt(framework.popup_getInput()));
        }, 0));
    }

    private void displayPeriodOnGraph(int progress){
        displayedCipherText = getCipherTextPeriodOf(cipherText, progress, progress);
        seekBarIndicator.setText("Period: " + progress);
        sign.setVisibility(View.GONE);

        LARGEST_FREQUENCY_VALUE = 0;

        refresh();

        /**Re adjust Graph' Y axis minimum and maximum value*/
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(LARGEST_FREQUENCY_VALUE);
        graph.getViewport().setMinY(0);
    }

    private void calculatePeriod(int newVal){
        if(newVal < 1)
            framework.system_message_small("Invalid key input, the key value must be 1 or more");
        else
        {
            periodButton.setText("Period: " + newVal);

            seekBar.setMax(newVal - 1);

            seekBar.setProgress(0);

            lowerCardView.setVisibility(View.VISIBLE);
            seekBarIndicator.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
        }
    }

    private String getCipherTextPeriodOf(String input, int startFrom, int periodValue) {
        String returnVal = new String();

        for(int i = startFrom; i < cipherText.length(); i += periodValue)
            returnVal += input.charAt(i);

        return returnVal;
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

    /**This calculates letter frequency as well as displaying the results on screen*/
    private DataPoint[] calculateLetterFrequency(String input, int SEQUENCE_LENGTH) {
        frequencyAnalysis = FrequencyAnalysis.frequencyAnalysis(input, SEQUENCE_LENGTH);
        frequencyAnalysis.sortPairListAlphabet();

        int dataLen = frequencyAnalysis.dataLength();
        if(dataLen > DATA_LIMIT)
            dataLen = DATA_LIMIT;

        DataPoint[] dp = new DataPoint[dataLen];

        LinearLayout layout = findViewById(R.id.frequency_period_linear_layout);
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

    /**Display the detailed information using an xml layout*/
    private void addDetailList(LinearLayout layout, String word, String value) {
        View detailView = getLayoutInflater().inflate(R.layout.detail_list_view, null);
        TextView textViewWord = detailView.findViewById(R.id.detail_name);
        textViewWord.setText(word);

        TextView textViewValue = detailView.findViewById(R.id.detail_value);
        textViewValue.setText(value);

        layout.addView(detailView);
    }

    private void refresh() {
        framework.format(displayedCipherText);

        if(!displayedCipherText.isEmpty())
            commonOccurenceSeries.resetData(calculateLetterFrequency(framework.getMODIFIED_TEXT(), 1)); //sequence length is 1 because we are mapping each character

        String[] temp = commonOccurenceWords.toArray(new String[commonOccurenceWords.size()]);
        setGraphLabel(temp);
    }
}
