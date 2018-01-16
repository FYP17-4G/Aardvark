package com.example.ekanugrahapratama.aardvark_project;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.view.View;
import android.graphics.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GraphView;

import com.example.ekanugrahapratama.aardvark_project.analysisTools.CryptoAnalysis;
import com.example.ekanugrahapratama.aardvark_project.kryptoTools.*;

public class GUI_fragment_project_view extends Fragment {

    App_Framework framework;

    private String projectTitle = new String();
    private String projectID = new String();

    //TODO(***) Declare variables here
    /**[VARIABLE SECTION]*/
    //Project cipher variables
    private List<String> changeHistory = new ArrayList<>();
    private String cipherText = new String();
    private String cipherTextWithCurrentPeriod = new String();
    private String originalCipherText = new String();
    private TextView cipherTextView;

    private String beforeShift = new String();

    //Analysis variables
    private CryptoAnalysis cryptoAnalysis;

    //Graph variables KEEP THIS HERE
    private String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private final double[] englishDistribution = {8.12, 1.49, 2.71, 4.32, 12.02, 2.30, 2.03, 5.92, 7.31, 0.10, 0.69, 3.98, 2.61, 6.95, 7.68, 1.82, 0.11, 6.02, 6.28, 9.10, 2.88, 1.11, 2.09, 0.17, 2.11, 0.07};

    private GraphView graph;
    private TextView graphPeriodIndicator;
    private LineGraphSeries<DataPoint> cipherTextSeries = new LineGraphSeries<DataPoint>(); //USE THIS DATA SET FOR PLOTTING cipherText only!!!
    private LineGraphSeries<DataPoint> periodCipherTextSeries = new LineGraphSeries<DataPoint>(); //USE THIS DATA SET EXCLUSIVELY FOR CALCULATING GRAPH OF PERIOD N!!!
    private SeekBar graphSeekBar;

    //IC CALCULATION
    private int MAX_PERIOD = 20;

    protected CalculateIC ic;
    private TextView cipherICTV;
    private double cipherIC = 0;

    private ArrayList<Double> cipherICofN;

    /**Variables for Krypto tools*/
    //CAESAR CIPHER
    private SeekBar caesarSeekBar;
    private TextView indicator;
    private int shiftCipherBy = 0;

    private Button caesarShiftRight;
    private Button caesarShiftLeft;

    private Button caesar_popup_button;

    //SUBSTITUTION
    private Spinner charASpinner; //Replace charA with charB
    private Spinner charBSpinner;
    private Button charSubButton;
    private char charA;
    private char charB;

    private String stringSubValue;

    //undo button and reset button
    private Button undoButton;
    private Button resetButton;

    //<...>

    /**TAB RELATED VARIABLES*/

    private View view;
    private FragmentActivity fragmentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //set up the view
        view = inflater.inflate(R.layout.fragment_project_view_main, container, false);
        fragmentActivity = this.getActivity(); /**This is important: use this to access activity related functions*/

        this.projectTitle = getArguments().getString("title");
        this.projectID = getArguments().getString("id");

        framework= new App_Framework(view.getContext());

        /**SET UP THE CIPHER TEXT VIEW AREA*/
        cipherTextView = (TextView) view.findViewById(R.id.project_view_cipher_text);
        cipherTextView.setTextColor(Color.WHITE);

        //SET UP TOOLS FOR THE PROJECT
        setCaesarTool(); //shift cipher
        setSubstitutionTool();
        setTranspoTools();

        //SET OTHER TOOLS HERE<...>

        getCipherTextFromFile();

        originalCipherText = cipherText; //TODO(me) FIND A WAY TO SET ORIGINAL CIPHER TEXT PROPERLY, SINCE THIS ONE IS JUST DIRECT ASSIGNING
        changeHistory.add(originalCipherText);
        cipherTextView.setText(cipherText);

        setGraph();
        rePlotGraph();

        setUndoButton();
        setResetButton();

        //set IC related function
        ic = new CalculateIC();

        calculateCipherIC(MAX_PERIOD);

        return view;
    }

    //TEST_CIPHER.txt is inside "assets" folder. DELETE IT LATER AND CHANGE IT ACCORDINGLY
    private void getCipherTextFromFile()
    {

        /**GET ORIGINAL CIPHER TEXT FILE FROM FILE*/
        this.cipherText = framework.init(framework.getCipherTextFromFile(this.projectID + this.projectTitle + "cipherTextOriginal.txt"));

        //this.cipherText = "dfwkgtnulfxpfggchrugiiezbxmzgsiifgbxsthttrvwyh.dzwdgivgbayvtrqrvxbxnxusxlublvfvpldr.fuhtckacqaimmcnfxduetmnaapxbkacecnawymgd.gxpxoulmiofindsvpcaikmjtsvxgcgfkzgaevf.pnehscczgeroemppskxbcokbkerlwcccvtbsfixojeemjnyfnndsjxqhifgkgs.gxpxoulylhzlfdujxqhqltptaewkxsvkw.wolkfpdyxjslrblvimxwtt";

        /**GET CIPHER TEXT WITH THE CHANGES*/

    }

    public String getCipherText()
    {
        return this.cipherText;
    }


    private void setUndoButton()
    {
        undoButton = (Button) view.findViewById(R.id.button_undo);

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                undo();
            }
        });
    }

    private void setResetButton()
    {
        resetButton = (Button) view.findViewById(R.id.button_reset);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                reset();
            }
        });
    }


    private void undo() //undo the cipher text into its previous state
    {

        if(changeHistory.size() == 1)
            framework.system_message_small("Maximum undo attempt reached");

        else if(!this.cipherText.equals(this.originalCipherText) && !changeHistory.isEmpty() && changeHistory.size() >1) //because the first entry == originalCipherText
        {
            changeHistory.remove(changeHistory.size()-1); //remove the latest entry
            this.cipherText = changeHistory.get(changeHistory.size()-1);
            cipherTextView.setText(framework.init(cipherText));
        }
    }

    private void reset() //reset the cipher text into its original state
    {
        if(this.cipherText.equals(this.originalCipherText))
            framework.system_message_small("cipher text is already at its original state");

        else if(!this.cipherText.equals(this.originalCipherText) && !changeHistory.isEmpty())
        {
            this.cipherText = originalCipherText;
            cipherTextView.setText(framework.init(cipherText));
            changeHistory.clear(); //remove all items from the arraylist
        }
    }

    private void refresh()//refreshes the cipher text view
    {
        changeHistory.add(cipherText);
        cipherTextView.setText(framework.init(cipherText));
        rePlotGraph();
    }

    /**GUI SETUP*/
    /**for all functions that changes the value of variable cipherText, call function "refresh()" afterwards to refresh cipher text view on the GUI
     * example can be seen in functions "doShiftLeft()" and "doShiftRight()"
     * */
    private void setCaesarTool()
    {
        View shiftView = getLayoutInflater().inflate(R.layout.pop_shift_cipher, null);



        View.OnClickListener shiftListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if((caesarSeekBar.getProgress()) != 0)
                {
                    if(v.getId() == R.id.button_caesarShiftR)
                        doShiftRight();

                    else
                        doShiftLeft();

                    refresh();
                }
            }
        };

        final SeekBar.OnSeekBarChangeListener caesarSeekBarListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {

                shiftCipherBy = (seekBar.getProgress() - 26);

                if(shiftCipherBy < 0)
                {
                    doShiftLeft();
                }
                else
                {
                    doShiftRight();
                }

                indicator.setText("Current Shift: " + (seekBar.getProgress() - 26));
                refresh();
                //caesarSeekBar.dispatchDisplayHint(caesarSeekBar.getProgress());
                //shiftCipherBy = caesarSeekBar.getProgress();
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

        caesarSeekBar = (SeekBar) shiftView.findViewById(R.id.seekBar_caesar);
        indicator = (TextView) shiftView.findViewById(R.id.seekBar_indicator);
        indicator.setText("Shift by: 0");
        caesarSeekBar.setMax(52);
        caesarSeekBar.setProgress(26);
        caesarSeekBar.setOnSeekBarChangeListener(caesarSeekBarListener);

        caesarShiftLeft = (Button) shiftView.findViewById(R.id.button_caesarShiftL);
        caesarShiftRight = (Button) shiftView.findViewById(R.id.button_caesarShiftR);

        caesarShiftLeft.setOnClickListener(shiftListener);
        caesarShiftRight.setOnClickListener(shiftListener);

        List<Integer> content = new ArrayList<>();
        for(int i = 0; i < 26; i++)
            content.add(i);

        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //caesar shift button in fragment project view
        caesar_popup_button = view.findViewById(R.id.button_shiftPopup);
        caesar_popup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                beforeShift = cipherText;
                framework.popup_custom("Caesar Shift", shiftView);
            }
        });

    }

    private void doShiftLeft()
    {
        try
        {
            cipherText = new Shift().decrypt(beforeShift.toLowerCase(), Integer.toString(Math.abs(shiftCipherBy)));
        }catch(InvalidKeyException e)
        {}
    }

    private void doShiftRight()
    {
        try
        {
            cipherText = new Shift().encrypt(beforeShift.toLowerCase(), Integer.toString(Math.abs(shiftCipherBy)));
        }catch(InvalidKeyException e)
        {}
    }

    private void setSubstitutionTool()
    {
        View substitutionView = getLayoutInflater().inflate(R.layout.pop_substitution_cipher, null);

        /**Substitution by character*/
        Character[] alphabets = {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        //listener for charA spinner view
        AdapterView.OnItemSelectedListener charAListener = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                charA = adapterView.getItemAtPosition(i).toString().charAt(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {}
        };

        //listener for charB spinner view
        AdapterView.OnItemSelectedListener charBListener = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                charB = adapterView.getItemAtPosition(i).toString().charAt(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {}
        };

        View.OnClickListener charSubButtonListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if((charA != '-') || (charB != '-'))
                {
                    doSubstitution(charA, charB);
                    refresh();
                }
            }
        };

        View.OnClickListener stringSubButtonListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogInterface.OnClickListener popupListener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        stringSubValue = framework.popup_getInput(); //this gets the String key value
                        doSubstitution(stringSubValue);
                        refresh();
                    }
                };

                framework.popup_show("Substitute by String", "String key value", popupListener);
            }
        };

        /**Setup spinner for both charA and charB*/
        charASpinner = (Spinner) substitutionView.findViewById(R.id.spinner_charA);
        charBSpinner = (Spinner) substitutionView.findViewById(R.id.spinner_charB);

        List<Character> content = new ArrayList<Character>(Arrays.asList(alphabets)); //assign 'alphabets' array as a list data type

        //set spinner for charA
        ArrayAdapter<Character> spinnerAdapterA = new ArrayAdapter<Character>(substitutionView.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charASpinner.setAdapter(spinnerAdapterA);

        //set spinner for charB
        ArrayAdapter<Character> spinnerAdapterB = new ArrayAdapter<Character>(substitutionView.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charBSpinner.setAdapter(spinnerAdapterB);

        charASpinner = (Spinner) substitutionView.findViewById(R.id.spinner_charA);
        charBSpinner = (Spinner) substitutionView.findViewById(R.id.spinner_charB);
        charASpinner.setOnItemSelectedListener(charAListener);
        charBSpinner.setOnItemSelectedListener(charBListener);

        /**Setup button for Substitution by char*/
        charSubButton = (Button) substitutionView.findViewById(R.id.button_charSubstitution);
        charSubButton.setOnClickListener(charSubButtonListener);

        /**Setup button for Substitution by String*/
        //stringSubButton = (Button) view.findViewById(R.id.button_stringSubstitution);
        //stringSubButton.setOnClickListener(stringSubButtonListener);



        /**SETUP BUTTON IN VIEW*/
        Button substitutionButton = view.findViewById(R.id.button_subPopup);
        substitutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                framework.popup_custom("Substitution", substitutionView);
            }
        });

    }

    private void doSubstitution(String stringKey)
    {
        /**ENCRYPT OR DECRYPT ????*/
        try
        {
            cipherText = new Substitution().decrypt(cipherText, stringKey);
        }catch(InvalidKeyException e)
        {}
    }

    private void doSubstitution(char a, char b) //replace charA with charB
    {
        cipherText = new Substitution().byCharacter(a, b, cipherText, originalCipherText);
    }

    private void setTranspoTools()
    {
        Button TranspoButton = (Button) view.findViewById(R.id.button_normalTranspo);

        TranspoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        doTranspo(framework.popup_getInput());
                        refresh();
                    }
                };

                framework.popup_getNumber_show("Columnar Transposition", "Enter key", ocl, 100); /**CHANGE INPUT LENGTH LIMIT*/
            }
        });
    }

    private void doTranspo(String key)
    {
        try
        {
            cipherText = new Transposition().decrypt(cipherText, key);
        }catch(InvalidKeyException e)
        {}
    }

    /**IC and FREQUENCY RELATED FUNCTIONS*/

    private void setGraph()
    {
        View graphView = getLayoutInflater().inflate(R.layout.pop_graph, null);

        //TODO(me) find a way to make this more flexible (maybe allow the user to set their own maximum period)

        final SeekBar.OnSeekBarChangeListener graphPeriodListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                int prog = graphSeekBar.getProgress();

                if(prog > 0)
                {
                    cipherTextWithCurrentPeriod = getCipherTextPeriodOf(0, prog); //cipherTextWithCurrentPeriod is class global
                    rePlotGraph();
                }

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

        cipherICTV = (TextView) graphView.findViewById(R.id.cipherICTextView);

        graphPeriodIndicator = (TextView) graphView.findViewById(R.id.period_indicator);
        graphPeriodIndicator.setText("IC of every N letters: 0");

        graphSeekBar = (SeekBar) graphView.findViewById(R.id.seekBar_period);
        graphSeekBar.setMax(MAX_PERIOD);
        //graphSeekBar.setMin(2); //TODO()CHANGE API 24 TO 26 FOR THIS TO WORK
        graphSeekBar.setProgress(2);
        graphSeekBar.setOnSeekBarChangeListener(graphPeriodListener);

        graph = (GraphView) graphView.findViewById(R.id.graph_lot);

        //set up graph axis title
        //graph.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

        //set up x axis labels
        StaticLabelsFormatter staticLabels = new StaticLabelsFormatter(graph);
        staticLabels.setHorizontalLabels(alphabet);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabels);

        //set first series line color
        cipherTextSeries.setColor(Color.BLUE);

        //set second series line color
        periodCipherTextSeries.setColor(Color.DKGRAY);

        //set graph' X and Y range
        //graph.getViewport().setMaxX(28);
        //graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(20);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.getViewport().setScrollable(true);

        graph.addSeries(cipherTextSeries);
        graph.addSeries(periodCipherTextSeries);



        /**SET THE BUTTON IN VIEW*/
        Button graphButtonPopup = view.findViewById(R.id.button_graphPopup);
        graphButtonPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                framework.popup_custom("Frequency", graphView);
            }
        });
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
        fragmentActivity.runOnUiThread(new Runnable()
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

    private DataPoint[] plotGraph(String textOfPeriod) //plot the graph for a given text, the value of the given text should be a string of period N
    {
        final int MAX_DATA_POINTS = 26;

        DataPoint[] dp = new DataPoint[MAX_DATA_POINTS];

        //fill series data
        for(int x = 0; x < MAX_DATA_POINTS; x++)
        {
            int alphabetcount = charCount(framework.init(textOfPeriod.toLowerCase()), alphabet[x].charAt(0));


            dp[x] = new DataPoint(x, alphabetcount);
        }
        return dp;
    }

    private int charCount(String text, char c)
    {
        int count = 0;

        for(int i = 0; i < text.length(); i++)
            if(text.charAt(i) == c)
                count++;

        return count;
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
