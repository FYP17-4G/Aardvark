package com.example.FYP.aardvark_project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.FYP.aardvark_project.kryptoTools.Analysis;
import com.example.FYP.aardvark_project.kryptoTools.CalculateIC;
import com.example.FYP.aardvark_project.kryptoTools.FrequencyAnalysis;
import com.example.FYP.aardvark_project.kryptoTools.Graph;
import com.example.FYP.aardvark_project.kryptoTools.mPair;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;


public class GUI_graph extends AppCompatActivity
{
    //Some pseudo macro variables
    private final int DATA_LIMIT = 20;
    private final int MAX_PERIOD = 20;
    private final int MAX_Y_AXIS = 60;
    private final int SEQUENCE_LENGTH = 3; //this number is only for a test

    private boolean GRAPH_FOCUSED = false;

    //Graph variables KEEP THIS HERE
    private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private final double[] englishDistribution = {8.12, 1.49, 2.71, 4.32, 12.02, 2.30, 2.03, 5.92, 7.31, 0.10, 0.69, 3.98, 2.61, 6.95, 7.68, 1.82, 0.11, 6.02, 6.28, 9.10, 2.88, 1.11, 2.09, 0.17, 2.11, 0.07};

    private GraphView graph;
    private TextView graphPeriodIndicator;
    private LineGraphSeries<DataPoint> cipherTextSeries = new LineGraphSeries<DataPoint>(); //USE THIS DATA SET FOR PLOTTING cipherText only!!!
    private LineGraphSeries<DataPoint> periodCipherTextSeries = new LineGraphSeries<DataPoint>(); //USE THIS DATA SET EXCLUSIVELY FOR CALCULATING GRAPH OF PERIOD N!!!
    private SeekBar graphSeekBar;

    private String cipherTextWithCurrentPeriod = new String();

    private TextView cipherTextView;

    private String cipherText = new String();

    //IC CALCULATION
    protected CalculateIC ic;
    private TextView cipherICTV;
    private double cipherIC = 0;

    private ArrayList<Double> cipherICofN;

    private App_Framework framework;

    //TODO()FINISH ANALYSIS, DISPLAY BIGRAM, TIGRAM ETC
    /**
     * Make sure 'sequenceLength' variable is no longer than the cipher text length;
     * 'sequenceLength' should be determinable by the user
     * */
    private Analysis frequencyAnalysis;

    private TextView frequencyAnalysisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        framework= new App_Framework(this, true);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graph);

        /**SET UP THE CIPHER TEXT VIEW AREA*/
        cipherTextView = (TextView) findViewById(R.id.textView_cipherTextView);
        cipherTextView.setTextColor(Color.BLACK);

        cipherText = getIntent().getStringExtra("cipherText");
        cipherTextView.setText(cipherText);

        setGraph();
        rePlotGraph();
        //set IC related function
        ic = new CalculateIC();

        calculateCipherIC(MAX_PERIOD);

        calculateLetterFrequency();
        focusOnGraph(false);

        framework.system_message_small("Tap on the graph to display detailed information <<< DELETE LATER");
    }

    /**THIS FUNCTION HIDES AND SHOWS ELEMENTS WHEN THE USER CLICKS ON THE GRAPH*/
    private void focusOnGraph(boolean focused)
    {
        FrameLayout cipherFrameLayout = findViewById(R.id.scrollable_cipher_layout);
        LinearLayout graphDetailedInfo = findViewById(R.id.graphDetailedInfoLayout);

        if(focused)
        {
            cipherFrameLayout.setVisibility(View.GONE);
            graphDetailedInfo.setVisibility(View.VISIBLE);
        }
        else if(!focused)
        {
            cipherFrameLayout.setVisibility(View.VISIBLE);
            graphDetailedInfo.setVisibility(View.GONE);
        }
    }

    /**IC and FREQUENCY RELATED FUNCTIONS*/

    private void setGraph()
    {
        //TODO(me) find a way to make this more flexible (maybe allow the user to set their own maximum period)

        final SeekBar.OnSeekBarChangeListener graphPeriodListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                int prog = (graphSeekBar.getProgress() + 1);

                //TODO() REPLACE THIS WITH MARK'S CODE
                cipherTextWithCurrentPeriod = getCipherTextPeriodOf(0, prog); //cipherTextWithCurrentPeriod is class global
                rePlotGraph();

                graphPeriodIndicator.setText("IC of every N letters: "+ prog);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        };

        View.OnClickListener graphClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**This toggles element visibility in graph activity*/
                GRAPH_FOCUSED = !GRAPH_FOCUSED;
                focusOnGraph(GRAPH_FOCUSED);
            }
        };

        cipherICTV = (TextView) findViewById(R.id.cipherICTextView);

        //SET GRAPH VIEW, AND THE INDICATOR
        graphSeekBar = (SeekBar) findViewById(R.id.seekBar_period);
        graphSeekBar.setMax(MAX_PERIOD);
        //graphSeekBar.setMin(2); //TODO()CHANGE API 24 TO 26 FOR THIS TO WORK
        graphSeekBar.setProgress(0);
        graphSeekBar.setOnSeekBarChangeListener(graphPeriodListener);

        graphPeriodIndicator = (TextView) findViewById(R.id.period_indicator);
        graphPeriodIndicator.setText("IC of every N letters: " + graphSeekBar.getProgress());

        graph = (GraphView) findViewById(R.id.graph_lot);
        graph.setOnClickListener(graphClickListener);

        //set up graph axis title
        //graph.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

        //set the graph to be scrollable
        graph.getViewport().setScrollable(true); //horizontal
        graph.getViewport().setScrollableY(true); //vertical

        //set up x axis labels
        StaticLabelsFormatter staticLabels = new StaticLabelsFormatter(graph);
        staticLabels.setHorizontalLabels(alphabet);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabels);

        //set first series line color
        cipherTextSeries.setColor(Color.BLUE);

        //set second series line color
        periodCipherTextSeries.setColor(Color.DKGRAY);

        graph.getViewport().setScrollable(true);

        graph.addSeries(cipherTextSeries);
        graph.addSeries(periodCipherTextSeries);

        graph.setForegroundGravity(Gravity.CENTER);

    }

    private String getCipherTextPeriodOf(int startFrom, int periodValue)
    {
        String returnVal = new String();

        for(int i = startFrom; i < cipherText.length(); i += periodValue)
            returnVal += cipherText.charAt(i);

        return returnVal;
        //return cipherICWithPeriod.get(periodValue).toString();
    }

    private void rePlotGraph()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                //remove the data sets and series
                cipherTextSeries.resetData(plotGraph());

                if(!cipherTextWithCurrentPeriod.isEmpty())//if this variable is not empty
                    periodCipherTextSeries.resetData(plotGraph(cipherTextWithCurrentPeriod));
            }
        });
    }

    private DataPoint[] plotGraph() //use this to plot graph for english distribution
    {
        final int MAX_DATA_POINTS = 26;

        DataPoint[] dp = new DataPoint[MAX_DATA_POINTS];

        //fill series data
        for(int x = 0; x < MAX_DATA_POINTS; x++)
            dp[x] = new DataPoint(x, englishDistribution[x]);

        return dp;
    }

    /**Plot the graph using Graph.java
     * Get the frequency of all letters, and then process it
     * */
    private DataPoint[] plotGraph(String textOfPeriod) //plot the graph for a given text, the value of the given text should be a string of period N
    {
        textOfPeriod = framework.format(textOfPeriod).toLowerCase();

        final int MAX_DATA_POINTS = 26;

        DataPoint[] dp = new DataPoint[MAX_DATA_POINTS];

        Integer[] frequency = Graph.displayGraph(framework.clean(textOfPeriod));

        //fill series data
        for(int x = 0; x < MAX_DATA_POINTS; x++)
            dp[x] = new DataPoint(x, frequency[x]);

        return dp;
    }

    private void calculateCipherIC(int n)
    {
        cipherIC = getCipherIC(cipherText);

        cipherICTV.setText("[IC: " + Double.toString(cipherIC) + "]\n");

        cipherICofN = new ArrayList<Double>();

        ArrayList<mPair<Integer, Double>> averageICList = new ArrayList<>();

        /**calculate IC of period 2...n*/
        for(int i = 2; i < n; i++)
        {
            double averageIC = 0;

            cipherICofN = getCipherIC(cipherText, i);
            for(int x = 0; x < cipherICofN.size(); x++)
            {
                averageIC += cipherICofN.get(x);
            }

            averageIC = averageIC/i;

            //averageIC control: if its 0, dont display
            if(averageIC != 0)
                averageICList.add(new mPair(i, averageIC));
        }

        //sort the average IC list (DESCENDING)
        averageICList.sort(new Comparator<mPair<Integer, Double>>()
        {
            @Override
            public int compare(mPair<Integer, Double> t0, mPair<Integer, Double> t1) {
                return t1.second.compareTo(t0.second);
            }
        });

        //append the texts to the interface
        for(int i = 0; i < averageICList.size(); i++)
            cipherICTV.append("IC of period " + (averageICList.get(i).first) + ": " + new DecimalFormat("0.0000").format(averageICList.get(i).second) + "\n");

        cipherICTV.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void calculateLetterFrequency()
    {
        frequencyAnalysis = FrequencyAnalysis.frequencyAnalysis(cipherText, SEQUENCE_LENGTH);

        frequencyAnalysisTextView = (TextView) findViewById(R.id.frequencyDetailsTextView);

        int dataLen = frequencyAnalysis.dataLength();
        if(dataLen > DATA_LIMIT)
            dataLen = DATA_LIMIT;

        frequencyAnalysisTextView.append("[Common word occurences]\n");

        for(int i = 0; i < dataLen; i++)
            frequencyAnalysisTextView.append(frequencyAnalysis.getFrequencyAt(i) + "\n");

        frequencyAnalysisTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    protected double getCipherIC(String cText)
    {
        return ic.getIC(cText);
    }

    protected ArrayList<Double> getCipherIC(String cText, int n) //get cipher IC for period of 2 until N
    {
        return ic.getIC(n, cText);
    }
}
