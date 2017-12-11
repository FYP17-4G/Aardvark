package com.example.ekanugrahapratama.aardvark_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.content.res.AssetManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private adaptr adapter;
    private RecyclerView list; //the 'array' of project list

    //this is the text file where the project list contents are stored. The contents will be used by recycler view in the main menu
    private String projectDirectoryFileName = "projectDirectory.txt";



    //TODO(999) FIGURE OUT HOW TO USE SHIFT CIPHER

    //TODO(0)Replace String data type of this array list to a struct like class containing ID:int and title:String
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

        adapter = new adaptr(projectTitle);
        list.setAdapter(adapter);

        //---------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO(3) Do something with floating action button => Add new entry into projectTitle arrayList, rewrite list.txt, and refresh (re fetch data from list.txt)
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNewProject()
        {

            //build popup dialogue
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create new project");

            //set up the input field
            final EditText inputText = new EditText(this);
            inputText.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(inputText);

            //set up positive button
            builder.setPositiveButton("Create", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    String newProjectTitle = inputText.getText().toString();
                    writeToList(newProjectTitle);
                    adapter.notifyDataSetChanged();//refresh the adapter
                }
            });

            //set up negative button
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                }
            });

            builder.show();
        }

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
}
