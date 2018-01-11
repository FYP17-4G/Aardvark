package com.example.ekanugrahapratama.aardvark_project;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    //TODO(***) Declare variables here
    /**[VARIABLE SECTION]*/
    //Project cipher variables
    private List<String> changeHistory = new ArrayList<>();
    private String cipherText = new String();
    private String cipherTextWithCurrentPeriod = new String();
    private String originalCipherText = new String();
    private TextView cipherTextView;

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

    private List<Double> cipherICWithPeriod = new ArrayList<>();

    /**Variables for Krypto tools*/
    //CAESAR CIPHER
    private SeekBar caesarSeekBar;
    private TextView indicator;
    private int shiftCipherBy = 0;

    private Button caesarShiftRight;
    private Button caesarShiftLeft;

    private ShiftCipher shiftCipher = new ShiftCipher();

    //COLUMNAR TRANSPOSITION
    private Button cTranspo_button;
    private ColumnarTransposition columnarTransposition;

    //RECTANGULAR TRANSPOSITION
    private Button rTranspo_button;
    private RectangularTransposition rectangularTransposition;

    private ColumnarTransposition cTranspo = new ColumnarTransposition();
    //SUBSTITUTION
    private Spinner charASpinner; //Replace charA with charB
    private Spinner charBSpinner;
    private Button charSubButton;
    private char charA;
    private char charB;

    private String stringSubValue;

    Substitute substitution;
    RectKeySubstitution rectSubstitution;

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

        framework= new App_Framework(view.getContext());

        /**SET UP THE CIPHER TEXT VIEW AREA*/
        cipherTextView = (TextView) view.findViewById(R.id.project_view_cipher_text);
        cipherTextView.setTextColor(Color.WHITE);

        //SET UP TOOLS FOR THE PROJECT
        setCaesarTool(); //shift cipher
        setSubstitutionTool();
        setTranspoTools();

        //SET OTHER TOOLS HERE<...>

        getCipherTextFromFile();/**Gets cipher text from a text file, make this function displays FILE BROWSER POPUP*/

        originalCipherText = cipherText; //TODO(me) FIND A WAY TO SET ORIGINAL CIPHER TEXT PROPERLY, SINCE THIS ONE IS JUST DIRECT ASSIGNING
        changeHistory.add(originalCipherText);

        //set IC related function
        cipherICTV = (TextView) view.findViewById(R.id.cipherICTextView);
        ic = new CalculateIC();

        calculateCipherIC(MAX_PERIOD);

        setGraph();
        rePlotGraph();

        setUndoButton();
        setResetButton();

        return view;
    }


    /**Misc Functions*/
    //TODO(me) get content from HARDCODED text file, dont forget to change this when the database is up
    //TEST_CIPHER.txt is inside "assets" folder. DELETE IT LATER AND CHANGE IT ACCORDINGLY
    private void getCipherTextFromFile()
    {

        /**IMPORTANT!!!! IF THE FILE IS NOT FOUND, THEN ASK THE USER TO GET THE FILE LOCATION*/

        BufferedReader fileIn;
        String line;

        try
        {
            fileIn = new BufferedReader(new InputStreamReader(fragmentActivity.getAssets().open("TEST_CIPHER.txt")));
            while((line = fileIn.readLine()) != null)
            {
                cipherText += line;
                cipherTextView.append(line+"\n");
            }

            fileIn.close();
        }catch(IOException e)
        {}catch(NullPointerException n)
        {
            System.out.println("[ERROR] @GUI_fragment_project_view: NullPtrException");
            System.exit(404);
        }
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

                indicator.setText("Shift by: " + caesarSeekBar.getProgress());
                caesarSeekBar.dispatchDisplayHint(caesarSeekBar.getProgress());
                shiftCipherBy = caesarSeekBar.getProgress();
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

        caesarSeekBar = (SeekBar) view.findViewById(R.id.seekBar_caesar);
        indicator = (TextView) view.findViewById(R.id.seekBar_indicator);
        indicator.setText("Shift by: 0");
        caesarSeekBar.setMax(26);
        caesarSeekBar.setProgress(0);
        caesarSeekBar.setOnSeekBarChangeListener(caesarSeekBarListener);

        caesarShiftLeft = (Button) view.findViewById(R.id.button_caesarShiftL);
        caesarShiftRight = (Button) view.findViewById(R.id.button_caesarShiftR);

        caesarShiftLeft.setOnClickListener(shiftListener);
        caesarShiftRight.setOnClickListener(shiftListener);

        List<Integer> content = new ArrayList<>();
        for(int i = 0; i < 26; i++)
            content.add(i);

        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    private void doShiftLeft()
    {
        //TODO(***) Do Shift DECRYPTION here, assign result to variable "cipherText", get key value from variable "shiftCipherBy"
        cipherText = shiftCipher.decrypt(cipherText.toLowerCase(), shiftCipherBy);
    }

    private void doShiftRight()
    {
        //TODO(***) Do Shift ENCRYPTION here, assign result to variable "ciphertext", get key value from variable "shiftCipherBy"
        cipherText = shiftCipher.encrypt(cipherText.toLowerCase(), shiftCipherBy);
    }

    private void setSubstitutionTool()
    {
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
        charASpinner = (Spinner) view.findViewById(R.id.spinner_charA);
        charBSpinner = (Spinner) view.findViewById(R.id.spinner_charB);

        List<Character> content = new ArrayList<Character>(Arrays.asList(alphabets)); //assign 'alphabets' array as a list data type

        //set spinner for charA
        ArrayAdapter<Character> spinnerAdapterA = new ArrayAdapter<Character>(view.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charASpinner.setAdapter(spinnerAdapterA);

        //set spinner for charB
        ArrayAdapter<Character> spinnerAdapterB = new ArrayAdapter<Character>(view.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charBSpinner.setAdapter(spinnerAdapterB);

        charASpinner = (Spinner) view.findViewById(R.id.spinner_charA);
        charBSpinner = (Spinner) view.findViewById(R.id.spinner_charB);
        charASpinner.setOnItemSelectedListener(charAListener);
        charBSpinner.setOnItemSelectedListener(charBListener);

        /**Setup button for Substitution by char*/
        charSubButton = (Button) view.findViewById(R.id.button_charSubstitution);
        charSubButton.setOnClickListener(charSubButtonListener);

        /**Setup button for Substitution by String*/
        //stringSubButton = (Button) view.findViewById(R.id.button_stringSubstitution);
        //stringSubButton.setOnClickListener(stringSubButtonListener);

    }

    private void doSubstitution(String stringKey)
    {
        //TODO(***) Substitution by STRING, assign result to variable "cipherText"
        //cipherText = substitution.str(cipherText, );
    }

    private void doSubstitution(char a, char b) //replace charA with charB
    {
        //TODO(***) Substitution by CHAR, assign result to variable "cipherText"
        cipherText = substitution.sub(a, b, cipherText.toLowerCase());
    }

    private void setTranspoTools()
    {
        /**Columnar Transposition*/
        cTranspo_button = (Button) view.findViewById(R.id.button_cTranspo);
        columnarTransposition = new ColumnarTransposition();

        cTranspo_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        doTranspo_C(framework.popup_getInput());
                        refresh();
                    }
                };

                framework.popup_getNumber_show("Columnar Transposition", "Enter key", ocl, 100); /**CHANGE INPUT LENGTH LIMIT*/
            }
        });


        /**Rectangular Transposition*/
        rTranspo_button = (Button) view.findViewById(R.id.button_rTranspo);
        rTranspo_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        doTranspo_R(framework.popup_getInput());
                        refresh();
                    }
                };

                framework.popup_getNumber_show("Rectangular Transposition", "Enter Number of columns", ocl, 1);
            }
        });

        /**Periodic Transposition*/
        //TODO(me) Set up interface for periodic transposition
    }

    private void doTranspo_C(String key)
    {
        //TODO(***) Columnar Transposition, assign result to variable "cipherText"
        List<List<Character>> list = new ArrayList<>();

        List<List<Character>> cipherTextResult = columnarTransposition.encrypt(cipherText, key, list);
        System.out.println(cipherTextResult.toString());

    }
    private void doTranspo_R(String key) //the key is integer
    {
        //TODO(***) Rectangular Transposition, assign result to variable "cipherText"
        int k = Integer.parseInt(key);//NEED TO ASSIGN INPUT LENGTH LIMIT IN THE POPUP INPUT FIELD

        //apply rectangular transposition from specified key
        cipherText = rectangularTransposition.encrypt(cipherText, k);
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

        graphPeriodIndicator = (TextView) view.findViewById(R.id.period_indicator);
        graphPeriodIndicator.setText("IC of every N letters: 0");

        graphSeekBar = (SeekBar) view.findViewById(R.id.seekBar_period);
        graphSeekBar.setMax(MAX_PERIOD);
        //graphSeekBar.setMin(2); //TODO()CHANGE API 24 TO 26 FOR THIS TO WORK
        graphSeekBar.setProgress(2);
        graphSeekBar.setOnSeekBarChangeListener(graphPeriodListener);

        graph = (GraphView) view.findViewById(R.id.graph_lot);

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

    private void calculateCipherFreq(int n)
    {

    }
}
