package com.example.ekanugrahapratama.aardvark_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.io.BufferedReader;

public class projectView extends AppCompatActivity {

    private String cipherText;

    //and some parameters here

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        //ACCESS THE PASSED PARAMETERS FROM adaptr.java
        //COUT IT AS A TEST
        //String passedValue = getIntent().getStringExtra("project_view_params");
        //System.out.println("Passed Value: " + passedValue);
        //TextView tv = (TextView) findViewById(R.id.textViewTest);
        //tv.setText(passedValue);

        //TODO(111) get content from HARDCODED text file, dont forget to change this when the database is up

    }


    //TODO(222) add functions for tools
}
