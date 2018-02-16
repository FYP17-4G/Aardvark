package com.example.FYP.aardvark_project.GUI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Context;

import com.example.FYP.aardvark_project.R;
import com.example.FYP.aardvark_project.kryptoTools.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is class contains methods that simplifies the process of building the application
 */

public class App_Framework
{
    private Context context;

    private AlertDialog alertDialog;
    private AlertDialog.Builder popUpWindow;
    private TextView popup_textView;

    private Toast toast;

    private EditText popup_inputText;

    public App_Framework(Context context, boolean overrideTheme) {
        this.context = context;

        if(overrideTheme)
            setTheme();

        toast = Toast.makeText(context, "dummy", Toast.LENGTH_SHORT); //create new Toast object
    }

    /**
     * This function reinstate the alert dialog builder and applies theme accordingly
     * */
    private void reinstateBuilder() {
        if(isDarkTheme()) //is dark theme
            popUpWindow = new AlertDialog.Builder(context, R.style.DarkDialogTheme);

        else //is light theme
            popUpWindow = new AlertDialog.Builder(context, R.style.AppDialogTheme);
    }

    /**
     * This function sets the theme of the application according to the shared preferences value defined
     * in the Activity_Settings activity
     *
     * =====================================
     * Because setTheme() is called upon this object creation, this object must be called before super.OnCreate()
     * on each activity
     *
     * #Note: For fragment, the point above does not matter
     * */
    private boolean getThemePreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(Activity_Settings.DARK_THEME_SWITCH, false);
    }

    public boolean setTheme() {
        Boolean dark = getThemePreference();

        if(!dark)
            context.setTheme(R.style.AppTheme); //set context theme to light
        else
            context.setTheme(R.style.DarkTheme); //set context theme to dark

        return dark;
    }

    public boolean isDarkTheme()
    {
        return getThemePreference();
    }



    /**
     * ==============POP UP MENU FUNCTIONS==============
     * */

    /**Use this method to get the user input in the popup dialogue*/
    public String popup_getInput()
    {
        return popup_inputText.getText().toString();
    }

    /**popup function specifically for "main activity " use, when long pressed a card, this function will display the preview of the cipher text*/
    @SuppressLint("ClickableViewAccessibility")
    public AlertDialog popup_cipher_preview(String cipherText) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pop_cipher_preview, null);
        TextView previewCipherText = view.findViewById(R.id.previewCText);
        previewCipherText.setText(cipherText);

        reinstateBuilder();

        popUpWindow.setView(view);
        alertDialog = popUpWindow.create();

        view.setOnTouchListener((view1, event) -> {

            if(event.getAction() == MotionEvent.ACTION_UP)
                alertDialog.cancel(); //cancel alert dialog on press release

            return true;
        });

        return alertDialog;
    }

    public AlertDialog popup_custom(String title, View view) { //plain custom pop up container
        if(view.getParent() != null) //checks if view already exist
            ((ViewGroup)view.getParent()).removeView(view);

        reinstateBuilder();

        popUpWindow.setTitle(title);
        popUpWindow.setView(view);

        alertDialog = popUpWindow.create();

        return alertDialog;
    }

    //custom popup with defined positive and negative button behaviour
    public AlertDialog popup_custom(String title, View view,String positiveText, String negativeText, DialogInterface.OnClickListener clickListener) {
        if(view.getParent() != null) //checks if view already exist
            ((ViewGroup)view.getParent()).removeView(view);

        reinstateBuilder();

        popUpWindow.setTitle(title);
        popUpWindow.setView(view);

        popUpWindow.setPositiveButton(positiveText, clickListener);
        popUpWindow.setNegativeButton(negativeText, (dialogInterface, i) -> dialogInterface.cancel());

        alertDialog = popUpWindow.create();
        return alertDialog;
    }

    public void popup_custom(String title, View view, DialogInterface.OnClickListener clickListener) {
        if(view.getParent() != null) //checks if view already exist
            ((ViewGroup)view.getParent()).removeView(view);

        reinstateBuilder();

        popUpWindow.setTitle(title);
        popUpWindow.setView(view);

        popUpWindow.setPositiveButton("OK", clickListener);
        popUpWindow.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        popUpWindow.show();
    }

    /**This changes the input type of the edit text field to number only*/
    public void popup_getNumber_show(String popup_title, String popup_hint, DialogInterface.OnClickListener clickListener, int inputLengthLimit) {
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        /**set up the input field*/
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        popup_inputText.setHint(popup_hint);
        popUpWindow.setView(popup_inputText);

        popUpWindow.setPositiveButton("OK",clickListener);
        popUpWindow.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        popUpWindow.show();
    }

    public void popup_getNumber_show(String popup_title, String popup_hint, DialogInterface.OnClickListener positiveOCL,DialogInterface.OnClickListener negativeOCL, String positiveText, String negativeText) {
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        /**set up the input field*/
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        popup_inputText.setHint(popup_hint);
        popUpWindow.setView(popup_inputText);

        popUpWindow.setPositiveButton(positiveText, positiveOCL);
        popUpWindow.setNegativeButton(negativeText, negativeOCL);

        popUpWindow.show();
    }

    public void popup_show(String popup_title, String popup_hint, DialogInterface.OnClickListener clickListener) {
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        /**set up the input field*/
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        popup_inputText.setHint(popup_hint);
        popUpWindow.setView(popup_inputText);

        popUpWindow.setPositiveButton("OK",clickListener);
        popUpWindow.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        popUpWindow.show();
    }

    /**CUSTOMIZEABLE pop up input text for positive button (right button) and negative button (left button)*/
    public void popup_show(String popup_title, String popup_hint, DialogInterface.OnClickListener positiveOCL,DialogInterface.OnClickListener negativeOCL, String positiveText, String negativeText) {
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        /**set up the input field*/
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        popup_inputText.setHint(popup_hint);
        popUpWindow.setView(popup_inputText);

        popUpWindow.setPositiveButton(positiveText, positiveOCL);
        popUpWindow.setNegativeButton(negativeText, negativeOCL);

        popUpWindow.show();
    }

    /**Use this for displaying small message, and error message as well*/
    public void system_message_small(String message){
        toast.setText(message);
        toast.show();
    }

    /**Pop up error message to display lengthy message, this can be used to display error message as well*/
    public void system_message_popup(String popup_title, String popup_message, String popup_buttonMessage) {
        DialogInterface.OnClickListener errMessageClickListener = (dialogInterface, i) -> dialogInterface.cancel();

        reinstateBuilder();

        popup_textView = new TextView(context);

        popUpWindow.setTitle(popup_title);
        popup_textView.setText(popup_message);
        popUpWindow.setPositiveButton(popup_buttonMessage, errMessageClickListener);
        popUpWindow.setView(popup_textView);
        popUpWindow.show();
    }
    public void system_message_popup(String popup_title, String popup_message) {
        DialogInterface.OnClickListener errMessageClickListener = (dialogInterface, i) -> dialogInterface.cancel();

        reinstateBuilder();

        popup_textView = new TextView(context);

        popUpWindow.setView(popup_textView);
        popUpWindow.setTitle(popup_title);
        popup_textView.setText(popup_message);
        popUpWindow.setPositiveButton("OK", errMessageClickListener);
        popUpWindow.show();
    }

    public void system_message_confirmAction(String popup_title, String popup_message, DialogInterface.OnClickListener ols){
        reinstateBuilder();

        popup_textView = new TextView(context);

        popUpWindow.setView(popup_textView);
        popUpWindow.setTitle(popup_title);
        popup_textView.setText("\n"+popup_message);
        popup_textView.setTypeface(Typeface.DEFAULT_BOLD);
        popup_textView.setGravity(Gravity.CENTER_HORIZONTAL);
        popup_textView.setTextSize(18);
        popUpWindow.setPositiveButton("OK", ols);
        popUpWindow.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        popUpWindow.show();
    }



    /**
     * ==============OTHER FRAMEWORK FUNCTIONS==============
     */

    /**This will convert given String to the same string but without whitespace*/
    public String stringNoWhiteSpace(String cText) {
        String temp = new String();

        for(int i = 0; i < cText.length(); i++)
            if(cText.charAt(i) != ' ')
                temp+=cText.charAt(i);

        return temp;
    }

    /**
     * Read text file from URI data type returned by the "OPEN_FILE_BROWSER" intent
     * */
    public String readTextFromUri(Uri uri, Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            stringBuilder.append(line);

        return stringBuilder.toString();
    }


    /**Mark's Code
     *
     * These are the functions necessary to apply changes to the cipher text,
     * while preserving the lines, symbols, and spaces of the original cipher text
     * */
    private static Utility util = Utility.getInstance();

    private String ORIGINAL_TEXT;
    private String MODIFIED_TEXT;
    private int TEXT_COUNT;

    private String displayOriginalString() {
        return ORIGINAL_TEXT;
    }

    public String displayModifiedString () {
        StringBuilder output = new StringBuilder();

        int counter = 0;
        for (Character c: ORIGINAL_TEXT.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                output.append(' ');
            } else if (Character.isUpperCase(c)) {
                output.append(Character.toUpperCase(MODIFIED_TEXT.charAt(counter)));
                ++counter;
            } else if (!Character.isAlphabetic(c)) {
                output.append(c);
            } else {
                output.append(MODIFIED_TEXT.charAt(counter));
                ++counter;
            }
        }

        return output.toString();
    }

    private String displayModifiedString (int blockSize, int blocksPerLine) {
        int charCounter = 0, blockCounter = 0;
        StringBuilder sb = new StringBuilder();

        for (Character c: MODIFIED_TEXT.toCharArray()) {
            sb.append(c);
            ++charCounter;
            if (charCounter == blockSize) {
                sb.append(" ");
                ++blockCounter;
                charCounter = 0;
            }

            if (blockCounter == blocksPerLine) {
                sb.append('\n');
                blockCounter = 0;
            }
        }

        return sb.toString();
    }


    private void init (String originalInput) {
        ORIGINAL_TEXT = originalInput;
        MODIFIED_TEXT = util.processText(ORIGINAL_TEXT);
        TEXT_COUNT = MODIFIED_TEXT.length();
    }

    /**MY OWN DEFINED FUNCTION*/
    public void setMODIFIED_TEXT(String s)
    {
        MODIFIED_TEXT = s;
    }
    public String getMODIFIED_TEXT()
    {
        return MODIFIED_TEXT;
    }

    public String format(String input) {
        init(input);
        return MODIFIED_TEXT;
    }
}
