package com.example.ekanugrahapratama.aardvark_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class GUI_project_view extends AppCompatActivity
{
    private String projectUniqueID = new String();
    private String projectTitle = new String();

    private projectView_fragmentPagerAdapter projectViewFragmentAdapter;
    private ViewPager viewPager;

    private GUI_fragment_project_view mainView = new GUI_fragment_project_view();
    private GUI_fragment_project_view_permutation permView = new GUI_fragment_project_view_permutation();

    private App_Framework framework;

    private void backToList()
    {
        startActivity(new Intent(this, GUI_MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToList();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backToList();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.putString("lastActivity_title", projectTitle);
        editor.putString("lastActivity_id", projectUniqueID);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        framework = new App_Framework(this);

        //ACCESS THE PASSED PARAMETERS FROM GUI_adaptr.java
        projectUniqueID = getIntent().getStringExtra("project_view_unique_ID"); //USE THIS LATRE
        this.projectTitle = getIntent().getStringExtra("project_view_title");
        setTitle(projectTitle);

        projectViewFragmentAdapter = new projectView_fragmentPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPager_projectView);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(tabSelectedListener);

    }

    private void setupViewPager(ViewPager viewPager)
    {
        projectViewFragmentAdapter.addFragment(mainView, "Main View");
        projectViewFragmentAdapter.addFragment(permView, "Permutation View");
        viewPager.setAdapter(projectViewFragmentAdapter);
    }

    /**Listener for when the tab is changed, main page in project view will pass the data to permutation view*/
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener()
    {
        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            permView.setCipherText(mainView.getCipherText());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}
