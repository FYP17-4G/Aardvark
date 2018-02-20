/**
 * Programmer: Eka Nugraha Pratama
 *
 * Contains the source code for "Encryption / Decryption" feature activity
 * */

package com.example.FYP.aardvark_project.GUI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.ClipboardManager;
import android.content.ClipData;

import com.example.FYP.aardvark_project.Ciphers.*;
import com.example.FYP.aardvark_project.Ciphers.RectangularKeyTransposition;
import com.example.FYP.aardvark_project.R;
import com.example.FYP.aardvark_project.Analytics.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Activity_Enc_Dec extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    App_Framework framework;

    private static final int READ_REQUEST_CODE = 42;

    /**
     * TOOLS LIST AND CONSTANTS
     */
    /**Use this array for when entering kamasutra cipher*/
    private final String alphabets[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private final String SHIFT_CIPHER = "Shift Cipher";
    private final String VIG_ADDITIVE = "Vigenere Additive";
    private final String VIG_INVERSE = "Vigenere Inverse";
    private final String VIG_SUBSTRACTIVE = "Vigenere Substractive";
    private final String TRANSPO = "Transposition Cipher";
    private final String TRANSPO_R = "Rectangular Transposition Cipher";
    private final String TRANSPO_P = "Periodic Transposition Cipher";
    private final String BEAUFORT = "Beaufort";
    private final String BEAUFORTVARIANT = "Beaufort Variant";
    private final String ONETIMEPAD = "One Time Pad";
    private final String KAMASUTRA = "Kamasutra Cipher";

    private final String INPUT_TEXT_EMPTY_MESSAGE = "Input TEXT is still empty!";
    private final String INPUT_KEY_EMPTY_MESSAGE = "Input KEY is still empty!";

    private final String INPUT_TEXT_FILE_NAME = "latestInputText.txt";

    private final int ALPHABET_LENGTH = 26;
    private final int GRID_COLUMN = 6;
    private final int GRID_ROW = 5;

    /**
     * OTHER VARIABLES
     */
    private String inputText = new String();
    private EditText inputTextView;

    private Button copyButton; //copy to clipboard
    private Button pasteButton; //paste from clipboard

    private String[] tools = {SHIFT_CIPHER, VIG_ADDITIVE, VIG_INVERSE, VIG_SUBSTRACTIVE, TRANSPO, TRANSPO_P, TRANSPO_R, BEAUFORT, BEAUFORTVARIANT, ONETIMEPAD, KAMASUTRA};
    private Spinner toolSpinner;

    private EditText keyInput;
    private Button positiveButton;
    private Button negativeButton;

    /**Variables for kamasutra cipher*/
    private FrameLayout dragableLayout;
    private GridLayout dragableGridView;
    private Button applyKamasutraButton;
    private Button resetKamasutraButton;
    private ImageView infoKamasutraButton;

    private ArrayList<Button> dragableButtonList;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_frontPage)
            launchFrontPageActivity();

        else if (id == R.id.nav_encryption_decryption)
            launchEncryptionDecryptionActivity();

        else if (id == R.id.nav_about_us)
            launchAboutUsActivity();

        else if (id == R.id.nav_settings)
            launchSettingsActivity();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveInputText();
        startActivity(new Intent(this, Activity_Main.class)); //navigate to front page on back pressed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.enc_dec_action_bar_menu, menu); // Inflate the menu; this adds items to the action bar if it is present
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.enc_dec_menu_read_from_file)
            openFileBrowser();
        return true;
    }

    /**This function will be executed on return from the device's file browser*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String cipherTextFromFile = new String();

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if(data != null) {
                Uri uri = data.getData(); //URI is the return value given by ACTION_OPEN_DOCUMENT intent

                try
                {
                    cipherTextFromFile = framework.readTextFromUri(uri, this);
                } catch (IOException e)
                {framework.system_message_small(e.getMessage());}

                if(!cipherTextFromFile.isEmpty())
                {
                    inputText = cipherTextFromFile;
                    refresh();
                }
                else
                    framework.system_message_small("Error opening file (file could be empty)");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        framework = new App_Framework(this, false);
        overrideTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc_dec);

        setNavDrawer();

        setInputTextView();

        setCopyPasteButton();
        setSpinner();
        setKeyInputView();

        setKamasutra();

        switchInput(true);
    }

    /**Functions for onCreate()*/
    /**Use this because this class needs "no action bar" theme for it to work */
    private void overrideTheme() {
        if(framework.isDarkTheme())
            this.setTheme(R.style.DarkTheme_NoActionBar);
        else
            this.setTheme(R.style.AppTheme_NoActionBar);
    }

    private void setNavDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Encryption/Decrytion Tool");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true); //highlights "front page" item menu
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Copy and paste mechanism
     */
    View.OnClickListener copyPasteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.button_copy_to_clip) {
                if (!inputText.isEmpty())
                    copyToClip();

                else
                    framework.system_message_small("Error: Text is still empty");
            } else
                pasteFromClip();
        }
    };

    private void setCopyPasteButton() {
        copyButton = findViewById(R.id.button_copy_to_clip);
        pasteButton = findViewById(R.id.button_paste_from_clip);

        copyButton.setOnClickListener(copyPasteButtonListener);
        pasteButton.setOnClickListener(copyPasteButtonListener);
    }

    private void pasteFromClip() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            inputText = clipboard.getPrimaryClip().getItemAt(0).getText().toString(); //copy whats inside primary clipboard to input text
            inputTextView.setText(inputText);
            framework.system_message_small("Text copied from clipboard");
        } else
            framework.system_message_small("Error: Clipboard is empty");
    }

    private void copyToClip() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("label", inputText);
        clipboard.setPrimaryClip(clip);

        framework.system_message_small("Copied to clipboard");
    }

    /**
     * View Setups
     */
    private void setInputTextView() {
        inputTextView = findViewById(R.id.ed_textInputView);
        inputText = loadInputText();
        inputTextView.setText(inputText);

        inputTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputText = inputTextView.getText().toString();
            }
        });
    }

    private void setSpinner() {
        toolSpinner = findViewById(R.id.tool_spinner);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tools);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolSpinner.setOnItemSelectedListener(spinnerListener);
        toolSpinner.setAdapter(dataAdapter);
    }

    private void setKeyInputView() {
        keyInput = findViewById(R.id.key_input);
        positiveButton = findViewById(R.id.positive_button);
        negativeButton = findViewById(R.id.negative_button);
    }

    private String getKeyInput() {
        return keyInput.getText().toString();
    }

    private void changeInput(String hint, String positiveButtonText, String negativeButtonText, View.OnClickListener buttonListener, boolean isNumberInput) {
        keyInput.setText("");
        keyInput.setHint(hint);

        positiveButton.setText(positiveButtonText);
        negativeButton.setText(negativeButtonText);

        positiveButton.setOnClickListener(buttonListener);
        negativeButton.setOnClickListener(buttonListener);

        if (isNumberInput)
            keyInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        else
            keyInput.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    /**
     * Element setters for Kamasutra Cipher
     * */
    private void setKamasutra() {
        /**Set dragable input*/
        dragableLayout = findViewById(R.id.dragable_layout);

        View v = findViewById(R.id.include_dragableInput);

        dragableGridView = v.findViewById(R.id.dragable_grid_layout);
        dragableGridView.setColumnCount(GRID_COLUMN);
        dragableGridView.setRowCount(GRID_ROW);

        applyKamasutraButton = v.findViewById(R.id.dragable_button_apply);
        applyKamasutraButton.setOnClickListener(kamasutraListener);

        resetKamasutraButton = v.findViewById(R.id.dragable_button_reset);
        resetKamasutraButton.setOnClickListener(view -> resetKamasutra());

        String infoMessage = "Drag and drop the alphabets to re arrange the key";
        infoKamasutraButton = v.findViewById(R.id.dragable_button_info);
        infoKamasutraButton.setOnClickListener(view -> framework.system_message_popup("Info", infoMessage, "Got it"));

        fillDragableButtonList(ALPHABET_LENGTH);
    }

    /**Fill the array list to be used for linear layout view*/
    private void fillDragableButtonList(int N) {
        dragableButtonList = new ArrayList<>();
        dragableGridView.removeAllViews();

        for(int i = 0; i < N; i++) //fill the array list
            dragableButtonList.add(addDragableButton(i, alphabets[i]));

        for(Button b: dragableButtonList) //add to view
            dragableGridView.addView(b);
    }

    /**Changes input view between "general text input" with encrypt and decrypt button
     * and "dragable buttons" for Kamasutra cipher use*/
    private void switchInput(boolean generalInput) {
        if(!generalInput) {
            keyInput.setVisibility(View.GONE);
            positiveButton.setVisibility(View.GONE);
            negativeButton.setVisibility(View.GONE);

            dragableLayout.setVisibility(View.VISIBLE);
        }
        else if(generalInput) {
            keyInput.setVisibility(View.VISIBLE);
            positiveButton.setVisibility(View.VISIBLE);
            negativeButton.setVisibility(View.VISIBLE);

            dragableLayout.setVisibility(View.GONE);
        }
    }

    /**
     * ID and val should be of the same value, but does not necessarily have to.
     * ID = the identification of the object
     * val = value displayed on the object
     * */
    private Button addDragableButton(int ID, String val) {
        Button button = new Button(new ContextThemeWrapper(this, R.style.Widget_AppCompat_Button_Borderless_Colored), null, R.style.Widget_AppCompat_Button_Borderless_Colored);
        button.setId(ID);
        button.setText(val);
        button.setTextSize(10);

        /**Build the drag shadow, its a "floating" copy of itself and follows user' finger*/
        button.setOnLongClickListener(view -> {
            ClipData clipData = ClipData.newPlainText(Integer.toString(ID), val);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(clipData, shadowBuilder, view, 0);

            return true;
        });
        button.setOnDragListener((view, dragEvent) -> {

            switch(dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View v = (View) dragEvent.getLocalState(); // v = object that is being dragged by the user

                    /**Switch position, both */
                    int idxHover = 0; //index of the view being dragged
                    int idxGround = 0; //index of the view below it

                    for(int i = 0; i < dragableButtonList.size(); i++) {
                        if(dragableButtonList.get(i).getId() == v.getId())
                            idxHover = i;
                        if(dragableButtonList.get(i).getId() == ID)
                            idxGround = i;
                    }

                    Collections.swap(dragableButtonList, idxHover, idxGround);

                    resetKamasutraInputView();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    break;
            }

            return true;
        });

        return button;
    }

    /**Re - order the alphabet arrangement in Kamasutra input view*/
    private void resetKamasutra() {
        fillDragableButtonList(ALPHABET_LENGTH);
        resetKamasutraInputView();
        framework.system_message_small("Key reset");
    }

    /**Reset the view with the values in "dragableButtonList"*/
    private void resetKamasutraInputView() {
        dragableGridView.removeAllViews();

        for(Button b: dragableButtonList)
            dragableGridView.addView(b);
    }


    /**
     * =========Cipher algorithm listeners=========
     */
    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String value = adapterView.getItemAtPosition(i).toString();

            switchInput(true); //switch the key input method to regular text input or dragable buttons(used in kamasutra cipher)
            switch (value) {
                case SHIFT_CIPHER:
                    changeInput("Shift Cipher By", "Shift Right", "Shift left", shiftListener, true);
                    break;
                case VIG_ADDITIVE:
                    changeInput("Vigenere Additive Key", "Encrypt", "Decrypt", vigenereAdditiveListener, false);
                    break;
                case VIG_INVERSE:
                    changeInput("Vigenere Inverse Key", "Encrypt", "Decrypt", vigenereInverseListener, false);
                    break;
                case VIG_SUBSTRACTIVE:
                    changeInput("Vigenere Substractive key", "Encrypt", "Decrypt", vigenereSubstractiveListener, false);
                    break;
                case TRANSPO:
                    changeInput("TranspositionCipher Key", "Encrypt", "Decrypt", transpositionListener, false);
                    break;
                case TRANSPO_P:
                    changeInput("Periodic TranspositionCipher Key", "Encrypt", "Decrypt", transpositionPeriodicListener, false);
                    break;
                case TRANSPO_R:
                    changeInput("Rectangular TranspositionCipher Key", "Encrypt"," Decrypt", transpositionRectangularListener, false);
                    break;
                case BEAUFORT:
                    changeInput("Beaufort Key", "Encrypt", "Decrypt", beaufortListener, false);
                    break;
                case BEAUFORTVARIANT:
                    changeInput("Beaufort Variant Key", "Encrypt", "Decrypt", beaufortVariantListener, false);
                    break;
                case ONETIMEPAD:
                    changeInput("One Time Pad key", "Encrypt", "Decrypt", oneTimePadListener, false);
                    break;
                case KAMASUTRA:
                    switchInput(false);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    View.OnClickListener shiftListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else if(Integer.parseInt(keyInput.getText().toString()) >= 26 || Integer.parseInt(keyInput.getText().toString()) < 1)
                framework.system_message_small("Shift Key must be between 1 and 25");
            else {
                if (view.getId() == R.id.positive_button) //encrypt
                    doShiftR(getKeyInput());
                else
                    doShiftL(getKeyInput());
            }
        }
    };

    View.OnClickListener vigenereAdditiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if (view.getId() == R.id.positive_button) //encrypt
                    doVigenereAdditive(true, getKeyInput());
                else
                    doVigenereAdditive(false, getKeyInput());
            }
        }
    };

    View.OnClickListener vigenereInverseListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if (view.getId() == R.id.positive_button) //encrypt
                    doVigenereInverse(true, getKeyInput());
                else
                    doVigenereInverse(false, getKeyInput());
            }
        }
    };

    View.OnClickListener vigenereSubstractiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if (view.getId() == R.id.positive_button) //encrypt
                    doVigenereSubstractive(true, getKeyInput());
                else
                    doVigenereSubstractive(false, getKeyInput());
            }
        }
    };

    View.OnClickListener transpositionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if (view.getId() == R.id.positive_button) //encrypt
                    doTranspo(true, getKeyInput());
                else
                    doTranspo(false, getKeyInput());
            }
        }
    };

    View.OnClickListener transpositionRectangularListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if(view.getId() == R.id.positive_button) //encrypt
                    doRectangularTranspo(true, getKeyInput());
                else
                    doRectangularTranspo(false, getKeyInput());
            }
        }
    };

    View.OnClickListener transpositionPeriodicListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if(view.getId() == R.id.positive_button) // encrypt
                    doPeriodicTranspo(true, getKeyInput());
                else
                    doPeriodicTranspo(false, getKeyInput());
            }
        }
    };

    View.OnClickListener beaufortListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if(view.getId() == R.id.positive_button) //encrypt
                    doBeaufortCipher(true, getKeyInput());
                else
                    doBeaufortCipher(false, getKeyInput());
            }
        }
    };

    View.OnClickListener beaufortVariantListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if(view.getId() == R.id.positive_button) // encrypt
                    doBeaufortVariant(true, getKeyInput());
                else
                    doBeaufortVariant(false, getKeyInput());
            }
        }
    };

    View.OnClickListener oneTimePadListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (inputTextEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);
            else if (inputKeyEmpty())
                framework.system_message_small(INPUT_KEY_EMPTY_MESSAGE);
            else {
                if(view.getId() == R.id.positive_button) // encrypt
                    doOneTimePad(true, getKeyInput());
                else
                    doOneTimePad(false, getKeyInput());
            }
        }
    };

    View.OnClickListener kamasutraListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(inputText.isEmpty())
                framework.system_message_small(INPUT_TEXT_EMPTY_MESSAGE);

            String key = new String();
            for(Button b: dragableButtonList)
                key += b.getText();

            doKamasutra(key);
        }
    };

    /**=========Cipher Algorithm=========*/

    /**
     * FOR ALL SHIFT ALGORITHM text variable: "input text: String", key variable: "shiftCipherBy: int"
     *
     * new Shift() uses the algorithm in kryptoTools.Shift.java
     */
    private void doShiftR(String key) { //shift Encrypt
        framework.format(inputText);
        try {
            framework.setMODIFIED_TEXT(new Shift().encrypt(framework.getMODIFIED_TEXT(), key));
        } catch (InvalidKeyException e) {
            framework.system_message_small(e.getMessage());
        }

        refresh();
    }

    private void doShiftL(String key) { //shift Decrypt
        framework.format(inputText);
        try {
            framework.setMODIFIED_TEXT(new Shift().decrypt(framework.getMODIFIED_TEXT(), key));
        } catch (InvalidKeyException e) {
            framework.system_message_small(e.getMessage());
        }

        refresh();
    }

    /**
     * FOR RECT TRANSPO, RECT SUBSTITUTION,AND ALL VIGENERE text variable: "input text: String", key variable: "key: String"
     */
    private void doVigenereAdditive(boolean encrypt, String key) {
        framework.format(inputText);
        try {
            if (encrypt)
                framework.setMODIFIED_TEXT(new VigenereAdditive().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new VigenereAdditive().decrypt(framework.getMODIFIED_TEXT(), key));
        } catch (InvalidKeyException e) {
            framework.system_message_small(e.getMessage());
        }

        refresh();
    }

    private void doVigenereInverse(boolean encrypt, String key) {
        framework.format(inputText);
        try {
            if (encrypt)
                framework.setMODIFIED_TEXT(new VigenereInverse().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new VigenereInverse().decrypt(framework.getMODIFIED_TEXT(), key));
        } catch (InvalidKeyException e) {
            framework.system_message_small(e.getMessage());
        }

        refresh();
    }

    private void doVigenereSubstractive(boolean encrypt, String key) {
        framework.format(inputText);
        try {
            if (encrypt)
                framework.setMODIFIED_TEXT(new VigenereSubtractive().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new VigenereSubtractive().decrypt(framework.getMODIFIED_TEXT(), key));
        } catch (InvalidKeyException e) {
            framework.system_message_small(e.getMessage());
        }

        refresh();
    }

    private void doTranspo(boolean encrypt, String key) {
        framework.format(inputText);
            if (encrypt)
                framework.setMODIFIED_TEXT(new TranspositionCipher().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new TranspositionCipher().decrypt(framework.getMODIFIED_TEXT(), key));

        refresh();
    }

    private void doPeriodicTranspo(boolean encrypt, String key) {
        framework.format(inputText);
            if(encrypt)
                framework.setMODIFIED_TEXT(new TranspositionPeriodic().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new TranspositionPeriodic().decrypt(framework.getMODIFIED_TEXT(), key));

        refresh();
    }

    private void doRectangularTranspo(boolean encrypt, String key) {
        framework.format(inputText);
            if(encrypt)
                framework.setMODIFIED_TEXT(new RectangularKeyTransposition().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new RectangularKeyTransposition().decrypt(framework.getMODIFIED_TEXT(), key));

        refresh();
    }

    private void doBeaufortCipher(boolean encrypt, String key) {
    framework.format(inputText);
        if(encrypt)
            framework.setMODIFIED_TEXT(new BeaufortCipher().encrypt(framework.getMODIFIED_TEXT(), key));
        else
            framework.setMODIFIED_TEXT(new BeaufortCipher().decrypt(framework.getMODIFIED_TEXT(), key));

        refresh();
    }

    private void doBeaufortVariant(boolean encrypt, String key) {
        framework.format(inputText);
            if(encrypt)
                framework.setMODIFIED_TEXT(new BeaufortVariantCipher().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new BeaufortVariantCipher().decrypt(framework.getMODIFIED_TEXT(), key));

        refresh();
    }

    private void doOneTimePad(boolean encrypt, String key) {
        framework.format(inputText);
            if(encrypt)
                framework.setMODIFIED_TEXT(new OneTimePad().encrypt(framework.getMODIFIED_TEXT(), key));
            else
                framework.setMODIFIED_TEXT(new OneTimePad().decrypt(framework.getMODIFIED_TEXT(), key));

        refresh();
    }

    private void doKamasutra(String key) {
        framework.format(inputText);
        framework.setMODIFIED_TEXT(new Kamasutra().encrypt(framework.getMODIFIED_TEXT(), key));
        refresh();
    }

    /**Saving and Loading latest input text*/
    private void saveInputText() {
        if(!inputTextEmpty()) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(INPUT_TEXT_FILE_NAME, this.MODE_PRIVATE));
                outputStreamWriter.write(inputText);
                outputStreamWriter.close();
            }
            catch (IOException e)
                {}
        }
    }
    private String loadInputText() {
        String ret = "";

        try {
            InputStream inputStream = this.openFileInput(INPUT_TEXT_FILE_NAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e)
            {framework.system_message_small(e.getMessage());}
        catch (IOException e)
            {framework.system_message_small(e.getMessage());}

        return ret;
    }

    private boolean inputTextEmpty()
    {
        return inputText.isEmpty();
    }
    private boolean inputKeyEmpty()
    {
        return keyInput.getText().toString().isEmpty();
    }

    private void openFileBrowser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //START INTENT TO CHOOSE FILE USING DEVICE' DEFAULT FILE BROWSER
        intent.addCategory(Intent.CATEGORY_OPENABLE);//SHOW ONLY FILES THAT CAN BE OPENED
        intent.setType("text/plain"); //only plain txt file can be accessed

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private void refresh() {
        try {
            inputText = framework.displayModifiedString();
            inputTextView.setText(inputText);
        }catch(IndexOutOfBoundsException e)
        {framework.system_message_small(e.getMessage());}
    }

    private void launchFrontPageActivity() {
        startActivity(new Intent(this, Activity_Main.class));
    }

    private void launchEncryptionDecryptionActivity() {
        startActivity(new Intent(this, Activity_Enc_Dec.class));
    }

    private void launchAboutUsActivity() {
        this.startActivity(new Intent(this, Activity_About_Us.class));
    }
    private void launchSettingsActivity() {
        this.startActivity(new Intent(this, Activity_Settings.class));
    }
}
