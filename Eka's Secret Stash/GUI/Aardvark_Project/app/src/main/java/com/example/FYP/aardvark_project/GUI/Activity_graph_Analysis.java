package com.example.FYP.aardvark_project.GUI;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.FYP.aardvark_project.R;
import com.example.FYP.aardvark_project.kryptoTools.CalculateIC;
import com.example.FYP.aardvark_project.kryptoTools.Graph;
import com.example.FYP.aardvark_project.kryptoTools.InvalidKeyException;
import com.example.FYP.aardvark_project.Ciphers.Shift;
import com.example.FYP.aardvark_project.kryptoTools.Utility;
import com.example.FYP.aardvark_project.kryptoTools.mPair;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Activity_graph_Analysis extends AppCompatActivity {
    /**
     * Other variables
     */
    private int FREQUENCY_PERIOD_LIMIT;

    /**
     * Graph related variables
     */
    private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private final double[] englishDistribution = {8.12, 1.49, 2.71, 4.32, 12.02, 2.30, 2.03, 5.92, 7.31, 0.10, 0.69, 3.98, 2.61, 6.95, 7.68, 1.82, 0.11, 6.02, 6.28, 9.10, 2.88, 1.11, 2.09, 0.17, 2.11, 0.07};

    private Button shiftLeftButton;
    private Button shiftRightButton;

    private GraphView graph;
    private TextView graphPeriodIndicator;
    private LineGraphSeries<DataPoint> cipherTextSeries = new LineGraphSeries<DataPoint>();
    private LineGraphSeries<DataPoint> periodCipherTextSeries = new LineGraphSeries<DataPoint>();
    private SeekBar graphSeekBar;
    private List<String> cipherTextPeriodList = new ArrayList<>();
    private Button periodButton;
    private int period = 0;

    private TextView keyValueTextView;
    private String keyValue = new String();

    private String cipherTextWithCurrentPeriod = new String();

    private String cipherText = new String();

    private CardView analysisPeriodCardView;

    /**
     * IC related variables
     */
    protected CalculateIC ic;
    private double cipherIC = 0;

    private ArrayList<Double> cipherICofN;

    private App_Framework framework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        framework = new App_Framework(this, true);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graph_analysis);

        analysisPeriodCardView = findViewById(R.id.analysis_period_card_view);

        cipherText = getIntent().getStringExtra("cipherText");

        FREQUENCY_PERIOD_LIMIT = cipherText.length() / 2;

        setGraph();
        rePlotGraph();

        ic = new CalculateIC();

        calculateCipherIC(FREQUENCY_PERIOD_LIMIT);

        getSupportActionBar().setElevation(0);

        this.setTitle("Frequency Analysis");

        keyValueTextView = findViewById(R.id.analysis_key_value);

        setAnalysisShiftButtons();
        setInfoButton();
    }

    private void setInfoButton()
    {
        String message = "Press on the top button to set period manually, press on individual card to set the period automatically";

        ImageView infoButton = findViewById(R.id.graph_analysis_info);
        infoButton.setOnClickListener(view -> framework.system_message_popup("Info", message, "Got it"));
    }

    private void setAnalysisShiftButtons() {
        shiftLeftButton = findViewById(R.id.button_analysis_shift_left);
        shiftRightButton = findViewById(R.id.button_analysis_shift_right);

        shiftLeftButton.setOnClickListener(view -> {
            try {
                cipherTextWithCurrentPeriod = new Shift().decrypt(framework.format(cipherTextWithCurrentPeriod).toLowerCase(), "1"); //shift the graph to the right
                DataPoint[] dp = plotGraph(cipherTextWithCurrentPeriod);
                periodCipherTextSeries.resetData(dp);

                if (graphSeekBar.getProgress() > 0) {
                    int charIdx = graphSeekBar.getProgress() - 1;
                    char target = keyValue.charAt(charIdx);
                    cipherTextPeriodList.set(charIdx, cipherTextWithCurrentPeriod);

                    int val = (int) target - 1;
                    target = (char) val;
                    if (target < 'A')
                        target = 'Z';

                    StringBuilder sb = new StringBuilder(keyValue);
                    sb.setCharAt(charIdx, target);

                    keyValue = new String(sb);
                    keyValueTextView.setText(keyValue);
                }
            } catch (InvalidKeyException e) {
                framework.system_message_small(e.getMessage());
            }
        });

        shiftRightButton.setOnClickListener(view -> {
            try {
                cipherTextWithCurrentPeriod = new Shift().encrypt(framework.format(cipherTextWithCurrentPeriod).toLowerCase(), "1"); //shift the graph to the left
                DataPoint[] dp = plotGraph(cipherTextWithCurrentPeriod);
                periodCipherTextSeries.resetData(dp);

                if (graphSeekBar.getProgress() > 0) {
                    int charIdx = graphSeekBar.getProgress() - 1;
                    char target = keyValue.charAt(charIdx);
                    cipherTextPeriodList.set(charIdx, cipherTextWithCurrentPeriod);

                    int val = (int) target + 1;
                    target = (char) val;
                    if (target > 'Z')
                        target = 'A';

                    StringBuilder sb = new StringBuilder(keyValue);
                    sb.setCharAt(charIdx, target);

                    keyValue = new String(sb);
                    keyValueTextView.setText(keyValue);
                }
            } catch (InvalidKeyException e) {
                framework.system_message_small(e.getMessage());
            }
        });
    }

    /**
     * IC and FREQUENCY RELATED FUNCTIONS
     */
    private void setGraphLabel(String[] label) {
        /**set the graph to be scrollable*/
        graph.getViewport().setScrollable(true); //horizontal
        graph.getViewport().setScrollableY(true); //vertical

        /**set up x axis labels*/
        StaticLabelsFormatter staticLabels = new StaticLabelsFormatter(graph);
        staticLabels.setHorizontalLabels(label);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabels);
    }

    private void setGraph() {
        final SeekBar.OnSeekBarChangeListener graphPeriodListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int prog = graphSeekBar.getProgress();

                if (prog > 0) {
                    cipherTextWithCurrentPeriod = cipherTextPeriodList.get(prog - 1); // minus one because it gets its data from an array list
                    rePlotGraph();
                } else if (prog <= 0)
                    periodCipherTextSeries.resetData(plotGraph("")); //reset the data to contain no data

                graphPeriodIndicator.setText("IC of every N letters: " + prog);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        /**Set the graph view seek bar and the indicator*/
        graphSeekBar = findViewById(R.id.seekBar_period);
        graphSeekBar.setMax(0);
        graphSeekBar.setProgress(0);
        graphSeekBar.setOnSeekBarChangeListener(graphPeriodListener);

        graphPeriodIndicator = findViewById(R.id.period_indicator);
        graphPeriodIndicator.setText("IC of every N letters: " + graphSeekBar.getProgress());

        graph = findViewById(R.id.graph_lot);

        periodButton = findViewById(R.id.button_period);
        periodButton.setText("Period: " + period);

        analysisPeriodCardView.setVisibility(View.GONE);

        periodButton.setOnClickListener(view -> framework.popup_getNumber_show("Set period", "Max period: " + FREQUENCY_PERIOD_LIMIT, (dialogInterface, i) -> calculatePeriodOf(Integer.parseInt(framework.popup_getInput())), 0));

        graph.setForegroundGravity(Gravity.CENTER);

        setGraphLabel(alphabet);

        periodCipherTextSeries.setColor(Color.parseColor("#FFA500")); //Orange

        graph.addSeries(cipherTextSeries);
        graph.addSeries(periodCipherTextSeries);
    }

    /**
     * Set the seek bar max value according to the specified period
     */
    private void calculatePeriodOf(int n) {
        period = n;

        if (period <= 0)
            framework.system_message_small("Period value is invalid");

        else if (period > FREQUENCY_PERIOD_LIMIT)
            framework.system_message_small("Period value cannot be more than " + FREQUENCY_PERIOD_LIMIT);
        else {
            fillCipherTextPeriodList(period);
            periodButton.setText("Period: " + period);
            graphSeekBar.setMax(period);

            analysisPeriodCardView.setVisibility(View.VISIBLE);

            /**Set new key*/

            String newKey = new String();

            for (int z = 0; z < period; z++)
                newKey += "A";

            keyValue = newKey;
            keyValueTextView.setText(newKey);
        }
    }

    /**
     * Populate the array list that contains substring of cipher text for Kasiski test
     * The program will then read from this array list and represent it in the graph
     * */
    private void fillCipherTextPeriodList(int period) {
        cipherTextPeriodList.clear();

        ArrayList<StringBuilder> temp = CalculateIC.getEveryNthLetter(period, cipherText);

        for (int i = 0; i < period; i++)
            cipherTextPeriodList.add(new String(temp.get(i)));
    }

    private void rePlotGraph() {
        runOnUiThread(() -> {

            cipherTextSeries.resetData(plotGraph()); //remove the data sets and series

            if(!cipherTextWithCurrentPeriod.isEmpty())
                periodCipherTextSeries.resetData(plotGraph(cipherTextWithCurrentPeriod));
        });
    }
    private DataPoint[] plotGraph() { //use this to plot graph for english distribution
        final int MAX_DATA_POINTS = 26;

        DataPoint[] dp = new DataPoint[MAX_DATA_POINTS];

        for(int x = 1; x < MAX_DATA_POINTS + 1; x++)
            dp[x - 1] = new DataPoint(x, englishDistribution[x - 1]);

        return dp;
    }
    private DataPoint[] plotGraph(String textOfPeriod) { //plot the graph for a given text, the value of the given text should be a string of period N
        int divisor = textOfPeriod.length();

        textOfPeriod = framework.format(textOfPeriod).toLowerCase();

        final int MAX_DATA_POINTS = 26;

        DataPoint[] dp = new DataPoint[MAX_DATA_POINTS];

        Integer[] frequency = Graph.displayGraph(framework.format(textOfPeriod));

        for(int x = 1; x < MAX_DATA_POINTS + 1; x++)
            dp[x - 1] = new DataPoint(x, frequencyFormula((double)frequency[x - 1], (double)divisor));

        return dp;
    }

    /**(n/length of text) * 100*/
    double frequencyFormula(double n, double divisor)
    {
        return (n/divisor) * 100;
    }

    private void calculateCipherIC(int n) {
        if(n > 20)
            n = 20;

        Utility util = Utility.getInstance();
        String cipherText = util.processText(this.cipherText); //this erases spaces, non alphabetic symbols, and new lines from the cipher text

        cipherICofN = new ArrayList<Double>();

        cipherIC = getCipherIC(cipherText);

        ArrayList<mPair<Integer, Double>> averageICList = new ArrayList<>();

        /**calculate IC of period 2...n, and calculate its average*/
        for(int i = 2; i < n; i++) {
            double averageIC = 0;

            cipherICofN = getCipherIC(cipherText, i);
            for(int x = 0; x < cipherICofN.size(); x++)
                averageIC += cipherICofN.get(x);

            averageIC = averageIC/i;

            if(averageIC != 0) //averageIC control: if its 0, dont display
                averageICList.add(new mPair(i, averageIC));
        }

        averageICList.sort((t0, t1) -> t1.second.compareTo(t0.second)); //sort the average IC list (DESCENDING)

        LinearLayout ICViewLinearLayout = findViewById(R.id.IC_view_linear_layout);
        for(int i = 0; i < averageICList.size(); i++) { //append the texts to the interface

            View detailView = getLayoutInflater().inflate(R.layout.detail_list_view, null);
            TextView detailName = detailView.findViewById(R.id.detail_name);
            TextView detailValue = detailView.findViewById(R.id.detail_value);

            detailName.setText("IC of period: " + averageICList.get(i).first.toString());
            detailValue.setText(new DecimalFormat("0.0000").format(averageICList.get(i).second).toString());

            CardView cardView = detailView.findViewById(R.id.detail_list_card_view);
            cardView.setClickable(true);

            /**Set card view on touch animation*/
            StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(this, R.anim.lift_on_touch);
            cardView.setStateListAnimator(stateListAnimator);

            /**Clicking on the card view will change the period being set*/
            int period = averageICList.get(i).first;
            cardView.setOnClickListener(view -> {
                calculatePeriodOf(period);
                periodButton.setText("Period: " + period);
                framework.system_message_small("Current period is set to: " + period);
            });

            ICViewLinearLayout.addView(detailView);
        }
    }

    protected double getCipherIC(String cText)
    {
        return ic.getIC(cText);
    }

    protected ArrayList<Double> getCipherIC(String cText, int n) { //get cipher IC for period of 2 until N
        return ic.getIC(n, cText);
    }
}