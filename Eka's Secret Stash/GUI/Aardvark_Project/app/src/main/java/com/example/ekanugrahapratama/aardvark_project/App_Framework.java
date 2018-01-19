package com.example.ekanugrahapratama.aardvark_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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

    //plain custom pop up container
    public void popup_custom(String title, View view)
    {

        if(view.getParent() != null)
        {
            ((ViewGroup)view.getParent()).removeView(view);
        }

        popUpWindow = new AlertDialog.Builder(context);
        popUpWindow.setView(view);
        popUpWindow.show();
    }

    //custom popup with defined positive and negative button behaviour
    public void popup_custom(String title, View view,String positiveText, String negativeText, DialogInterface.OnClickListener clickListener)
    {

        if(view.getParent() != null)
        {
            ((ViewGroup)view.getParent()).removeView(view);
        }

        popUpWindow = new AlertDialog.Builder(context);
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

        popUpWindow = new AlertDialog.Builder(context);
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

    /**THIS FUNCTION "CLEANS" THE INPUT STRING SO IT WILL BE SAFE TO USE IN MARK'S CODES
     * Delete stringNoWhiteSpace() later
     * */
    public String init (String input) {
        input = input.toLowerCase();
        input = input.replaceAll("[^A-Za-z]", "");
        return input;
    }

    /**SAVE TO AND LOAD FROM TEXT FILE IN THE DEVICE STORAGE*/
    public void saveAsTxt(String filename, String input, boolean append)
    {
        //overwrite list.txt
        BufferedWriter outputFile;

        try
        {
            FileOutputStream fos;

            if(append)
                fos = context.openFileOutput(filename, context.MODE_APPEND);
            else
                fos = context.openFileOutput(filename, context.MODE_PRIVATE);

            outputFile = new BufferedWriter(new OutputStreamWriter(fos));
            outputFile.write(input + "\n");
            outputFile.close();
        }catch(IOException e)
        {}
    }

    public void saveAsTxt(String filename, List<String> input, boolean append)
    {
        //overwrite list.txt
        BufferedWriter outputFile;

        try
        {
            FileOutputStream fos;

            if(append)
                fos = context.openFileOutput(filename, context.MODE_APPEND);
            else
                fos = context.openFileOutput(filename, context.MODE_PRIVATE);

            outputFile = new BufferedWriter(new OutputStreamWriter(fos));

            for(int i = 0; i < input.size(); i++)
                outputFile.write(input.get(i) + "\n");

            outputFile.close();
        }catch(IOException e)
        {}
    }

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

    //this gets cipher text file in storage location managed by the application
    public String getTextFromFile(String filename)
    {
        String returnValue = new String();

        BufferedReader inputFile;

        try
        {
            FileInputStream fis = context.openFileInput(filename);

            inputFile = new BufferedReader(new InputStreamReader(fis));

            String line;
            while((line = inputFile.readLine()) != null)
            {
                returnValue += line;
            }
        }catch(IOException e)
        {System.out.println("[ERROR] Text file not found");}

        return returnValue;
    }

    public void deleteTextFile(String filename)
    {
        File file = context.getFileStreamPath(filename);
        file.delete();
    }

    /**WHEN RENAMING, IT NEEDS THE FULL PATH,
     *
     * OBTAIN THE FULL PATH BY file.toString();
     * */
    public void renameTextFile(String targetFile, String newFileName)
    {
        File file = context.getFileStreamPath(targetFile);

        //extract the path
        String path = file.getAbsolutePath();
        for(int i = path.length(); i > 0; i--)
        {
            if(path.charAt(i - 1) == '/')
                break;

            StringBuilder sb = new StringBuilder(path);
            sb.deleteCharAt(i - 1);
            path = sb.toString();
        }

        newFileName = path + newFileName;

        if(file.renameTo(new File(newFileName)))
            system_message_small("Renamed");
        else
            system_message_small("Rename failed");
    }
}
