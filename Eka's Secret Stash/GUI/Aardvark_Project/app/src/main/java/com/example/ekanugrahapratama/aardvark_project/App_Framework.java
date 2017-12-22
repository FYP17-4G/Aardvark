package com.example.ekanugrahapratama.aardvark_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Context;

/**
 * This is class contains methods that simplifies the process of building the application
 */

public class App_Framework
{
    private Context context;

    //Pop up window related variables
    private AlertDialog.Builder popUpWindow;
    private EditText popup_inputText;
    private TextView popup_textView;

    public App_Framework(Context context)
    {
        this.context = context;
    }

    /**
     * [This will create a standard popup dialogue]
     *
     * In order to do this, you have to pass in onClickListener variable that defines specific action to this method
     * e.g:
     *      App_Framework framework;
     *      DialogueInterface.onClickListener myListener = new DialogueInterface.onClickListener(...Define on click listener action here...);
     *      framework.popup_getInput("Title", "Hint", myListener);
     * */

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

        popUpWindow.setTitle(popup_title);
        popup_textView.setText(popup_message);
        popUpWindow.setPositiveButton("OK", errMessageClickListener);
    }
}
