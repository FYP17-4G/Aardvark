package com.example.ekanugrahapratama.aardvark_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

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
                Intent mainIntent = new Intent(GUI_Splash_FullScreen.this, GUI_MainActivity.class);
                GUI_Splash_FullScreen.this.startActivity(mainIntent);
                GUI_Splash_FullScreen.this.finish();//so the user cant go back to this activity
                overridePendingTransition(R.anim.anim_transition_slide_from_bottom, R.anim.anim_transition_slide_to_top);
            }
        }, DELAY_TIME);
    }
}