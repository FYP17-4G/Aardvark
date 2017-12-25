package com.example.ekanugrahapratama.aardvark_project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Random;
import java.util.ArrayList;

public class GUI_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private App_Framework framework = new App_Framework(this);

    private GUI_adaptr adapter;
    private RecyclerView list; //the 'array' of project list

    //this is the text file where the project list contents are stored. The contents will be used by recycler view in the main menu
    private String projectDirectoryFileName = "projectDirectory.txt";

    //front page identifier = struct like class for the adapter to simplify the data reading
    private ArrayList<frontPageIdentifier> projectTitle = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RECYCLER HERE---------------------
        //fill in the projectTitle
        //TODO(1) Fill in the project title container with ID||Title from list.txt file

        BufferedReader fileIn;

        //get the contents for the recycler view
        try
        {
            FileInputStream fis = openFileInput(projectDirectoryFileName);
            fileIn = new BufferedReader(new InputStreamReader(fis));

            String line;
            String[] substr;//use this variable when splitting 'line'


            while((line = fileIn.readLine()) != null)
            {
                //process the line
                substr = line.split("\\|\\|");

                frontPageIdentifier fpi = new frontPageIdentifier(substr[0], substr[1]);
                projectTitle.add(fpi);
            }

            fileIn.close();
        }catch(IOException e){}

        //TODO(2) Use encryption to secure list.txt <<<<<<<<<<<<<< IMPORTANTER



        //the rest
        list = (RecyclerView) findViewById(R.id.rv_numbers);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        list.setLayoutManager(layout);

        list.setHasFixedSize(true);

        adapter = new GUI_adaptr(projectTitle);
        list.setAdapter(adapter);
        //---------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createNewProject();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_encryption_decryption)
        {
            launchEncryptionDecryptionActivity();
        }
        else if (id == R.id.nav_about_us)
        {
            launchAboutUsActivity();
        }
        else if (id == R.id.nav_settings)
        {
            launchSettingsActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchEncryptionDecryptionActivity()
    {
        Intent intent = new Intent(this, GUI_Enc_Dec.class);
        startActivity(intent);
    }

    private void launchAboutUsActivity()
    {
        //TODO(me) GUI for about us activity
        /**Just create empty activity with a paragraph of text and some contact info*/
    }
    private void launchSettingsActivity()
    {
        //TODO(me) GUI for settings activity
        /**For settings, use "Settings template"*/
    }

    private void createNewProject()
        {
           DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener()
           {
               @Override
               public void onClick(DialogInterface dialogInterface, int i)
               {

                   if(framework.popup_getInput().isEmpty())
                       framework.system_message_small("Error: Please enter a project name");

                   if(projectExist(framework.popup_getInput()))
                       framework.system_message_small("Error: Project title already exist");

                   else
                   {
                       String newProjectTitle = framework.popup_getInput();
                       writeToList(newProjectTitle);
                       adapter.notifyDataSetChanged();//refresh the adapter

                       //TODO(3) ONCE THE DATABASE IS UP, CREATE ASSOCIATED DATA OF THIS ITEM
                       //<...>
                   }
               }

           };

           framework.popup_show("Create new Project", "New Project Title", ocl);
        }

    protected boolean projectExist(String newProjectTitle)
    {
        for(int i = 0; i < projectTitle.size(); i++)
            if(projectTitle.get(i).getTitle().equals(newProjectTitle))
                return true;

        return false;
    }

    /**This will store the data to a text file that contains information to display the list of existing projects*/
    //FILE STORAGE WILL BE HANDLED INTERNALLY BY ANDROID
    private void writeToList(String newProjectTitle)
        {
            //add the new project into the arrayList
            frontPageIdentifier newProject = new frontPageIdentifier(Integer.toString(new Random().nextInt(999)+1), newProjectTitle);
            projectTitle.add(newProject);

            //overwrite list.txt
            BufferedWriter outputFile;

            String newID = newProject.getID();
            String newTitle = newProject.getTitle();

            try
                {
                    FileOutputStream fos = openFileOutput(projectDirectoryFileName, MODE_APPEND);
                    outputFile = new BufferedWriter(new OutputStreamWriter(fos));
                    outputFile.write(newID + "||" + newTitle+"\n");
                    outputFile.close();
                }catch(IOException e)
                    {}

        }

    private void writeToDB()
        {}
}
