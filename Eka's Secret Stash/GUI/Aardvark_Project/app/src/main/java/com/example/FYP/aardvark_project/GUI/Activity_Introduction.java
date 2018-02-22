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

    private final int IMG1 = R.mipmap.intro_welcome;
    private final String HEX1 = "#9D9ABF";

    private final int IMG2 = R.mipmap.intro_projects;
    private final String HEX2 = "#E8C04F";

    private final int IMG3 = R.mipmap.intro_analytical_tools;
    private final String HEX3 = "#D2514B";

    private final int IMG4 = R.mipmap.intro_enc_dec;
    private final String HEX4 = "#66A4C6";

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

        addIntroFragment(IMG1, HEX1);
        addIntroFragment(IMG2, HEX2);
        addIntroFragment(IMG3, HEX3);
        addIntroFragment(IMG4, HEX4);

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

    private void addIntroFragment(int drawable, String hex){
        addFragment(new Step.Builder().setDrawable(drawable).setBackgroundColor(Color.parseColor(hex)).build());
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
