package com.example.FYP.aardvark_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class GUI_aboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String text = "We are a small team of University of Wollongong students, doing our Final Year Project on Krypto.exe. Our goal is to produce a new and improved Android app based on the original Krypto.exe created by Prof Willy Susilo.";
        TextView aboutUsTextView = (TextView)findViewById(R.id.about_us_text_view);
        aboutUsTextView.setText(text);
        }
}
