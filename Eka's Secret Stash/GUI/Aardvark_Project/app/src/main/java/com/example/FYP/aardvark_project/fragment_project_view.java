package com.example.FYP.aardvark_project;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.view.View;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.FYP.aardvark_project.Database.DatabaseFramework;
import com.example.FYP.aardvark_project.kryptoTools.*;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class fragment_project_view extends Fragment {

    /**Constants*/
    private final int INITIAL_CAESAR_SEEKBAR_VALUE = 26;

    /**Project variables*/
    private App_Framework framework;

    private TextView cipherTextView;

    private List<String> changeHistory = new ArrayList<>();

    private String projectTitle = new String();
    private String projectID = new String();
    private String cipherText = new String();
    private String originalCipherText = new String();

    /**Caesar shift cipher*/
    private Button caesar_shift_button;
    private SeekBar caesarSeekBar;
    private TextView indicator;

    private int shiftCipherBy = 0;
    private String beforeShift = new String();

    /**SubstitutionCipher*/
    private Spinner charASpinner;
    private Spinner charBSpinner;
    private Button charSubButton;
    private char charA;
    private char charB;

    private String beforeSub = new String();

    /**undo button and reset button*/
    private Button undoButton;
    private Button resetButton;

    /**database*/
    private DatabaseFramework database;

    /**Tab related varibles*/
    private View view;
    private FragmentActivity fragmentActivity;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private GeneralTextInput generalTextInput;
    private FrameLayout generalTextInputLayout;

    /**Misc variables*/
    private FrameLayout layoutSub;
    private FrameLayout layoutShift;

    private Button frequencyAnalysisButton;
    private Button ICFrequencyButton;

    private ViewFlipper slidingUpPanelViewFlipper; //use to change included xml view

    private Button slidingUpPanelToolsButton; // Button to access list of tools

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /**Setup this fragment view*/
        view = inflater.inflate(R.layout.fragment_project_view_main, container, false);
        fragmentActivity = this.getActivity(); //This is important: use this to access activity related functions

        getCompositeKeys();//gets ID and title, useful for looking up for related data from the database

        framework= new App_Framework(view.getContext(), true);
        database = new DatabaseFramework(view.getContext());

        setCipherTextView();

        getCipherTextFromDB();

        setShiftTool();
        setSubstitutionTool();
        setTranspoTools();

        originalCipherText = changeHistory.get(0); //sets the original cipher text to the very first entry in the change history

        setUndoButton();
        setResetButton();

        setSlidingUpPanel();

        setAnalysisTool();
        setLetterFrequencyGraph();
        setPeriodFrequencyGraph();

        setSaveButton();

        generalTextInput = new GeneralTextInput(view.findViewById(R.id.include_general));

        setInitialVisibility();

        return view;
    }

    /**Sets which view is being shown or hide during this activity creation*/
    private void setInitialVisibility()
    {
        layoutSub = view.findViewById(R.id.subLayout);
        layoutShift = view.findViewById(R.id.shiftLayout);
        generalTextInputLayout = view.findViewById(R.id.generalInputLayout);

        layoutSub.setVisibility(View.VISIBLE);
        layoutShift.setVisibility(View.GONE);
        generalTextInputLayout.setVisibility(View.GONE);

    }

    /**
     * This are the functions for tools that only needs an input text and one (or two) button to apply
     * */
    private void generalTextInputAppear(String hint, String positiveText, String negativeText, OnClickListener positiveListener, OnClickListener negativeListener)
    {
        generalTextInputLayout.setVisibility(View.VISIBLE);
        layoutSub.setVisibility(View.GONE);
        layoutShift.setVisibility(View.GONE);

        generalTextInput.positiveButtonVisible();
        generalTextInput.negativeButtonVisible();

        generalTextInput.inputAcceptText();

        generalTextInput.clearInput();
        generalTextInput.setHint(hint);
        generalTextInput.setPositiveButtonText(positiveText);
        generalTextInput.setNegativeButtonText(negativeText);
        generalTextInput.setPositiveButtonListener(positiveListener);
        generalTextInput.setNegativeButtonListener(negativeListener);
    }
    private void generalTextInputAppear(String hint, String positiveText, OnClickListener positiveListener)
    {
        generalTextInputLayout.setVisibility(View.VISIBLE);
        layoutSub.setVisibility(View.GONE);
        layoutShift.setVisibility(View.GONE);

        generalTextInput.positiveButtonVisible();
        generalTextInput.negativeButtonGone();

        generalTextInput.clearInput();
        generalTextInput.setHint(hint);
        generalTextInput.setPositiveButtonText(positiveText);
        generalTextInput.setPositiveButtonListener(positiveListener);
    }

    /**
     * Composite keys = the keys used to identify this project (ID, title)
     * */
    private void getCompositeKeys()
    {
        this.projectTitle = getArguments().getString("title");
        this.projectID = getArguments().getString("id");
    }

    /**Functions for the sliding up panel*/
    private void setSlidingUpPanel()
    {
        ImageView panelIndicator = view.findViewById(R.id.panel_arrow_indicator);
        View tempView = view.findViewById(R.id.sliding_up_panel_content_tools_include);
        ImageView panelIndicator2 = tempView.findViewById(R.id.panel_arrow_indicator);

        slidingUpPanelLayout = view.findViewById(R.id.root_slide_up_panel);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    panelIndicator.setImageResource(R.mipmap.ic_arrow_drop_down_circle_black_24dp); //change indicator to down arrow
                    panelIndicator2.setImageResource(R.mipmap.ic_arrow_drop_down_circle_black_24dp); //change indicator to down arrow
                }
                else if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                {
                    panelIndicator.setImageResource(R.mipmap.ic_arrow_drop_up_black_24dp); //change indicator to up arrow
                    panelIndicator2.setImageResource(R.mipmap.ic_arrow_drop_up_black_24dp); //change indicator to up arrow
                }
            }
        });

        setSlidingUpPanelViewFlipper();
        slidingUpPanelDisplayTools();
    }
    private void setSlidingUpPanelViewFlipper()
    {
        View slidingUpGeneralView = view.findViewById(R.id.sliding_up_panel_content_include);
        View slidingUpToolsView = view.findViewById(R.id.sliding_up_panel_content_tools_include);

        slidingUpPanelViewFlipper = view.findViewById(R.id.slidingUpPanelViewFlipper);

        slidingUpPanelToolsButton = slidingUpGeneralView.findViewById(R.id.button_crypto_tools);
        slidingUpPanelToolsButton.setOnClickListener(view -> slidingUpPanelDisplayTools());
        Button slidingUpPanelBackButton = slidingUpToolsView.findViewById(R.id.slidingUpPanelBackButton);
        slidingUpPanelBackButton.setOnClickListener(view -> slidingUpPanelDisplayExtra());
    }

    private void collapsePanel()
    {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }
    private void expandPanel()
    {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    private void changeCipherIcon(int iconID)
    {
        ImageView img = view.findViewById(R.id.sliding_up_panel_cipher_icon);
        img.setImageResource(iconID);
    }

    private void slidingUpPanelDisplayTools()
    {
        slidingUpPanelViewFlipper.setInAnimation(view.getContext(), R.anim.anim_in_from_left);
        slidingUpPanelViewFlipper.setOutAnimation(view.getContext(), R.anim.anim_out_to_right);
        slidingUpPanelViewFlipper.setDisplayedChild(1);
    }

    private void slidingUpPanelDisplayExtra()
    {
        slidingUpPanelViewFlipper.setInAnimation(view.getContext(), R.anim.anim_in_from_right);
        slidingUpPanelViewFlipper.setOutAnimation(view.getContext(), R.anim.anim_out_to_left);
        slidingUpPanelViewFlipper.setDisplayedChild(0);
    }

    /**
     * Get cipher text data from the database.
     * This function also splits every cipher text changes and store it into
     * change history array list
     * */
    private void getCipherTextFromDB()
    {
        String ctext = database.getCipherText(projectID, projectTitle);
        String[] split = ctext.split("\\|"); //split and add to change history

        changeHistory = new ArrayList<>(Arrays.asList(split));
        cipherText = changeHistory.get(changeHistory.size() - 1);
        cipherTextView.setText(cipherText);
    }

    private void updateCipherTextToDB()
    {
        String data = new String(); //process the stuff from changeHistory

        for(int i = 0; i < changeHistory.size(); i++)
        {
            data += changeHistory.get(i);
            if(i < changeHistory.size() - 1)
                data += "|";
        }
        database.updateCipherText(projectID, projectTitle, data);
    }

    private void setCipherTextView()
    {
        /**SET UP THE CIPHER TEXT VIEW AREA*/
        cipherTextView = view.findViewById(R.id.project_view_cipher_text);
        if(framework.isDarkTheme())
            cipherTextView.setTextColor(getResources().getColor(R.color.dark_primaryTextColor));
        else
            cipherTextView.setTextColor(getResources().getColor(R.color.primaryTextColor));
    }

    private void setSaveButton()
    {
        /**SET UP SAVE BUTTON*/
        Button saveButton = view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(view -> {
            updateCipherTextToDB();
            framework.system_message_small("Progress saved");
        });
    }

    private void setUndoButton()
    {
        undoButton = view.findViewById(R.id.button_undo);
        undoButton.setOnClickListener(view -> undo());
    }

    private void setResetButton()
    {
        resetButton = view.findViewById(R.id.button_reset);
        resetButton.setOnClickListener(view -> reset());
    }

    private void setAnalysisTool()
    {
        Button graphButtonPopup = view.findViewById(R.id.button_graphPopup);
        graphButtonPopup.setOnClickListener(view -> {
            Intent intent = new Intent(fragmentActivity, Activity_graph_Analysis.class);
            intent.putExtra("cipherText", cipherText);

            startActivity(intent);
            collapsePanel();
        });
    }

    private void setLetterFrequencyGraph()
    {
        frequencyAnalysisButton = view.findViewById(R.id.button_graph_frequency);
        frequencyAnalysisButton.setOnClickListener(view -> {
            Intent intent = new Intent(fragmentActivity, Activity_graph_frequency_letter.class);
            intent.putExtra("cipherText", cipherText);
            startActivity(intent);
            collapsePanel();
        });
    }

    private void setPeriodFrequencyGraph()
    {
        ICFrequencyButton = view.findViewById(R.id.frequency_graph_period);
        ICFrequencyButton.setOnClickListener(view -> {
            Intent intent = new Intent(fragmentActivity, Activity_graph_frequency_period.class);
            intent.putExtra("cipherText", cipherText);
            startActivity(intent);
        });
        collapsePanel();
    }

    private void undo() //undo the cipher text into its previous state
    {
        if(changeHistory.size() <= 1)
        {
            framework.system_message_small("Maximum undo attempt reached");
            caesarSeekBar.setProgress(INITIAL_CAESAR_SEEKBAR_VALUE);
        }

        else if(!cipherText.equals(originalCipherText) && !changeHistory.isEmpty() && changeHistory.size() >1) //because the first entry == originalCipherText
        {
            changeHistory.remove(changeHistory.size()-1); //remove the latest entry
            this.cipherText = changeHistory.get(changeHistory.size()-1);
            cipherTextView.setText(cipherText);
        }
    }

    private void reset() //reset the cipher text into its original state
    {
        if(cipherText.equals(originalCipherText))
            framework.system_message_small("cipher text is already at its original state");

        else if(!cipherText.equals(originalCipherText) && !changeHistory.isEmpty())
        {
            this.cipherText = originalCipherText;
            cipherTextView.setText(cipherText);
            changeHistory.clear(); //remove all items from the arraylist
        }

        caesarSeekBar.setProgress(INITIAL_CAESAR_SEEKBAR_VALUE);
    }

    /**GUI SETUP*/
    /**for all functions that changes the value of variable cipherText, call function "refresh()" afterwards to refresh cipher text view on the GUI
     * example can be seen in functions "doShiftLeft()" and "doShiftRight()"
     * */
    private void setShiftTool()
    {
        View shiftView = view.findViewById(R.id.include_shift); //gets from the "include" view type in the xml file

        SeekBar.OnSeekBarChangeListener caesarSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                shiftCipherBy = (seekBar.getProgress() - INITIAL_CAESAR_SEEKBAR_VALUE);

                if(shiftCipherBy < 0)
                {
                    doShiftLeft();
                    indicator.setText("Current Shift: <" + ((seekBar.getProgress() - 26) * -1));
                }
                else
                {
                    doShiftRight();
                    indicator.setText("Current Shift: >" + (seekBar.getProgress() - 26));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                refresh();
                beforeShift = cipherText;
            }
        };

        caesarSeekBar = shiftView.findViewById(R.id.seekBar_caesar);
        indicator = shiftView.findViewById(R.id.seekBar_indicator);

        caesarSeekBar.setMax(52);
        caesarSeekBar.setProgress(INITIAL_CAESAR_SEEKBAR_VALUE);
        caesarSeekBar.setOnSeekBarChangeListener(caesarSeekBarListener);

        indicator.setText("Current Shift: " + (caesarSeekBar.getProgress() - INITIAL_CAESAR_SEEKBAR_VALUE));

        List<Integer> content = new ArrayList<>();
        for(int i = 0; i < 26; i++)
            content.add(i);

        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        caesar_shift_button = view.findViewById(R.id.button_shiftPopup);
        caesar_shift_button.setOnClickListener(view -> {

            beforeShift = cipherText;

            layoutShift.setVisibility(View.VISIBLE);
            layoutSub.setVisibility(View.GONE);
            generalTextInputLayout.setVisibility(View.GONE);

            collapsePanel();

            changeCipherIcon(R.mipmap.shift);
        });
    }

    /**
     * Cipher calculation functions
     * */

    private void doShiftLeft()
    {
        framework.format(beforeShift); //sets framework.original_text to beforeShift
        try
        {
            framework.setMODIFIED_TEXT(new Shift().decrypt(framework.getMODIFIED_TEXT(), Integer.toString(Math.abs(shiftCipherBy))));
            cipherText = framework.displayModifiedString();
        }catch(InvalidKeyException e)
        {
            framework.system_message_small(e.getMessage());
        }
    }

    private void doShiftRight()
    {
        framework.format(beforeShift); //sets framework.original_text to beforeShift
        try
        {
            framework.setMODIFIED_TEXT(new Shift().encrypt(framework.getMODIFIED_TEXT(), Integer.toString(Math.abs(shiftCipherBy))));
            cipherText = framework.displayModifiedString();
        }catch(InvalidKeyException e)
        {
            framework.system_message_small(e.getMessage());
        }
    }

    private void setSubstitutionTool()
    {
        Button stringSubButton;

        View substitutionView = view.findViewById(R.id.include_sub); //gets view from "include" element in xml file

        Character[] alphabets = {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        /**listener for charA spinner view*/
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

        /**listener for charB spinner view*/
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

        OnClickListener charSubButtonListener = v -> {
            if((charA != '-') || (charB != '-'))
            {
                beforeSub = originalCipherText;
                doSubstitution(charA, charB);
                refresh();
            }
        };

        OnClickListener stringSubButtonListener = (View view) -> {
            collapsePanel();
            changeCipherIcon(R.mipmap.substitution);
            generalTextInputAppear("String substitution key", "Encrypt", "Despacito", view1 -> {
                doSubstitution(generalTextInput.getInput(), true);
                refresh();
            }, view12 -> {
                doSubstitution(generalTextInput.getInput(), true);
                refresh();
        });
        };

        /**Setup spinner for both charA and charB*/
        charASpinner = substitutionView.findViewById(R.id.spinner_charA);
        charBSpinner = substitutionView.findViewById(R.id.spinner_charB);

        List<Character> content = new ArrayList<Character>(Arrays.asList(alphabets)); //assign 'alphabets' array as a list data type

        /**Setup spinner for charA and char B*/
        ArrayAdapter<Character> spinnerAdapterA = new ArrayAdapter<Character>(substitutionView.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charASpinner.setAdapter(spinnerAdapterA);

        ArrayAdapter<Character> spinnerAdapterB = new ArrayAdapter<Character>(substitutionView.getContext(), android.R.layout.simple_spinner_item, content);
        spinnerAdapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        charBSpinner.setAdapter(spinnerAdapterB);

        charASpinner = substitutionView.findViewById(R.id.spinner_charA);
        charBSpinner = substitutionView.findViewById(R.id.spinner_charB);
        charASpinner.setOnItemSelectedListener(charAListener);
        charBSpinner.setOnItemSelectedListener(charBListener);

        /**Setup button for SubstitutionCipher by char*/
        charSubButton = substitutionView.findViewById(R.id.button_charSubstitution);
        charSubButton.setOnClickListener(charSubButtonListener);

        /**Setup button for SubstitutionCipher by String*/
        stringSubButton = view.findViewById(R.id.button_stringSubstitution);
        stringSubButton.setOnClickListener(stringSubButtonListener);

        /**Setup button in the sliding up panel*/
        Button substitutionButton = view.findViewById(R.id.button_subPopup);
        substitutionButton.setOnClickListener(view -> {

            layoutSub.setVisibility(View.VISIBLE);
            layoutShift.setVisibility(View.GONE);
            generalTextInputLayout.setVisibility(View.GONE);

            collapsePanel();
            changeCipherIcon(R.mipmap.substitution);
        });

    }

    private void doSubstitution(String stringKey, boolean encrypt)
    {
        //TODO() ENCRYPT OR DECRYPT ????
        framework.format(cipherText);
        try
        {
            if(encrypt)
                framework.setMODIFIED_TEXT(new Substitution().encrypt(framework.getMODIFIED_TEXT(), stringKey));
            else
                framework.setMODIFIED_TEXT(new Substitution().decrypt(framework.getMODIFIED_TEXT(), stringKey));

            cipherText = framework.displayModifiedString();
        }catch(InvalidKeyException e)
        {
            framework.system_message_small(e.getMessage());
        }
    }

    private void doSubstitution(char a, char b) //replace charA with charB
    {
        framework.format(cipherText);
        framework.setMODIFIED_TEXT(new Substitution().byCharacter(a, b, framework.getMODIFIED_TEXT(), beforeSub));
        cipherText = framework.displayModifiedString();
    }

    private void setTranspoTools()
    {
        Button TranspoButton = view.findViewById(R.id.button_normalTranspo);

        TranspoButton.setOnClickListener(view -> {

            collapsePanel();
            changeCipherIcon(R.mipmap.transposition);
            generalTextInputAppear("Columnar TranspositionCipher Key", "Encrypt", "Decrypt",
                    view1 -> {
                doTranspoEncrypt(generalTextInput.getInput());
                refresh();

                }, view2 -> {
                doTranspoDecrypt(generalTextInput.getInput());
                refresh();
                });
        });
    }

    private void doTranspoEncrypt(String key)
    {
        framework.format(cipherText);
        try
        {
            framework.setMODIFIED_TEXT(new Transposition().encrypt(framework.getMODIFIED_TEXT(), key));
            cipherText = framework.displayModifiedString();
        }catch(InvalidKeyException e)
            {framework.system_message_small(e.getMessage());}
    }
    private void doTranspoDecrypt(String key)
    {
        framework.format(cipherText);
        try
        {
            framework.setMODIFIED_TEXT(new Transposition().decrypt(framework.getMODIFIED_TEXT(), key));
            cipherText = framework.displayModifiedString();
        }catch(InvalidKeyException e)
            {framework.system_message_small(e.getMessage());}
    }

    private void refresh()//refreshes the cipher text view
    {
        changeHistory.add(cipherText);
        cipherTextView.setText(cipherText);
    }

    protected String getCipherText()
    {
        return this.cipherText;
    }
    protected String getOriginalCipherText()
    {
        return this.originalCipherText;
    }
/**
 * THIS IS A CLASS FOR GENERAL INPUT TEXT IN THE PROJECT VIEW
 * */
    class GeneralTextInput
    {
        View view;
        Button positiveButton;
        Button negativeButton;
        EditText textInput;

        public GeneralTextInput(View parentView)
        {
            this.view = parentView;
            this.positiveButton = view.findViewById(R.id.general_positive_button);
            this.negativeButton = view.findViewById(R.id.general_negative_button);
            this.textInput = view.findViewById(R.id.general_input);
        }

        /**Initialize input type*/
        protected void inputAcceptText()
        {
            this.textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        protected void inputAcceptNumbers() {this.textInput.setInputType(InputType.TYPE_CLASS_NUMBER);}

        /**Setters and getters*/
        protected void setPositiveButtonText(String s)
        {
            this.positiveButton.setText(s);
        }
        protected void setNegativeButtonText(String s)
        {
            this.negativeButton.setText(s);
        }
        protected void setHint(String s)
        {
            this.textInput.setHint(s);
        }
        protected void setPositiveButtonListener(OnClickListener ocl) {this.positiveButton.setOnClickListener(ocl);}
        protected void setNegativeButtonListener(OnClickListener ocl) {this.negativeButton.setOnClickListener(ocl);}

        protected void clearInput()
        {
            this.textInput.setText("");
        }

        protected String getInput()
        {
            return this.textInput.getText().toString();
        }

        protected void positiveButtonVisible()
        {
            this.positiveButton.setVisibility(View.VISIBLE);
        }
        protected void negativeButtonVisible()
        {
            this.positiveButton.setVisibility(View.VISIBLE);
        }
        protected void negativeButtonGone()
        {
            this.negativeButton.setVisibility(View.GONE);
        }
    }
}
