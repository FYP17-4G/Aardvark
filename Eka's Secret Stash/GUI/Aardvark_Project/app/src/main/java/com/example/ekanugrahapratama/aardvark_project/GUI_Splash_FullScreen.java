package com.example.ekanugrahapratama.aardvark_project;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GUI_Splash_FullScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        launchApp();
    }

    private void launchApp()
    {
        int DELAY_TIME = 2000;

        //remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash__full_screen);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                callDispatcher();
            }
        }, DELAY_TIME);
    }

    private void callDispatcher()
    {
        Class<?> activityClass;

        String projectName = new String();
        String projectID = new String();

        try {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            activityClass = Class.forName(
                    prefs.getString("lastActivity", GUI_MainActivity.class.getName()));

            projectName = prefs.getString("lastActivity_title", "**NO TITLE**");
            projectID = prefs.getString("lastActivity_id", "**NO ID**");
        } catch(ClassNotFoundException ex) {
            activityClass = GUI_MainActivity.class;
        }

        Intent intent = new Intent(this, activityClass);

        intent.putExtra("project_view_params", projectName);//this will pass on variables to the new activity, access it using the "name" (first param in this function)
        intent.putExtra("project_view_title", projectName);

        if(projectName.equals("**NO TITLE**"))
            intent = new Intent(this, GUI_MainActivity.class);

        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_transition_slide_from_bottom, R.anim.anim_transition_slide_to_top);
    }
}