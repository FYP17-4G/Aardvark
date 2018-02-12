package com.example.FYP.aardvark_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Activity_Project_View extends AppCompatActivity
{
    private String projectUniqueID = new String();
    private String projectTitle = new String();

    private fragmentPagerAdapter projectViewFragmentAdapter;
    private ViewPager viewPager;

    private fragment_project_view mainView = new fragment_project_view();
    private fragment_project_view_permutation permView = new fragment_project_view_permutation();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_view_action_bar_menu, menu); // Inflate the menu; this adds items to the action bar if it is present
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home)
            backToList();
        else if(item.getItemId()== R.id.menu_item_note)
            launchNote();

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

        SharedPreferences prefs = getSharedPreferences("PREF_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.putString("lastActivity_title", projectTitle);
        editor.putString("lastActivity_id", projectUniqueID);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        new App_Framework(this, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        getValuesFromSharedPrefs();

        setTitle(this.projectTitle);

        setBundleForMainView();

        projectViewFragmentAdapter = new fragmentPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.viewPager_projectView);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(tabSelectedListener);

        getSupportActionBar().setElevation(0); //disable toolbar drop down shadow
    }

    /**Listener for when the tab is changed, main page in project view will pass the data to permutation view*/
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener()
    {
        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            if(tab.getPosition() == 1) //if permutation view is selected
            {
                AsyncTask.execute(() -> {
                    permView.setCipherText(mainView.getCipherText());
                    permView.setOriginalCipherText(mainView.getOriginalCipherText());
                    permView.refresh(mainView.getCipherText(), 0, 0);
                });
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    /**Functions for onCreate()*/
    private void getValuesFromSharedPrefs()
    {
        this.projectUniqueID = getIntent().getStringExtra("project_view_unique_ID");
        this.projectTitle = getIntent().getStringExtra("project_view_title");

        if(this.projectUniqueID.isEmpty() || this.projectTitle.isEmpty()) //check if the value of ID and title is null, if so, exit the application and output error message
        {
            Log.e("GUI_Project_View ERROR", "Project Title or ID is NULL");
            System.out.println("Project Title or ID is NULL");
            finish();
            System.exit(0);
        }
    }
    private void setBundleForMainView()
    {
        Bundle bundle = new Bundle();
        bundle.putString("title", this.projectTitle);
        bundle.putString("id", this.projectUniqueID);
        mainView.setArguments(bundle);
    }

    /**Setup Tab view pager*/
    private void setupViewPager(ViewPager viewPager)
    {
        projectViewFragmentAdapter.addFragment(mainView, "Main View");
        projectViewFragmentAdapter.addFragment(permView, "Permutation View");
        viewPager.setAdapter(projectViewFragmentAdapter);
    }

    private void launchNote()
    {
        Intent intent = new Intent(this, Activity_Note.class);
        intent.putExtra("title", projectTitle);
        intent.putExtra("id", projectUniqueID);
        startActivity(intent);
    }

    private void backToList()
    {
        startActivity(new Intent(this, Activity_Main.class));
    }
}
