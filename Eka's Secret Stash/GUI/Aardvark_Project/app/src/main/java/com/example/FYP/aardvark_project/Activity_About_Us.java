package com.example.FYP.aardvark_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Activity_About_Us extends AppCompatActivity {

     @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        overrideTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setAboutUsPage();
    }

   private void setAboutUsPage()
   {
       String text = "We are a small team of University of Wollongong students, doing our Final Year Project on Krypto.exe. Our goal is to produce a new and improved Android app based on the original Krypto.exe created by Prof Willy Susilo.";
       TextView aboutUsTextView = findViewById(R.id.about_us_text_view);
       aboutUsTextView.setText(text);
   }
    private void overrideTheme()
    {
        if(new App_Framework(this, false).isDarkTheme())
            this.setTheme(R.style.DarkTheme_NoActionBar);
        else
            this.setTheme(R.style.AppTheme_NoActionBar);
    }
}
