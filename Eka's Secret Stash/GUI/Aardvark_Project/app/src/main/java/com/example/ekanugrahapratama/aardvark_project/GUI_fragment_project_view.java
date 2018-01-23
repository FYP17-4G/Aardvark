package com.example.ekanugrahapratama.aardvark_project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.ekanugrahapratama.aardvark_project.Database.DatabaseFramework;
import com.example.ekanugrahapratama.aardvark_project.analysisTools.CryptoAnalysis;
import com.example.ekanugrahapratama.aardvark_project.kryptoTools.*;

public class GUI_fragment_project_view extends Fragment {

    private App_Framework framework;

    private String projectTitle = new String();
    private String projectID = new String();

    /**[VARIABLE SECTION]*/
    //Project cipher variables
    private List<String> changeHistory = new ArrayList<>();
    private String cipherText = new String();
    private String originalCipherText = new String();
    private TextView cipherTextView;

    private String beforeShift = new String();

    //Analysis variables
    private CryptoAnalysis cryptoAnalysis;

    /**Variables for Krypto tools*/
    //CAESAR CIPHER
    private SeekBar caesarSeekBar;
    private TextView indicator;
    private int shiftCipherBy = 0;

    private Button caesar_popup_button;

    //SUBSTITUTION
    private Spinner charASpinner; //Replace charA with charB
    private Spinner charBSpinner;
    private Button charSubButton;
    private char charA;
    private char charB;

    private String beforeSub = new String();

    //undo button and reset button
    private Button undoButton;
    private Button resetButton;

    //database
    private DatabaseFramework database;

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

        //set up the database
        database = new DatabaseFramework(view.getContext());

        /**SET UP THE CIPHER TEXT VIEW AREA*/
        cipherTextView = (TextView) view.findViewById(R.id.project_view_cipher_text);
        cipherTextView.setTextColor(Color.BLACK);

        getCipherTextFromDB();

        //SET UP TOOLS FOR THE PROJECT
        setCaesarTool(); //shift cipher
        setSubstitutionTool();
        setTranspoTools();

        //SET OTHER TOOLS HERE<...>

        //originalCipherText = cipherText;
        originalCipherText = changeHistory.get(0); //since the originalCipherText will always be at the first entry
        //changeHistory.add(originalCipherText);

        setUndoButton();
        setResetButton();

        setGraph();

        /**SET UP SAVE BUTTON*/
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCipherTextToDB();
                framework.system_message_small("Progress saved");
            }
        });

        return view;
    }

    private void getCipherTextFromDB()
    {
        //this.cipherText = framework.clean(framework.getTextFromFile(this.projectID + this.projectTitle + "cipherTextOriginal.txt"));

         String ctext = database.getCipherText(projectID, projectTitle);
        //split and add to change history
        String[] split = ctext.split("\\|");

        changeHistory = new ArrayList<>(Arrays.asList(split));

        cipherText = changeHistory.get(changeHistory.size() - 1);
        //cipherText = ctext;
        cipherText = framework.format(cipherText);
        cipherTextView.setText(cipherText);
    }

    private void updateCipherTextToDB()
    {
        //process the stuff from changeHistory
        String data = new String();

        for(int i = 0; i < changeHistory.size(); i++)
        {
            data += changeHistory.get(i);
            if(i < changeHistory.size() - 1)
                data += "|";
        }
        database.updateCipherText(projectID, projectTitle, data);
    }

    public String getCipherText()
    {
        return this.cipherText;
    }

    private void setGraph()
    {
        Button graphButtonPopup = view.findViewById(R.id.button_graphPopup);
        graphButtonPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DO TRANSITION
                Intent intent = new Intent(fragmentActivity, GUI_fragment_graph.class);
                intent.putExtra("cipherText", cipherText);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentActivity, cipherTextView, ViewCompat.getTransitionName(cipherTextView));
                startActivity(intent, options.toBundle());
            }
        });
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
            cipherTextView.setText(framework.format(cipherText));
        }
    }

    private void reset() //reset the cipher text into its original state
    {
        if(this.cipherText.equals(this.originalCipherText))
            framework.system_message_small("cipher text is already at its original state");

        else if(!this.cipherText.equals(this.originalCipherText) && !changeHistory.isEmpty())
        {
            this.cipherText = originalCipherText;
            cipherTextView.setText(framework.format(cipherText));
            changeHistory.clear(); //remove all items from the arraylist
        }
    }

    private void refresh()//refreshes the cipher text view
    {
        changeHistory.add(cipherText);
        cipherTextView.setText(cipherText);
    }

    /**GUI SETUP*/
    /**for all functions that changes the value of variable cipherText, call function "refresh()" afterwards to refresh cipher text view on the GUI
     * example can be seen in functions "doShiftLeft()" and "doShiftRight()"
     * */
    private void setCaesarTool()
    {
        View shiftView = getLayoutInflater().inflate(R.layout.pop_shift_cipher, null);


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
        Button stringSubButton;

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
                    beforeSub = cipherText;
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
                        doSubstitution(framework.popup_getInput());
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
        stringSubButton = (Button) view.findViewById(R.id.button_stringSubstitution);
        stringSubButton.setOnClickListener(stringSubButtonListener);



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
        cipherText = new Substitution().byCharacter(a, b, cipherText, beforeSub);
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
}
