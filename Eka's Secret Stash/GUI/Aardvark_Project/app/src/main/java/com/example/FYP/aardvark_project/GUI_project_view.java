package com.example.FYP.aardvark_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

//TODO() BUILD UNDO - RESET CIPHER TEXT STATE MECHANISM

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project_view_action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case android.R.id.home:
                backToList();
            case R.id.menu_item_note:
                launchNote();
        }*/

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
        framework = new App_Framework(this, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        getValuesFromSharedPrefs();

        setTitle(this.projectTitle);

        setBundleForMainView();
        //setBundleForPermutationView();

        projectViewFragmentAdapter = new projectView_fragmentPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPager_projectView);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(tabSelectedListener);

        //this disables the back button at the upper left screen
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    /**Functions for onCreate()*/
    private void getValuesFromSharedPrefs()
    {
        //ACCESS THE PASSED PARAMETERS FROM GUI_adaptr.java
        this.projectUniqueID = getIntent().getStringExtra("project_view_unique_ID");
        this.projectTitle = getIntent().getStringExtra("project_view_title");

        //check if the value of ID and title is null, if so, exit the application and output error message
        if(this.projectUniqueID.isEmpty() || this.projectTitle.isEmpty())
        {
            finish();
            Log.e("Unexpected Error", "Project Title or ID is NULL");
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


    private void launchNote()
    {
        Intent intent = new Intent(this, GUI_note.class);
        intent.putExtra("title", projectTitle);
        intent.putExtra("id", projectUniqueID);
        startActivity(intent);
    }

    //SETUP VIEW PAGER FOR TAB
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
