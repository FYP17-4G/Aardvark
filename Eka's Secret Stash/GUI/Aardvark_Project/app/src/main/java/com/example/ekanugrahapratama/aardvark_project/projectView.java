package com.example.ekanugrahapratama.aardvark_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.ekanugrahapratama.aardvark_project.kryptoTools.CalculateIC;
import com.example.ekanugrahapratama.aardvark_project.kryptoTools.ShiftCipher;

public class projectView extends AppCompatActivity {

    private String cipherText = new String();
    TextView cipherTextView;

    //Tools variables here

    //CAESAR CIPHER
    Spinner caesarSpinner;
    ShiftCipher caesarCipher = new ShiftCipher();
    int shiftCipherBy = 0;

    Button caesarShiftRight;
    Button caesarShiftLeft;

    //IC CALCULATION
    CalculateIC ic;
    TextView cipherICTV;
    double cipherIC = 0;

    //<...>

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        //ACCESS THE PASSED PARAMETERS FROM adaptr.java

        String projectUniqueID = getIntent().getStringExtra("project_view_unique_ID");

        //SET UP TOOLS FOR THE PROJECT
        setCaesarTool();

        getCipherTextFromFile(); //TODO(***) JUST WHY????

        cipherICTV = (TextView) findViewById(R.id.cipherICTextView);
        ic = new CalculateIC();
        calculateCipherIC();
    }

    //TODO(111) get content from HARDCODED text file, dont forget to change this when the database is up
    //TEST_CIPHER.txt is inside "assets" folder. DELETE IT LATER AND CHANGE IT ACCORDINGLY
    private void getCipherTextFromFile()
    {
        BufferedReader fileIn;
        String line;

        try
        {
            fileIn = new BufferedReader(new InputStreamReader(getAssets().open("TEST_CIPHER.txt")));
            while((line = fileIn.readLine()) != null)
            {
                System.out.println(">>>> " + line);
                cipherText += line;
                cipherTextView.append(line+"\n");
            }

            fileIn.close();
        }catch(IOException e)
        {}catch(NullPointerException n)
        {
            System.out.println("[ERROR NULL POINTER EXCEPTION]");
            System.exit(404);
        }
    }

    //This will convert given String to the same string but without whitespace
    private String stringNoWhiteSpace(String cText)
    {
        String temp = new String();

        for(int i = 0; i < cText.length(); i++)
            if(cText.charAt(i) != ' ')
                temp+=cText.charAt(i);

        return temp;
    }

    private void calculateCipherIC()
    {
        cipherIC = ic.getIC(stringNoWhiteSpace(cipherText));
        cipherICTV.setText(Double.toString(cipherIC));
    }

    private void setCaesarTool()
    {
        cipherTextView = (TextView) findViewById(R.id.project_view_cipher_text);
        caesarShiftLeft = (Button) findViewById(R.id.button_caesarShiftL);
        caesarShiftRight = (Button) findViewById(R.id.button_caesarShiftR);

        caesarSpinner = (Spinner) findViewById(R.id.caesarSpinner);
        caesarSpinner.setOnItemSelectedListener(spinnerListener);

        caesarShiftLeft.setOnClickListener(shiftListener);
        caesarShiftRight.setOnClickListener(shiftListener);

        List<Integer> content = new ArrayList<>();
        for(int i = 0; i < 26; i++)
            content.add(i);

        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, content);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        caesarSpinner.setAdapter(spinnerAdapter);

    }

    View.OnClickListener shiftListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
            {
                if(v.getId() == R.id.button_caesarShiftR)
                    cipherText = caesarCipher.encrypt(cipherText, shiftCipherBy);

                else
                    cipherText = caesarCipher.decrypt(cipherText, shiftCipherBy);

                cipherTextView.setText(cipherText);
            }
    };

    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
        {
        shiftCipherBy = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView)
        {

        }
    };
}
