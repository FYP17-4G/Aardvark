package com.example.FYP.aardvark_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.FYP.aardvark_project.kryptoTools.Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * This is class contains methods that simplifies the process of building the application
 */

public class App_Framework
{
    private Context context;

    //Pop up window related variables
    private AlertDialog alertDialog;
    private AlertDialog.Builder popUpWindow;
    private TextView popup_textView;

    //pop up view object variables
    private EditText popup_inputText;

    private String popUpNumberInput = new String();

    public App_Framework(Context context, boolean overrideTheme)
    {
        this.context = context;

        if(overrideTheme)
            setTheme();
    }

    /**
     * This function reinstate the alert dialog builder and applies theme accordingly
     * */
    private void reinstateBuilder()
    {
        if(new App_Framework(context, true).setTheme()) //is dark theme
            popUpWindow = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.DarkTheme));

        else //is light theme
            popUpWindow = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme));
    }

    /**
     * This function sets the theme of the application according to the shared preferences value defined
     * in the Settings activity
     *
     * Because setTheme() is called upon this object creation, this object must be called before super.OnCreate()
     * on each activity
     *
     * Note: For fragment, this does not matter
     * */
    public boolean setTheme()
    {
        //if true = change to dark theme, if not, use light theme

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean dark = sharedPreferences.getBoolean(Settings.DARK_THEME_SWITCH, false);

        if(!dark)
        {
            //set context theme to light
            context.setTheme(R.style.AppTheme);

        }
        else
        {
            //set context theme to dark
            context.setTheme(R.style.DarkTheme);
        }

        return dark;
    }

    /**=========================GUI RELATED FUNCTIONS*/

    //popup specifically for main activity use, when long pressed a card, this function will display the preview of the cipher text
    public void popup_cipher_preview(String cipherText)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pop_cipher_preview, null);
        TextView previewCipherText = view.findViewById(R.id.previewCText);
        previewCipherText.setText(cipherText);

        reinstateBuilder();

        popUpWindow.setView(view);
        alertDialog = popUpWindow.create();
        alertDialog.show();

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    //cancel alert dialog on press release
                    alertDialog.cancel();
                }

                return true;
            }
        });
    }

    //plain custom pop up container
    public AlertDialog popup_custom(String title, View view)
    {

        if(view.getParent() != null)
        {
            ((ViewGroup)view.getParent()).removeView(view);
        }

        reinstateBuilder();

        popUpWindow.setTitle(title);
        popUpWindow.setView(view);

        alertDialog = popUpWindow.create();

        return alertDialog;
    }

    //custom popup with defined positive and negative button behaviour
    public void popup_custom(String title, View view,String positiveText, String negativeText, DialogInterface.OnClickListener clickListener)
    {

        if(view.getParent() != null)
        {
            ((ViewGroup)view.getParent()).removeView(view);
        }

        reinstateBuilder();

        popUpWindow.setTitle(title);
        popUpWindow.setView(view);

        popUpWindow.setPositiveButton(positiveText, clickListener);
        popUpWindow.setNegativeButton(negativeText, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        popUpWindow.show();
    }

    public void popup_custom(String title, View view, DialogInterface.OnClickListener clickListener)
    {

        if(view.getParent() != null)
        {
            ((ViewGroup)view.getParent()).removeView(view);
        }

        reinstateBuilder();

        popUpWindow.setTitle(title);
        popUpWindow.setView(view);

        popUpWindow.setPositiveButton("OK", clickListener);
        popUpWindow.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        popUpWindow.show();
    }

    public void popup_getNumber_show(String popup_title, String popup_hint, DialogInterface.OnClickListener clickListener, int inputLengthLimit)
    {
        //build popup dialogue
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        //set up the input field
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        popup_inputText.setHint(popup_hint); //set hint on what value the user should enter
        popUpWindow.setView(popup_inputText);

        //set up positive button
        popUpWindow.setPositiveButton("OK",clickListener);

        //set up negative button
        popUpWindow.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        popUpWindow.show();
    }

    public String popup_getNumber_getResult()
    {
        return popUpNumberInput;
    }

    public void popup_show(String popup_title, String popup_hint, DialogInterface.OnClickListener clickListener)
    {
        //build popup dialogue
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        //set up the input field
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        popup_inputText.setHint(popup_hint); //set hint on what value the user should enter
        popUpWindow.setView(popup_inputText);

        //set up positive button
        popUpWindow.setPositiveButton("OK",clickListener);

        //set up negative button
        popUpWindow.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        popUpWindow.show();
    }

    /**CUSTOMIZEABLE pop up input text for positive button (right button) and negative button (left button)*/
    public void popup_show(String popup_title, String popup_hint, DialogInterface.OnClickListener positiveOCL,DialogInterface.OnClickListener negativeOCL, String positiveText, String negativeText)
    {
        //build popup dialogue
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        //set up the input field
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        popup_inputText.setHint(popup_hint); //set hint on what value the user should enter
        popUpWindow.setView(popup_inputText);

        //set up positive button
        popUpWindow.setPositiveButton(positiveText, positiveOCL);

        //set up negative button
        popUpWindow.setNegativeButton(negativeText, negativeOCL);

        popUpWindow.show();
    }

    public void popup_getNumber_show(String popup_title, String popup_hint, DialogInterface.OnClickListener positiveOCL,DialogInterface.OnClickListener negativeOCL, String positiveText, String negativeText)
    {
        //by default, the pop up dialogue content is text input
        //build popup dialogue
        reinstateBuilder();

        popUpWindow.setTitle(popup_title);

        //set up the input field
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
        popup_inputText.setHint(popup_hint); //set hint on what value the user should enter
        popUpWindow.setView(popup_inputText);

        //set up positive button
        popUpWindow.setPositiveButton(positiveText, positiveOCL);

        //set up negative button
        popUpWindow.setNegativeButton(negativeText, negativeOCL);

        popUpWindow.show();
    }

    /**
     * Use this method to get the user input in the popup dialogue
     * */
    public String popup_getInput()
    {
        return popup_inputText.getText().toString();
    }

    /**Use this for displaying small message, and error message as well*/
    public void system_message_small(String message) //use TOASTBOX FOR THIS
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**Pop up error message to display lengthy message, this can be used to display error message as well*/
    public void system_message_popup(String popup_title, String popup_message, String popup_buttonMessage)
    {
        DialogInterface.OnClickListener errMessageClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        };

        reinstateBuilder();

        popup_textView = new TextView(context);

        popUpWindow.setTitle(popup_title);
        popup_textView.setText(popup_message);
        popUpWindow.setPositiveButton(popup_buttonMessage, errMessageClickListener);
        popUpWindow.setView(popup_textView);
        popUpWindow.show();
    }
    public void system_message_popup(String popup_title, String popup_message) //by default, the text message in the popup button is "OK"
    {
        DialogInterface.OnClickListener errMessageClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        };

        reinstateBuilder();

        popup_textView = new TextView(context);

        popUpWindow.setView(popup_textView);
        popUpWindow.setTitle(popup_title);
        popup_textView.setText(popup_message);
        popUpWindow.setPositiveButton("OK", errMessageClickListener);
        popUpWindow.show();
    }

    public void system_message_confirmAction(String popup_title, String popup_message, DialogInterface.OnClickListener ols) //by default, the text message in the popup button is "OK"
    {
        reinstateBuilder();

        popup_textView = new TextView(context);

        popUpWindow.setView(popup_textView);
        popUpWindow.setTitle(popup_title);
        popup_textView.setText("\n"+popup_message);
        popup_textView.setTypeface(Typeface.DEFAULT_BOLD);
        popup_textView.setGravity(Gravity.CENTER_HORIZONTAL);
        popup_textView.setTextSize(18);
        popUpWindow.setPositiveButton("OK", ols);
        popUpWindow.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        popUpWindow.show();
    }






    /**==============OTHER FRAMEWORK FUNCTIONS*/
    //This will convert given String to the same string but without whitespace
    public String stringNoWhiteSpace(String cText)
    {
        String temp = new String();

        for(int i = 0; i < cText.length(); i++)
            if(cText.charAt(i) != ' ')

    temp+=cText.charAt(i);

        return temp;
    }


    /**
     * Read data from URI returned by the "OPEN_FILE_BROWSER" intent
     * */
    public String readTextFromUri(Uri uri, Context context) throws IOException
    {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            stringBuilder.append(line);

        return stringBuilder.toString();
    }


    /**READ AND WRITE CODE (Mark)*/
    private static Utility util = Utility.getInstance();

    private String ORIGINAL_TEXT;
    private String MODIFIED_TEXT;
    private int TEXT_COUNT;

    private String displayOriginalString() {
        return ORIGINAL_TEXT;
    }

    private String displayModifiedString () {
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

    /**THIS FUNCTION "CLEANS" THE INPUT STRING SO IT WILL BE SAFE TO USE IN MARK'S CODES
     * Delete stringNoWhiteSpace() later
     * */
    public String format (String input) {
        input = input.toLowerCase();
        input = input.replaceAll("[^A-Za-z]", "");
        return input;
    }

    //MY OWN DEFINED FUNCTION
    public String clean(String input)
    {
        init(input);
        return displayModifiedString();
    }
}
