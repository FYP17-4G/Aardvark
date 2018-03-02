/**
 * Programmer: Eka Nugraha Pratama
 *
 * (This object is also instantiated in "Activity_Project_View")
 *
 * Contains the source code to set up the behavior and logic of Project view elements, sliding up view panel, and crypto tools
 * */

package com.example.FYP.aardvark_project.GUI;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.view.View;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.example.FYP.aardvark_project.Common.AppFramework;
import com.example.FYP.aardvark_project.Common.Project;
import com.example.FYP.aardvark_project.Database.DatabaseFramework;
import com.example.FYP.aardvark_project.Ciphers.BeaufortCipher;
import com.example.FYP.aardvark_project.Ciphers.BeaufortVariantCipher;
import com.example.FYP.aardvark_project.Ciphers.RectangularKeySubstitution;
import com.example.FYP.aardvark_project.Ciphers.Shift;
import com.example.FYP.aardvark_project.Ciphers.SubstitutionCipher;
import com.example.FYP.aardvark_project.Ciphers.TranspositionCipher;
import com.example.FYP.aardvark_project.Ciphers.TranspositionPeriodic;
import com.example.FYP.aardvark_project.Ciphers.VigenereCipher;
import com.example.FYP.aardvark_project.R;
import com.example.FYP.aardvark_project.Analytics.*;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class Fragment_project_view extends Fragment {


    /**Cipher Variables*/
    SubstitutionCipher substitutionCipher = new SubstitutionCipher();
    Shift shift = new Shift();
    TranspositionCipher transpositionCipher = new TranspositionCipher();
    TranspositionPeriodic transpositionPeriodic = new TranspositionPeriodic();
    RectangularKeySubstitution rectangularKeyTransposition = new RectangularKeySubstitution();
    BeaufortCipher beaufortCipher = new BeaufortCipher();
    BeaufortVariantCipher beaufortVariantCipher = new BeaufortVariantCipher();
    VigenereCipher vigenereCipher = new VigenereCipher();

    /**Constants*/
    private final int INITIAL_CAESAR_SEEKBAR_VALUE = 26;

    private final String SUBSTITUTION_CHARACTER = substitutionCipher.getName();
    private final String SHIFT_CIPHER = shift.getName();
    private final String TRANSPOSITION = transpositionCipher.getName();
    private final String TRANSPOSITION_PERIODIC = transpositionPeriodic.getName();
    private final String TRANSPOSITION_RECTANGULAR = rectangularKeyTransposition.getName();
    private final String BEAUFORT = beaufortCipher.getName();
    private final String BEAUFORT_VARIANT = beaufortVariantCipher.getName();
    private final String VIGENERE = vigenereCipher.getName();

    /**Project variables*/
    private AppFramework framework;

    private TextView cipherTextView;

    private List<String> changeHistory = new ArrayList<>();

    private String projectTitle = new String();
    private String projectID = new String();
    private String cipherText = new String();
    private String originalCipherText = new String();

    /**Caesar shift cipher*/
    private SeekBar shiftSeekBar;
    private TextView indicator;

    private int shiftCipherBy = 0;

    /**SubstitutionCipher*/
    private char charA;
    private char charB;

    /**database*/
    private DatabaseFramework database;

    /**Tab related varibles*/
    private View view;
    private FragmentActivity fragmentActivity; // fragmentActivity == context

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private GeneralTextInput generalTextInput;
    private FrameLayout generalTextInputLayout;

    /**Tool spinner*/
    private String[] spinnerList = {SUBSTITUTION_CHARACTER, SHIFT_CIPHER, TRANSPOSITION, TRANSPOSITION_PERIODIC, TRANSPOSITION_RECTANGULAR, BEAUFORT, BEAUFORT_VARIANT, VIGENERE};

    /**Block edit variables*/
    private int MAX_SEEKBAR = 20;

    private Switch showOriginalSwitch;

    private TextView spaceIndicator;
    private TextView lineIndicator;

    private SeekBar spaceSeekBar;
    private SeekBar lineSeekBar;

    private int space = 0;
    private int line = 0;

    /**Misc variables*/
    private FrameLayout layoutSub; //layout for substitution cipher input
    private FrameLayout layoutShift; //layout for shift cipher input

    private ViewFlipper slidingUpPanelViewFlipper; //use to change included xml view

    private String toolDescription = new String();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**Setup this fragment view*/
        view = inflater.inflate(R.layout.fragment_project_view_main, container, false);
        fragmentActivity = this.getActivity(); //This is important: use this to access activity related functions

        getCompositeKeys();//gets ID and title, useful for looking up for related data from the database

        framework= new AppFramework(view.getContext(), true);
        database = new DatabaseFramework(view.getContext());

        setCipherTextView();

        getCipherTextFromDB();

        setShiftTool();
        setSubstitutionTool();

        originalCipherText = changeHistory.get(0); //sets the original cipher text to the very first entry in the change history

        setUndoButton();

        setSlidingUpPanel();

        generalTextInput = new GeneralTextInput(view.findViewById(R.id.include_general));

        setInitialVisibility();

        setToolSpinner();

        return view;
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(seekBar.getId() == R.id.seekBar_space)
                space = spaceSeekBar.getProgress();
            else
                line = lineSeekBar.getProgress();

            spaceIndicator.setText("Characters per block: " + space);
            lineIndicator.setText("Blocks per line: " + line);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            refresh(cipherText, space, line, showOriginalSwitch.isChecked());
        }
    };

    private void setToolSpinner() {
        /**Set the tool spinner*/
        View toolSpinnerView = view.findViewById(R.id.sliding_up_panel_content_tools_include);
        Spinner toolSpinner = toolSpinnerView.findViewById(R.id.sliding_up_panel_tool_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();

                if(value.equals(SUBSTITUTION_CHARACTER)) {
                    layoutSub.setVisibility(View.VISIBLE);
                    layoutShift.setVisibility(View.GONE);
                    generalTextInputLayout.setVisibility(View.GONE);

                    toolDescription = substitutionCipher.getDescription();
                }
                else if(value.equals(TRANSPOSITION)){
                    generalTextInputAppear("Columnar TranspositionCipher Key", "Encrypt", "Decrypt",
                            view1 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doTranspoEncrypt(generalTextInput.getInput());
                                    refresh();
                                }

                            }, view2 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doTranspoDecrypt(generalTextInput.getInput());
                                    refresh();
                                }
                            });

                    toolDescription = transpositionCipher.getDescription();
                }
                else if(value.equals(TRANSPOSITION_PERIODIC)){
                    generalTextInputAppear("Periodic TranspositionCipher Key", "Encrypt", "Decrypt",
                            view1 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doTranspoPeriodicEncrypt(generalTextInput.getInput());
                                    refresh();
                                }

                            }, view2 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doTranspoPeriodicDecrypt(generalTextInput.getInput());
                                    refresh();
                                }
                            });

                    toolDescription = transpositionPeriodic.getDescription();
                }
                else if(value.equals(TRANSPOSITION_RECTANGULAR)){
                    generalTextInputAppear("Rectangular TranspositionCipher Key", "Encrypt", "Decrypt",
                            view1 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doTranspoRectangularEncrypt(generalTextInput.getInput());
                                    refresh();
                                }

                            }, view2 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doTranspoRectangularDecrypt(generalTextInput.getInput());
                                    refresh();
                                }
                            });

                    toolDescription = rectangularKeyTransposition.getDescription();
                }
                else if(value.equals(SHIFT_CIPHER)){
                    layoutShift.setVisibility(View.VISIBLE);
                    layoutSub.setVisibility(View.GONE);
                    generalTextInputLayout.setVisibility(View.GONE);

                    toolDescription = new Shift().getDescription();
                }
                else if(value.equals(BEAUFORT)){
                    generalTextInputAppear("Beaufort Key", "Encrypt", "Decrypt",
                            view1 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doBeaufortEncrypt(generalTextInput.getInput());
                                    refresh();
                                }

                            }, view2 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doBeaufortDecrypt(generalTextInput.getInput());
                                    refresh();
                                }
                            });

                    toolDescription = beaufortCipher.getDescription();
                }
                else if(value.equals(BEAUFORT_VARIANT)){
                    generalTextInputAppear("Beaufort Variant Key", "Encrypt", "Decrypt",
                            view1 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doBeaufortVariantEncrypt(generalTextInput.getInput());
                                    refresh();
                                }

                            }, view2 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doBeaufortVariantDecrypt(generalTextInput.getInput());
                                    refresh();
                                }
                            });

                    toolDescription = beaufortVariantCipher.getDescription();
                }
                else if(value.equals(VIGENERE)){
                    generalTextInputAppear("Vigenere Key", "Encrypt", "Decrypt",
                            view1 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doVigenereEncrypt(generalTextInput.getInput());
                                    refresh();
                                }

                            }, view2 -> {
                                if(!generalTextInput.inputEmpty()) {
                                    doVigenereDecrypt(generalTextInput.getInput());
                                    refresh();
                                }
                            });

                    toolDescription = vigenereCipher.getDescription();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        toolSpinner.setAdapter(dataAdapter);
    }

    /**Sets which view is being shown or hide during this activity creation*/
    private void setInitialVisibility() {
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
    private void generalTextInputAppear(String hint, String positiveText, String negativeText, OnClickListener positiveListener, OnClickListener negativeListener) {
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
    private void generalTextInputAppear(String hint, String positiveText, OnClickListener positiveListener) {
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
    private void getCompositeKeys() {
        this.projectTitle = getArguments().getString("title");
        this.projectID = getArguments().getString("id");
    }

    /**Functions for the sliding up panel*/
    private void setSlidingUpPanel() {
        ImageView panelIndicator = view.findViewById(R.id.panel_arrow_indicator);
        View cipherToolView = view.findViewById(R.id.sliding_up_panel_content_tools_include);
        ImageView panelIndicator2 = cipherToolView.findViewById(R.id.panel_arrow_indicator);
        View blockEditView = view.findViewById(R.id.sliding_up_panel_content_block_edit_include);
        ImageView panelIndicator3 = blockEditView.findViewById(R.id.panel_arrow_indicator);

        slidingUpPanelLayout = view.findViewById(R.id.root_slide_up_panel);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if(newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    panelIndicator.setImageResource(R.mipmap.ic_arrow_drop_down_circle_black_24dp); //change indicator to down arrow
                    panelIndicator2.setImageResource(R.mipmap.ic_arrow_drop_down_circle_black_24dp); //change indicator to down arrow
                    panelIndicator3.setImageResource(R.mipmap.ic_arrow_drop_down_circle_black_24dp); //change indicator to down arrow
                }
                else if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    panelIndicator.setImageResource(R.mipmap.ic_arrow_drop_up_black_24dp); //change indicator to up arrow
                    panelIndicator2.setImageResource(R.mipmap.ic_arrow_drop_up_black_24dp); //change indicator to up arrow
                    panelIndicator3.setImageResource(R.mipmap.ic_arrow_drop_up_black_24dp); //change indicator to up arrow
                }
            }
        });

        setSlidingUpPanelViewFlipper();
        slidingUpPanelDisplayMain();

        setAnalysisTool();
        setLetterFrequencyGraph();
        setPeriodFrequencyGraph();
    }

    private void setSlidingUpPanelViewFlipper() {
        View slidingUpGeneralView = view.findViewById(R.id.sliding_up_panel_content_include);
        View slidingUpToolsView = view.findViewById(R.id.sliding_up_panel_content_tools_include);
        View slidingUpPermutationView = view.findViewById(R.id.sliding_up_panel_content_block_edit_include);

        slidingUpPanelViewFlipper = view.findViewById(R.id.slidingUpPanelViewFlipper);

        /**Set Cipher tools view button*/
        Button slidingUpPanelToolsButton = slidingUpGeneralView.findViewById(R.id.button_crypto_tools);
        slidingUpPanelToolsButton.setOnClickListener(view -> slidingUpPanelDisplayTools());

        /**Set up back button in Tools view*/
        Button slidingUpPanelToolsBackButton = slidingUpToolsView.findViewById(R.id.slidingUpPanelBackButton);
        slidingUpPanelToolsBackButton.setOnClickListener(view -> slidingUpPanelDisplayMain());

        /**Set up cipher description button in Tools view*/
        ImageView cipherDescriptionImageView = slidingUpToolsView.findViewById(R.id.sliding_up_tool_info);
        cipherDescriptionImageView.setOnClickListener(view -> framework.system_message_popup("Cipher Description", toolDescription, "Got it"));

        /**Set permutation view button*/
        Button blockEditButton = view.findViewById(R.id.button_block_edit);
        blockEditButton.setOnClickListener(view -> slidingUpPanelDisplayPermutation());

        Button permutationBack = slidingUpPermutationView.findViewById(R.id.permutation_back);
        permutationBack.setOnClickListener(view -> slidingUpPanelDisplayMain());
        setPermutationSeekBars();
    }

    private void setPermutationSeekBars() {
        if(cipherText.length() < MAX_SEEKBAR)
            MAX_SEEKBAR = cipherText.length();

        showOriginalSwitch = view.findViewById(R.id.block_edit_show_original);
        showOriginalSwitch.setOnCheckedChangeListener((compoundButton, b) -> refresh(cipherText, space, line, b));

        spaceIndicator = view.findViewById(R.id.seekBar_space_indicator);
        lineIndicator = view.findViewById(R.id.seekBar_line_indicator);

        spaceSeekBar = view.findViewById(R.id.seekBar_space);
        lineSeekBar = view.findViewById(R.id.seekBar_line);
        spaceSeekBar.setProgress(space);
        lineSeekBar.setProgress(line);
        spaceSeekBar.setMax(MAX_SEEKBAR);
        lineSeekBar.setMax(MAX_SEEKBAR);
        spaceSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        lineSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void slidingUpPanelDisplayMain() {
        slidingUpPanelViewFlipper.setInAnimation(view.getContext(), R.anim.anim_in_from_right);
        slidingUpPanelViewFlipper.setOutAnimation(view.getContext(), R.anim.anim_out_to_left);
        slidingUpPanelViewFlipper.setDisplayedChild(0);
    }
    private void slidingUpPanelDisplayTools() {
        slidingUpPanelViewFlipper.setInAnimation(view.getContext(), R.anim.anim_in_from_left);
        slidingUpPanelViewFlipper.setOutAnimation(view.getContext(), R.anim.anim_out_to_right);
        slidingUpPanelViewFlipper.setDisplayedChild(1);
    }
    private void slidingUpPanelDisplayPermutation() {
        slidingUpPanelViewFlipper.setInAnimation(view.getContext(), R.anim.anim_in_from_left);
        slidingUpPanelViewFlipper.setOutAnimation(view.getContext(), R.anim.anim_out_to_right);
        slidingUpPanelViewFlipper.setDisplayedChild(2);
    }

    /**
     * Get cipher text data from the database.
     * This function also splits every cipher text changes and store it into
     * change history array list
     * */
    private void getCipherTextFromDB() {
        String ctext = database.getCipherText(projectID, projectTitle);
        String[] split = ctext.split("\\|"); //split and add to change history

        changeHistory = new ArrayList<>(Arrays.asList(split));
        cipherText = changeHistory.get(changeHistory.size() - 1);
        cipherTextView.setText(cipherText);
    }

    protected void updateCipherTextToDB() { //saves project' progress
        String data = new String(); //process the stuff from changeHistory

        if(!data.isEmpty()){
            for(int i = 0; i < changeHistory.size(); i++) {
                data += changeHistory.get(i);
                if(i < changeHistory.size() - 1)
                    data += "|";
            }
            database.updateCipherText(projectID, projectTitle, data);
        }
    }

    private void setCipherTextView() {
        /**SET UP THE CIPHER TEXT VIEW AREA*/
        cipherTextView = view.findViewById(R.id.project_view_cipher_text);
    }

    private void setUndoButton() {
        Button undoButton = view.findViewById(R.id.button_undo);
        undoButton.setOnClickListener(view -> undo());
    }

    private void setAnalysisTool() {
        Button graphButtonPopup = view.findViewById(R.id.button_graphPopup);
        graphButtonPopup.setOnClickListener(view -> {
            Intent intent = new Intent(fragmentActivity, Activity_calculate_IC.class);
            intent.putExtra("cipherText", cipherText);

            startActivity(intent);
        });
    }

    private void setLetterFrequencyGraph() {
        Button frequencyAnalysisButton = view.findViewById(R.id.frequency_graph_letter);
        frequencyAnalysisButton.setOnClickListener(view -> {
            Intent intent = new Intent(fragmentActivity, Activity_graph_nGram.class);
            intent.putExtra("cipherText", cipherText);
            startActivity(intent);
        });
    }

    private void setPeriodFrequencyGraph() {
        Button ICFrequencyButton = view.findViewById(R.id.frequency_graph_period);
        ICFrequencyButton.setOnClickListener(view -> {
            Intent intent = new Intent(fragmentActivity, Activity_graph_character_frequency.class);
            intent.putExtra("cipherText", cipherText);
            startActivity(intent);
        });
    }

    private void undo(){ //undo the cipher text into its previous state
        if(changeHistory.size() <= 1 || originalCipherText.equals(cipherText)) {
            framework.system_message_small("Maximum undo attempt reached");
            shiftSeekBar.setProgress(INITIAL_CAESAR_SEEKBAR_VALUE);
        }

        else if(!cipherText.equals(originalCipherText) && !changeHistory.isEmpty() && changeHistory.size() >1) { //because the first entry == originalCipherText
            changeHistory.remove(changeHistory.size()-1); //remove the latest entry
            cipherText = changeHistory.get(changeHistory.size()-1);
            cipherTextView.setText(cipherText);
        }
    }

    /**
     * Reset Cipher text into its original state
     * ##This function is being accessed from this fragment's parent activity (Activity_Project_View)"*/
    protected void reset() {
        if(cipherText.equals(originalCipherText))
            framework.system_message_small("cipher text is already at its original state");

        else if(!cipherText.equals(originalCipherText) && !changeHistory.isEmpty()) {
            cipherText = originalCipherText;
            cipherTextView.setText(cipherText);
            changeHistory.clear(); //remove all items from the arraylist
            framework.system_message_small("Cipher text is set to its original state");
        }

        shiftSeekBar.setProgress(0);
    }

    /**GUI SETUP*/
    /**for all functions that changes the value of variable cipherText, call function "refresh()" afterwards to refresh cipher text view on the GUI
     * example can be seen in functions "doShiftLeft()" and "doShiftRight()"
     * */
    private void setShiftTool() {
        View shiftView = view.findViewById(R.id.include_shift); //gets from the "include" view type in the xml file

        SeekBar.OnSeekBarChangeListener shiftSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                indicator.setText("Current Shift: " + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        shiftSeekBar = shiftView.findViewById(R.id.seekBar_caesar);
        indicator = shiftView.findViewById(R.id.seekBar_indicator);

        shiftSeekBar.setMax(INITIAL_CAESAR_SEEKBAR_VALUE);
        shiftSeekBar.setProgress(0);
        shiftSeekBar.setOnSeekBarChangeListener(shiftSeekBarListener);

        indicator.setText("Current Shift: " + shiftSeekBar.getProgress());

        Button shiftLeft = shiftView.findViewById(R.id.shift_left);
        Button shiftRight = shiftView.findViewById(R.id.shift_right);

        shiftLeft.setOnClickListener(view -> {
            shiftCipherBy = shiftSeekBar.getProgress();
            doShiftLeft();
            refresh();
        });
        shiftRight.setOnClickListener(view -> {
            shiftCipherBy = shiftSeekBar.getProgress();
            doShiftRight();
            refresh();
        });
    }

    /**
     * Cipher calculation functions
     * */

    private void doShiftLeft() {
        framework.format(cipherText);
        try {
            framework.setMODIFIED_TEXT(shift.decrypt(framework.getMODIFIED_TEXT(), Integer.toString(Math.abs(shiftCipherBy))));
            cipherText = framework.displayModifiedString();
        }catch(InvalidKeyException e) {
            framework.system_message_small(e.getMessage());
        }
    }

    private void doShiftRight() {
        framework.format(cipherText);
        try {
            framework.setMODIFIED_TEXT(shift.encrypt(framework.getMODIFIED_TEXT(), Integer.toString(Math.abs(shiftCipherBy))));
            cipherText = framework.displayModifiedString();
        }catch(InvalidKeyException e) {
            framework.system_message_small(e.getMessage());
        }
    }

    private void setSubstitutionTool() {
        View substitutionView = view.findViewById(R.id.include_sub); //gets view from "include" element in xml file

        Character[] alphabets = {'-', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        /**listener for charA spinner view*/
        AdapterView.OnItemSelectedListener charAListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                charA = adapterView.getItemAtPosition(i).toString().charAt(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {}
        };

        /**listener for charB spinner view*/
        AdapterView.OnItemSelectedListener charBListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                charB = adapterView.getItemAtPosition(i).toString().charAt(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {}
        };

        OnClickListener charSubButtonListener = v -> {
            if((charA != '-') || (charB != '-')) {
                doSubstitution(charA, charB);
                refresh();
            }
        };

        /**Setup spinner for both charA and charB*/
        Spinner charASpinner = substitutionView.findViewById(R.id.spinner_charA);
        Spinner charBSpinner = substitutionView.findViewById(R.id.spinner_charB);

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
        Button charSubButton = substitutionView.findViewById(R.id.button_charSubstitution);
        charSubButton.setOnClickListener(charSubButtonListener);
    }

    private void doSubstitution(char a, char b){ //replace charA with charB
        framework.format(cipherText);

        framework.setMODIFIED_TEXT(substitutionCipher.byCharacter(a, b, framework.getMODIFIED_TEXT(), originalCipherText));
        cipherText = framework.displayModifiedString();
    }

    private void doTranspoEncrypt(String key) {
        framework.format(cipherText);

        String s = transpositionCipher.encrypt(framework.getMODIFIED_TEXT(), key);

        if(inputTextContainsDuplicate(s))
            framework.system_message_small("Transposition key cannot contain duplicate characters");
        else if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");
        else{
            framework.setMODIFIED_TEXT(s);
            cipherText = framework.displayModifiedString();
        }
    }
    private void doTranspoDecrypt(String key) {
        framework.format(cipherText);

        String s = transpositionCipher.decrypt(framework.getMODIFIED_TEXT(), key);

        if(inputTextContainsDuplicate(s))
            framework.system_message_small("Transposition key cannot contain duplicate characters");
        else if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");
        else{
            framework.setMODIFIED_TEXT(s);
            cipherText = framework.displayModifiedString();
        }
    }

    private void doTranspoPeriodicEncrypt(String key) {
        framework.format(cipherText);

        framework.setMODIFIED_TEXT(transpositionPeriodic.encrypt(framework.getMODIFIED_TEXT(), key));
        cipherText = framework.displayModifiedString();
    }
    private void doTranspoPeriodicDecrypt(String key) {
        framework.format(cipherText);

        framework.setMODIFIED_TEXT(transpositionPeriodic.decrypt(framework.getMODIFIED_TEXT(), key));
        cipherText = framework.displayModifiedString();
    }

    private void doTranspoRectangularEncrypt(String key) {
        framework.format(cipherText);

        framework.setMODIFIED_TEXT(rectangularKeyTransposition.encrypt(framework.getMODIFIED_TEXT(), key));
        cipherText = framework.displayModifiedString();
    }
    private void doTranspoRectangularDecrypt(String key) {
        framework.format(cipherText);

        framework.setMODIFIED_TEXT(rectangularKeyTransposition.decrypt(framework.getMODIFIED_TEXT(), key));
        cipherText = framework.displayModifiedString();
    }

    private void doBeaufortEncrypt(String key) {
        framework.format(cipherText);

        String s = beaufortCipher.encrypt(framework.getMODIFIED_TEXT(), key);

        if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");
        else{
            framework.setMODIFIED_TEXT(s);
            cipherText = framework.displayModifiedString();
        }
    }
    private void doBeaufortDecrypt(String key) {
        framework.format(cipherText);

        String s = beaufortCipher.decrypt(framework.getMODIFIED_TEXT(), key);

        if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");
        else{
            framework.setMODIFIED_TEXT(s);
            cipherText = framework.displayModifiedString();
        }
    }

    private void doBeaufortVariantEncrypt(String key) {
        framework.format(cipherText);

        String s = beaufortVariantCipher.encrypt(framework.getMODIFIED_TEXT(), key);

        if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");
        else{
            framework.setMODIFIED_TEXT(s);
            cipherText = framework.displayModifiedString();
        }
    }
    private void doBeaufortVariantDecrypt(String key) {
        framework.format(cipherText);

        String s = beaufortVariantCipher.decrypt(framework.getMODIFIED_TEXT(), key);

        if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");
        else{
            framework.setMODIFIED_TEXT(s);
            cipherText = framework.displayModifiedString();
        }
    }

    private void doVigenereEncrypt(String key) {
        framework.format(cipherText);

        String s = vigenereCipher.encrypt(framework.getMODIFIED_TEXT(), key);

        if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");

        framework.setMODIFIED_TEXT(s);
        cipherText = framework.displayModifiedString();
    }
    private void doVigenereDecrypt(String key) {
        framework.format(cipherText);

        String s = vigenereCipher.decrypt(framework.getMODIFIED_TEXT(), key);

        if(s.equals("Failed."))
            framework.system_message_small("Invalid key input");

        framework.setMODIFIED_TEXT(s);
        cipherText = framework.displayModifiedString();
    }

    /**Refreshes text view, as well as recording the cipher text changes*/
    private void refresh(){
        changeHistory.add(cipherText);
        cipherTextView.setText(cipherText);
    }

    /**Refreshes text view but with custom chars/block and blocks/line */
    private void refresh(String val, int space, int line, boolean b) {

        Project project = new Project("", 0, originalCipherText);
//        System.out.println("val = " + val);
        project.setModifiedText(val);

        System.out.println("VAL: " + val);
        System.out.println(" MT: " +  project.getModifiedText());

//        System.out.println(cipherText);
        cipherText = project.print(space, line, b); //space == block size, line == blocks per line
        refresh();
    }

    private boolean inputTextContainsDuplicate(String str){
        HashSet hashSet = new HashSet(str.length());

        for(char c : str.toCharArray()){ //iterate through character array
            if(!hashSet.add(Character.toUpperCase(c)))//try to add each char to hashset
                return true; //return false if could not add
        }
        return false;
    }
/**
 * THIS IS A CLASS FOR GENERAL TEXT INPUT IN THE PROJECT VIEW
 * */
    class GeneralTextInput
    {
        View view;
        Button positiveButton;
        Button negativeButton;
        EditText textInput;

        public GeneralTextInput(View parentView) {
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

        protected boolean inputEmpty() {
            if(this.textInput.getText().toString().isEmpty()) {
                framework.system_message_small("Key cannot be empty");
                return true;
            }

            return false;
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
