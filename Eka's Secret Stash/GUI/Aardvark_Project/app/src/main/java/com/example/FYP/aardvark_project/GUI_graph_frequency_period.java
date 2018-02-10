package com.example.FYP.aardvark_project;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.FYP.aardvark_project.kryptoTools.Analysis;
import com.example.FYP.aardvark_project.kryptoTools.FrequencyAnalysis;
import com.example.FYP.aardvark_project.kryptoTools.Utility;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class GUI_graph_frequency_period extends AppCompatActivity {

    private final int DATA_LIMIT = 26; // A - Z

    private Analysis frequencyAnalysis;
    private TextView frequencyAnalysisTextView;

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

    private App_Framework framework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_frequency__period);

        framework = new App_Framework(this, true);

        setGraph();
        setSeekBar();

        graph.setTitle("Frequency Graph (Period)");
        this.setTitle("Frequency Graph (Period)");
    }

    private void setGraph()
    {
        cipherText = getIntent().getStringExtra("cipherText");

        graph = findViewById(R.id.graph_period_lot);
        frequencyAnalysisTextView = findViewById(R.id.graph_period_detail);

        setGraphLabel(alphabet);
        graph.addSeries(commonOccurenceSeries);
        setGraphYAxisHeight();
    }

    private void setGraphYAxisHeight()
    {
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(LARGEST_FREQUENCY_VALUE);
        graph.getViewport().setYAxisBoundsManual(true);
    }

    private void setSeekBar()
    {
        seekBar = findViewById(R.id.period_seekBar);
        seekBarIndicator = findViewById(R.id.period_indicator);
        periodButton = findViewById(R.id.period_button);

        seekBarIndicator.setText("Period: 0");
        periodButton.setText("Max Period: 0");

        seekBar.setProgress(0);
        seekBar.setMax(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(seekBar.getProgress() > 0)
                {
                    displayedCipherText = getCipherTextPeriodOf(cipherText, seekBar.getProgress(), seekBar.getProgress());
                    seekBarIndicator.setText("Period: " + seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                refresh();
            }
        });

        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                framework.popup_getNumber_show("Set Period", "Period key", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int newVal = Integer.parseInt(framework.popup_getInput());

                        periodButton.setText("Max period: " + newVal);
                        seekBar.setMax(newVal);
                        seekBar.setProgress(0);
                    }
                }, 0);
            }
        });
    }

    private String getCipherTextPeriodOf(String input, int startFrom, int periodValue)
    {
        String returnVal = new String();

        for(int i = startFrom; i < cipherText.length(); i += periodValue)
            returnVal += input.charAt(i);

        return returnVal;
    }

    private void setGraphLabel(String[] label)
    {
        /**set the graph to be scrollable*/
        graph.getViewport().setScrollable(true); //horizontal
        graph.getViewport().setScrollableY(true); //vertical

        /**set up x axis labels*/
        StaticLabelsFormatter staticLabels = new StaticLabelsFormatter(graph);
        staticLabels.setHorizontalLabels(label);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabels);
    }

    private DataPoint[] calculateLetterFrequency(String input, int SEQUENCE_LENGTH)
    {
        Utility util = Utility.getInstance();
        String cipherText = util.processText(this.displayedCipherText); //this erases spaces, non alphabetic symbols, and new lines from the cipher text

        frequencyAnalysis = FrequencyAnalysis.frequencyAnalysis(input, SEQUENCE_LENGTH);

        frequencyAnalysisTextView = findViewById(R.id.graph_period_detail);

        int dataLen = frequencyAnalysis.dataLength();
        if(dataLen > DATA_LIMIT)
            dataLen = DATA_LIMIT;

        DataPoint[] dp = new DataPoint[dataLen];

        frequencyAnalysisTextView.setText("[Common word occurences of period: " + seekBar.getProgress() + "]\n");

        commonOccurenceWords.clear();
        for(int i = 0; i < dataLen; i++)
        {
            String val = frequencyAnalysis.getFrequencyAt(i);
            frequencyAnalysisTextView.append(val + "\n");
            String[] split = val.split("\\:");
            dp[i] = new DataPoint(i, Integer.parseInt(split[1])); //add the frequency of a word
            commonOccurenceWords.add(split[0]); // add the word

            if(Integer.parseInt(split[1]) > LARGEST_FREQUENCY_VALUE)
                LARGEST_FREQUENCY_VALUE = Integer.parseInt(split[1]);
        }

        frequencyAnalysisTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        return dp;
    }

    private void refresh()
    {
        if(seekBar.getProgress() > 0)
        {
            if(!displayedCipherText.isEmpty())
                commonOccurenceSeries.resetData(calculateLetterFrequency(displayedCipherText, 1)); //sequence length is 1 because we are mapping each character

            setGraphYAxisHeight();
            String[] temp = commonOccurenceWords.toArray(new String[commonOccurenceWords.size()]);
            setGraphLabel(temp);
        }
    }
}
