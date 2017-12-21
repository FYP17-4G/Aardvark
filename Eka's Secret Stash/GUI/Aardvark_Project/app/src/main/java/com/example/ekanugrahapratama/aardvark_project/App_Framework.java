package com.example.ekanugrahapratama.aardvark_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import android.content.Context;
import android.view.View;

/**
 * This is class contains methods that simplifies the process of building the application
 */

public class App_Framework
{
    private Context context;

    public App_Framework(Context c)
    {
        this.context = c;
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

    private EditText popup_inputText;
    public void popup_show(String popup_title, String popup_hint, DialogInterface.OnClickListener clickListener)
    {
        //build popup dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(popup_title);

        //set up the input field
        popup_inputText = new EditText(context);
        popup_inputText.setInputType(InputType.TYPE_CLASS_TEXT);
        popup_inputText.setHint(popup_hint); //set hint on what value the user should enter
        builder.setView(popup_inputText);

        //set up positive button
        builder.setPositiveButton("OK",clickListener);

        //set up negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }
    /**
     * Use this method to get the user input in the popup dialogue
     * */
    public String popup_getInput()
    {
        return popup_inputText.getText().toString();
    }

    /**Use this for displaying minor warnings and other small stuff*/
    public void errMessage_small(String message) //use TOASTBOX FOR THIS
    {

    }

    /**Pop up error message to display lengthy error message, or a severe one, contains only message and "OK" button*/
    public void errrMessage_popup(String title, String message)
    {

    }
}
