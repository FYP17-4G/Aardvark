package com.example.FYP.aardvark_project.GUI;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.FYP.aardvark_project.Common.AppFramework;
import com.example.FYP.aardvark_project.R;

public class Activity_Splash_Screen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overrideTheme();
        super.onCreate(savedInstanceState);

        getWindow().setNavigationBarColor(Color.WHITE);

        launchApp();
    }

    private void overrideTheme() {
        if(new AppFramework(this, false).isDarkTheme())
            this.setTheme(R.style.DarkTheme_NoActionBar);
        else
            this.setTheme(R.style.AppTheme_NoActionBar);
    }

    private void launchApp() {
        int DELAY_TIME = 2000;

        requestWindowFeature(Window.FEATURE_NO_TITLE); //remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_full_screen);

        new Handler().postDelayed(() -> callDispatcher(), DELAY_TIME);
    }

    /**This class reads the last opened activity from Shared preferences and open it*/
    private void callDispatcher() {
        Class<?> activityClass;

        String projectName = new String();
        String projectID = new String();

        if(!callOnBoardActivity()) {
            try {
                SharedPreferences prefs = getSharedPreferences("PREF_SESSION", MODE_PRIVATE);
                activityClass = Class.forName(
                        prefs.getString("lastActivity", Activity_Main.class.getName()));

                projectName = prefs.getString("lastActivity_title", "**NO TITLE**");
                projectID = prefs.getString("lastActivity_id", "**NO ID**");
            } catch(ClassNotFoundException ex) {
                activityClass = Activity_Main.class;
            }

            Intent intent;

            if(projectName.equals("**NO TITLE**") && projectID.equals("**NO ID**"))
                intent = new Intent(this, Activity_Main.class);
            else {
                /**intent = new Intent(this, activityClass); //this will start 'GUI_Fragment_Project_View' with its corresponding ID and title*/
                intent = new Intent(this, activityClass);

                intent.putExtra("project_view_unique_ID", projectID);//this will pass on variables to the new activity, access it using the "name" (first param in this function)
                intent.putExtra("project_view_title", projectName);
            }

            startActivity(intent);
            //overridePendingTransition(R.anim.anim_transition_slide_from_bottom, R.anim.anim_transition_slide_to_top);
        }

        finish(); //finish() prevents the user to be able to go back to splash screen activity
    }

    /**
     * This will call Activity_Introduction activity when the user opens the app
     * for the very first time
     * */
    private boolean callOnBoardActivity() {
        final String ON_BOARDING_SHARED_PREFS = "On boarding shared preference";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getBoolean(ON_BOARDING_SHARED_PREFS, false)) {
            startActivity(new Intent(this, Activity_Introduction.class)); // The user hasn't seen the OnboardingFragment yet, so show it
            return true;
        }

        return false;
    }
}