/**
 * Programmer: Eka Nugraha Pratama
 *
 * Contains the source code for Introduction activity
 * This activity is only called once upon fresh instalation of the application.
 * (like a short application info and app tour guide)
 * */

package com.example.FYP.aardvark_project.GUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.FYP.aardvark_project.R;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class Activity_Introduction extends TutorialActivity {

    private final String HEADER1 = "Welcome to CRYPTOAW";
    private final String CONTENT1 = "Thank you for downloading CRYPTOAW.\n This application is developed by a team of students from University of Wollongong as our final year project";
    private final String HEX1 = "#000080"; //navy blue
    private final int IMAGE1 = R.drawable.ic_menu_camera;
    private final String SUMMARY1 = "Lets get started!";

    private final String HEADER2 = "What this app has for you";
    private final String CONTENT2 = "This application helps you analyze and break classical cryptography ciphers, with intuitive UI <more on this>";
    private final String HEX2 = "#dcd9cd"; // milk white
    private final int IMAGE2 = R.drawable.ic_menu_gallery;
    private final String SUMMARY2 = "<Summary 2>";

    private final String HEADER3 = "Anything else?";
    private final String CONTENT3 = "You can Encrypt and Decrypt texts and messages using various classical cryptography ciphers too";
    private final String HEX3 = "#228B22"; //forest green
    private final int IMAGE3 = R.drawable.ic_action_action_search;
    private final String SUMMARY3 = "Use this to amaze your friends!";

    @Override
    public void finishTutorial() {
        Intent intent = new Intent(this, Activity_Main.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        seenByUser();

        addIntroFragment(HEADER1, CONTENT1, HEX1, IMAGE1, SUMMARY1); //layout 1
        addIntroFragment(HEADER2, CONTENT2, HEX2, IMAGE2, SUMMARY2); //layout 2
        addIntroFragment(HEADER3, CONTENT3, HEX3, IMAGE3, SUMMARY3); //layout 3
    }

    private void addIntroFragment(String title, String content, String hexBGColor, int drawable, String summary) {
        addFragment(new Step.Builder().setTitle(title)
                .setContent(content)
                .setBackgroundColor(Color.parseColor(hexBGColor)) // int background color
                .setDrawable(drawable) // int top drawable
                .setSummary(summary)
                .build());

        super.getWindow().setNavigationBarColor(Color.parseColor(hexBGColor));
    }

    /**
     * Set shared preference of this activity as true, meaning the user has seen introduction activity
     * and wont be shown this activity again upon opening the application
     * */
    private void seenByUser() {
        final String ON_BOARDING_SHARED_PREFS = "On boarding shared preference";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ON_BOARDING_SHARED_PREFS, true);
        editor.commit();
    }
}
