/**
 * Programmer: Eka Nugraha Pratama
 *
 * This is the activity for crypto projects. This activity only does the initialization of the view layout behaviour (e.g: navigation panel, the "triple dot" button
 * at the upper left of the screen, etc)
 *
 * For the source code that does the set up of the actual crypto tools, see "Fragment_project_view"
 * */

package com.example.FYP.aardvark_project.GUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.FYP.aardvark_project.R;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Project_View extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private String projectUniqueID = new String();
    private String projectTitle = new String();

    private FragmentPagerAdapter projectViewFragmentAdapter;

    private Fragment_project_view mainView = new Fragment_project_view();

    private App_Framework framework;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_frontPage)
            launchFrontPageActivity();

        else if (id == R.id.nav_encryption_decryption)
            launchEncryptionDecryptionActivity();

        else if (id == R.id.nav_about_us)
            launchAboutUsActivity();

        else if (id == R.id.nav_settings)
            launchSettingsActivity();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_view_action_bar_menu, menu); // Inflate the menu; this adds items to the action bar if it is present
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.home)
            backToList();
        else if(item.getItemId() == R.id.menu_item_note)
            launchNoteActivity();
        else if(item.getItemId() == R.id.menu_item_reset)
            mainView.reset();
        else if(item.getItemId() == R.id.menu_item_save)
            mainView.updateCipherTextToDB();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToList();
    }

    /**
     * If the user pauses the app(e.g: pressing home button), the app will save the state of which
     * activity the user was at last.So the user will be able to continue from where they left off
     * even after the user exits the app
     * */
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
    protected void onCreate(Bundle savedInstanceState) {
        framework = new App_Framework(this, false);
        overrideTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        getCompositeParams(); //get this project ID and title (the composite params) passed from main activity
        setTitle(this.projectTitle);

        setNavDrawer();

        setBundleForMainView(); //set parameters to be transferred to project view fragment
        projectViewFragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        setupViewPager(findViewById(R.id.viewPager_projectView));

        initializeAutosave();
    }
    /**Use this because this class needs "no action bar" theme for it to work */
    private void overrideTheme() {
        if(framework.isDarkTheme())
            this.setTheme(R.style.DarkTheme_NoActionBar);
        else
            this.setTheme(R.style.AppTheme_NoActionBar);
    }

    private void initializeAutosave() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        mainView.updateCipherTextToDB();
                        framework.system_message_small("Progress saved (autosave)");
                    }
                    catch (Exception e) {
                        framework.system_message_small(e.getMessage());
                    }
                });
            }
        };

        timer.schedule(doAsynchronousTask, 600000); //autosaves every 10 minutes
    }

    /**Functions for onCreate()*/
    private void getCompositeParams() {
        this.projectUniqueID = getIntent().getStringExtra("project_view_unique_ID");
        this.projectTitle = getIntent().getStringExtra("project_view_title");

        if(this.projectUniqueID.isEmpty() || this.projectTitle.isEmpty()) { //check if the value of ID and title is null, if so, exit the application and output error message
            finish();
            System.exit(0);
        }
    }

    /**
     * This function sets project title and ID to a bundle object, and will be used in project view fragment
     * */
    private void setBundleForMainView() {
        Bundle bundle = new Bundle();
        bundle.putString("title", this.projectTitle);
        bundle.putString("id", this.projectUniqueID);
        mainView.setArguments(bundle);
    }

    /**Setup Tab view pager*/
    private void setupViewPager(ViewPager viewPager) {
        projectViewFragmentAdapter.addFragment(mainView, "Main View");
        viewPager.setAdapter(projectViewFragmentAdapter);
    }

    private void launchNoteActivity() {
        Intent intent = new Intent(this, Activity_Note.class);
        intent.putExtra("title", projectTitle);
        intent.putExtra("id", projectUniqueID);
        startActivity(intent);
    }

    /**Navigate to the main menu activity*/
    private void backToList()
    {
        startActivity(new Intent(this, Activity_Main.class));
    }

    /**Navigation panel functions*/
    private void setNavDrawer() {
        View toolBarView = findViewById(R.id.project_view_app_bar_include);
        Toolbar toolbar = toolBarView.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true); //highlights "front page" item menu
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void launchFrontPageActivity()
    {
        startActivity(new Intent(this, Activity_Main.class));
    }

    private void launchEncryptionDecryptionActivity() {
        startActivity(new Intent(this, Activity_Enc_Dec.class));
    }

    private void launchAboutUsActivity() {
        this.startActivity(new Intent(this, Activity_About_Us.class));
    }
    private void launchSettingsActivity() {
        this.startActivity(new Intent(this, Activity_Settings.class));
    }
}
