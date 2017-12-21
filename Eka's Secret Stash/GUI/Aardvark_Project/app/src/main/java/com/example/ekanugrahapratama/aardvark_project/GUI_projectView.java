package com.example.ekanugrahapratama.aardvark_project;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GraphView;

import com.example.ekanugrahapratama.aardvark_project.analysisTools.CryptoAnalysis;
import com.example.ekanugrahapratama.aardvark_project.kryptoTools.CalculateIC;

public class GUI_projectView extends AppCompatActivity {

    App_Framework framework = new App_Framework(this);

    //TODO(***) Declare variables here
    /**[VARIABLE SECTION]*/
    //Project cipher variables
    String projectUniqueID = new String();
    private String projectTitle = new String();
    private String cipherText = new String();
    private String cipherTextWithCurrentPeriod = new String();
    private String originalCipherText = new String();
    private TextView cipherTextView;

    //Analysis variables
    private CryptoAnalysis cryptoAnalysis;

    //Graph variables
    private GraphView graph;
    private TextView graphPeriodIndicator;
    private LineGraphSeries<DataPoint> cipherTextSeries = new LineGraphSeries<DataPoint>(); //USE THIS DATA SET FOR PLOTTING cipherText only!!!
    private LineGraphSeries<DataPoint> periodCipherTextSeries = new LineGraphSeries<DataPoint>(); //USE THIS DATA SET EXCLUSIVELY FOR CALCULATING GRAPH OF PERIOD N!!!
    private SeekBar graphSeekBar;

    /**Variables for Krypto tools*/
    //CAESAR CIPHER
    //private ShiftCipher caesarCipher = new ShiftCipher();

    private SeekBar caesarSeekBar;
    TextView indicator;
    private int shiftCipherBy = 0;

    private Button caesarShiftRight;
    private Button caesarShiftLeft;

    //IC CALCULATION
    protected CalculateIC ic;
    private TextView cipherICTV;
    private double cipherIC = 0;

    //COLUMNAR TRANSPOSITION
    private Button cTranspo_button;

    //RECTANGULAR TRANSPOSITION
    private Button rTranspo_button;

    //SUBSTITUTION
    private Spinner charASpinner; //Replace charA with charB
    private Spinner charBSpinner;
    private Button charSubButton;
    private char charA;
    private char charB;

    private Button stringSubButton;
    private String stringSubValue;


    //<...>

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        //ACCESS THE PASSED PARAMETERS FROM GUI_adaptr.java

        projectUniqueID = getIntent().getStringExtra("project_view_unique_ID"); //USE THIS LATRE
        this.projectTitle = getIntent().getStringExtra("project_view_title");
        setTitle(projectTitle);

        //SET UP TOOLS FOR THE PROJECT
        setCaesarTool(); //shift cipher
        setSubstitutionTool();
        setTranspoTools();

        //SET OTHER TOOLS HERE<...>

        getCipherTextFromFile();/**Gets cipher text from a text file, make this function displays FILE BROWSER POPUP*/

        //set IC related function
        cipherICTV = (TextView) findViewById(R.id.cipherICTextView);
        ic = new CalculateIC();
        calculateCipherIC();
        calculateCipherFreq();

        graph = (GraphView) findViewById(R.id.graph_lot);
        plotGraph();
        setICPeriodTool();
    }

    /**Misc Functions*/
    //TODO(me) get content from HARDCODED text file, dont forget to change this when the database is up
    //TEST_CIPHER.txt is inside "assets" folder. DELETE IT LATER AND CHANGE IT ACCORDINGLY
    private void getCipherTextFromFile()
    {
        BufferedReader fileIn;
        String line;

        try
        {
            fileIn = new BufferedReader(new InputStreamReader(getAssets().open("TEST_CIPHER.txt")));
            while((line = fileIn.readLine()) != null)
            {
                System.out.println(">>>> " + line);
                cipherText += line;
                cipherTextView.append(line+"\n");
            }

            fileIn.close();
        }catch(IOException e)
        {}catch(NullPointerException n)
        {
            System.out.println("[ERROR NULL POINTER EXCEPTION]");
            System.exit(404);
        }
    }

    //This will convert given String to the same string but without whitespace
    protected String stringNoWhiteSpace(String cText)
    {
        String temp = new String();

        for(int i = 0; i < cText.length(); i++)
            if(cText.charAt(i) != ' ')
                temp+=cText.charAt(i);

        return temp;
    }


    //TODO(me) FINISH THESE: record(), undo(), reset()
    //this records the actions of the user, use this for doing stuff like undo action
    //this method can be used as "write to file"
    private void record(String recordString)
    {

    }

    private void undo() //undo the cipher text into its previous state
    {

    }

    private void reset() //reset the cipher text into its original state
    {

    }

    private void refresh()//refreshes the cipher text view
    {
        cipherTextView.setText(cipherText);
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

        caesarSeekBar = (SeekBar) findViewById(R.id.seekBar_caesar);
        indicator = (TextView)findViewById(R.id.seekBar_indicator);
        indicator.setText("Shift by: 0");
        caesarSeekBar.setMax(26);
        caesarSeekBar.setProgress(0);
        caesarSeekBar.setOnSeekBarChangeListener(caesarSeekBarListener);

        cipherTextView = (TextView) findViewById(R.id.project_view_cipher_text);
        caesarShiftLeft = (Button) findViewById(R.id.button_caesarShiftL);
        caesarShiftRight = (Button) findViewById(R.id.button_caesarShiftR);

        caesarShiftLeft.setOnClickListener(shiftListener);
        caesarShiftRight.setOnClickListener(shiftListener);

        List<Integer> content = new ArrayList<>();
        for(int i = 0; i < 26; i++)
            content.add(i);

        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, content);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    private void doShiftLeft()
    {
        //TODO(***) Do Shift DECRYPTION here, assign result to variable "cipherText"
    }

    private void doShiftRight()
    {
        //TODO(***) Do Shift ENCRYPTION here, assign result to variable "ciphertext"
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
        charASpinner = (Spinner) findViewById(R.id.spinner_charA);
        charBSpinner = (Spinner) findViewById(R.id.spinner_charB);

        List<Character> content = new ArrayList<Character>(Arrays.asList(alphabets)); //assign 'alphabets' array as a list data type

        //set spinner for charA
        ArrayAdapter<Character> spinnerAdapterA = new ArrayAdapter<Character>(this, android.R.layout.simple_spinner_item, content);
        spinnerAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charASpinner.setAdapter(spinnerAdapterA);

        //set spinner for charB
        ArrayAdapter<Character> spinnerAdapterB = new ArrayAdapter<Character>(this, android.R.layout.simple_spinner_item, content);
        spinnerAdapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charBSpinner.setAdapter(spinnerAdapterB);

        charASpinner = (Spinner) findViewById(R.id.spinner_charA);
        charBSpinner = (Spinner) findViewById(R.id.spinner_charB);
        charASpinner.setOnItemSelectedListener(charAListener);
        charBSpinner.setOnItemSelectedListener(charBListener);

        /**Setup button for Substitution by char*/
        charSubButton = (Button) findViewById(R.id.button_charSubstitution);
        charSubButton.setOnClickListener(charSubButtonListener);

        /**Setup button for Substitution by String*/
        stringSubButton = (Button) findViewById(R.id.button_stringSubstitution);
        stringSubButton.setOnClickListener(stringSubButtonListener);

    }

        private void doSubstitution(String stringKey)
        {
            //TODO(***) Substitution by STRING, assign result to variable "cipherText"
        }

        private void doSubstitution(char a, char b) //replace charA with charB
        {
            //TODO(***) Substitution by CHAR, assign result to variable "cipherText"
        }

    private void setTranspoTools()
    {
        /**Columnar Transposition*/
        cTranspo_button = (Button) findViewById(R.id.button_cTranspo);
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
                        doTranspo_C();

                        refresh();
                    }
                };

                framework.popup_show("Columnar Transposition", "Enter key", ocl);
            }
        });


        /**Rectangular Transposition*/
        rTranspo_button = (Button) findViewById(R.id.button_rTranspo);
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
                        doTranspo_R();

                        refresh();
                    }
                };

                framework.popup_show("Columnar Transposition", "Enter key", ocl);
            }
        });

        /**Periodic Transposition*/
        //TODO(me) Set up interface for periodic transposition
    }

        private void doTranspo_C()
        {
            //TODO(***) Columnar Transposition, assign result to variable "cipherText"
        }
        private void doTranspo_R()
        {
            //TODO(***) Rectangular Transposition, assign result to variable "cipherText"
        }

    /**IC and FREQUENCY RELATED FUNCTIONS*/

    private void setICPeriodTool()
    {
        final SeekBar.OnSeekBarChangeListener graphPeriodListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                graphPeriodIndicator.setText("Period: "+ graphSeekBar.getProgress());

                if((graphSeekBar.getProgress()) != 0)
                {
                    cipherTextWithCurrentPeriod = getCipherTextPeriodOf(graphSeekBar.getProgress());
                    graphSeekBar.dispatchDisplayHint(graphSeekBar.getProgress());
                    rePlotGraph();
                }
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

        int MAX_PERIOD = 20; //TODO(me) find a way to make this more flexible (maybe allow the user to set their own maximum period)

        graphPeriodIndicator = (TextView) findViewById(R.id.period_indicator);
        graphPeriodIndicator.setText("Period: 0");

        graphSeekBar = (SeekBar) findViewById(R.id.seekBar_period);
        graphSeekBar.setMax(MAX_PERIOD);
        graphSeekBar.setProgress(0);
        graphSeekBar.setOnSeekBarChangeListener(graphPeriodListener);
    }

    private String getCipherTextPeriodOf(int periodValue)
    {
        //TODO(***) Get the cipherText String of period N
        String returnValue = "REPLACE LATER, skratta du florlar du";

        return returnValue;
    }

    private void rePlotGraph()
    {
        //remove the data sets and series
        cipherTextSeries = new LineGraphSeries<DataPoint>();
        periodCipherTextSeries = new LineGraphSeries<DataPoint>();
        graph.removeAllSeries();

        //re plot the graph
        plotGraph();

        if(!cipherTextWithCurrentPeriod.isEmpty())//if this variable is not empty
            plotGraph(cipherTextWithCurrentPeriod);
    }

    private void plotGraph() //use this to plot graph of whatever the value is in cipherText variable
    {
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        final int MAX_DATA_POINTS = 26;

        //fill series data
        for(int x = 0; x < MAX_DATA_POINTS; x++)
        {
            int alphabetcount = charCount(cipherText.toLowerCase(), alphabet[x].charAt(0));
            cipherTextSeries.appendData(new DataPoint(x, alphabetcount), true, MAX_DATA_POINTS);
        }

        //set up graph axis title
        graph.getGridLabelRenderer().setVerticalAxisTitle("Frequency");

        //set up x axis labels
        StaticLabelsFormatter staticLabels = new StaticLabelsFormatter(graph);
        staticLabels.setHorizontalLabels(alphabet);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabels);

        //set the line color
        cipherTextSeries.setColor(Color.BLUE);

        graph.addSeries(cipherTextSeries);
    }

    private void plotGraph(String textOfPeriod) //plot the graph for a given text, the value of the given text should be a string of period N
    {
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        final int MAX_DATA_POINTS = 26;

        //fill series data
        for(int x = 0; x < MAX_DATA_POINTS; x++)
        {
            int alphabetcount = charCount(textOfPeriod.toLowerCase(), alphabet[x].charAt(0));
            periodCipherTextSeries.appendData(new DataPoint(x, alphabetcount), true, MAX_DATA_POINTS);
        }

        //set the line color
        periodCipherTextSeries.setColor(Color.GRAY);
        graph.addSeries(periodCipherTextSeries);
    }

    private int charCount(String text, char c)
    {
        int count = 0;

            for(int i = 0; i < text.length(); i++)
                if(text.charAt(i) == c)
                    count++;

        return count;
    }

    private void calculateCipherIC()
    {
        cipherIC = getCipherIC(cipherText);
        cipherICTV.setText("IC: " + Double.toString(cipherIC));
    }

        //this is protected because we need it for suggestion system
        protected double getCipherIC(String cText)
        {
            return ic.getIC(stringNoWhiteSpace(cText));
        }


    private void calculateCipherFreq()
    {
        //TODO(***) Calculate Cipher Frequency
        /**maybe pass it into an arrayList (Declare it in VARIABLE SECTION up top), then I'll make the GUI after*/

        //TODO(me) setup GUI for displaying cipher
    }
    private void calculateCipherFreq(int begin, int end) //begin and end marks the index where it starts and where it finishes
    {
        //TODO(***) Calculate Cipher Frequency(with boundaries)
    }
}
