package com.example.ekanugrahapratama.aardvark_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This is class contains methods that simplifies the process of building the application
 */

public class App_Framework
{
    private Context context;

    //Pop up window related variables
    private AlertDialog.Builder popUpWindow;
    private TextView popup_textView;

    //pop up view object variables
    private EditText popup_inputText;
    private RecyclerView.Adapter adapter;

    private String popUpNumberInput = new String();

    public App_Framework(Context context)
    {
        this.context = context;
    }

    //displays selection list pop up menu
    public void popup_selectionChoice_show(ArrayList<String> content)
    {
        adapter = new RecyclerView.Adapter()
        {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
    }

    public void popup_getNumber_show(String popup_title, String popup_hint, DialogInterface.OnClickListener clickListener, int inputLengthLimit)
    {
        //build popup dialogue
        popUpWindow = new AlertDialog.Builder(context);
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
        popUpWindow = new AlertDialog.Builder(context);
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
        popUpWindow = new AlertDialog.Builder(context);
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
        popUpWindow = new AlertDialog.Builder(context);
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

        popUpWindow = new AlertDialog.Builder(context);
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

        popUpWindow = new AlertDialog.Builder(context);
        popup_textView = new TextView(context);

        popUpWindow.setView(popup_textView);
        popUpWindow.setTitle(popup_title);
        popup_textView.setText(popup_message);
        popUpWindow.setPositiveButton("OK", errMessageClickListener);
        popUpWindow.show();
    }

    public void system_message_confirmAction(String popup_title, String popup_message, DialogInterface.OnClickListener ols) //by default, the text message in the popup button is "OK"
    {   popUpWindow = new AlertDialog.Builder(context);
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

    //This will convert given String to the same string but without whitespace
    public String stringNoWhiteSpace(String cText)
    {
        String temp = new String();

        for(int i = 0; i < cText.length(); i++)
            if(cText.charAt(i) != ' ')
                temp+=cText.charAt(i);

        return temp;
    }
}
