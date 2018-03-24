package com.example.FYP.aardvark_project.GUI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.FYP.aardvark_project.Common.AppFramework;
import com.example.FYP.aardvark_project.R;

public class Activity_About_Us extends AppCompatActivity {

    private LinearLayout linearLayout;

    private String HYPERLINK_GRAPH_VIEW = "<a href='https://www.android-graphview.org//'>Graph View</a>";
    private String HYPERLINK_INTRO_ACTIVITY = "<a href='https://github.com/msayan/tutorial-view'>Tutorial View</a>";
    private String HYPERLINK_SLIDING_UP_PANEL = "<a href='https://github.com/umano/AndroidSlidingUpPanel'>Sliding Up View Panel</a>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getWindow().setNavigationBarColor(Color.WHITE);

        linearLayout = findViewById(R.id.about_us_linear_layout);

        /**Build external libraries text view*/

        linearLayout.addView(addTextHyperlink(HYPERLINK_GRAPH_VIEW));
        linearLayout.addView(addTextHyperlink(HYPERLINK_INTRO_ACTIVITY));
        linearLayout.addView(addTextHyperlink(HYPERLINK_SLIDING_UP_PANEL));
    }

    private TextView addTextHyperlink(String s){
        TextView textView = new TextView(this);
        textView.setText(Html.fromHtml(s));
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView.setPadding(97, 0, 0, 25);

        return textView;
    }
}
