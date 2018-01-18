package com.example.ekanugrahapratama.aardvark_project;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Random;
import java.util.ArrayList;

public class GUI_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int READ_REQUEST_CODE = 42;

    private App_Framework framework = new App_Framework(this);

    private GUI_adaptr adapter;
    private RecyclerView list; //the 'array' of project list

    //this is the text file where the project list contents are stored. The contents will be used by recycler view in the main menu
    private String projectDirectoryFileName = "projectDirectory.txt";

    //front page identifier = struct like class for the adapter to simplify the data reading
    private ArrayList<frontPageIdentifier> projectTitle = new ArrayList();

    private Context context = this;

    private View newProjectView;
    private String cipherTextFromFile = new String(); //for use when the user enters a file as cipher text input

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newProjectView = getLayoutInflater().inflate(R.layout.pop_new_project, null);

        //clear shared preferenes
        SharedPreferences prefs = getSharedPreferences("PREF_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();


        //RECYCLER HERE---------------------
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

        if(id == R.id.nav_frontPage)
            launchFrontPageActivity();

        /*else*/if (id == R.id.nav_encryption_decryption)
            launchEncryptionDecryptionActivity();

        else if (id == R.id.nav_about_us)
             launchAboutUsActivity();

        else if (id == R.id.nav_settings)
            launchSettingsActivity();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchFrontPageActivity()
    {
        Intent intent = new Intent(this, GUI_MainActivity.class);
        startActivity(intent);
    }

    private void launchEncryptionDecryptionActivity()
    {
        Intent intent = new Intent(this, GUI_Enc_Dec.class);
        startActivity(intent);
    }

    private void launchAboutUsActivity()
    {
        /**Just create empty activity with a paragraph of text and some contact info*/
        Intent intent = new Intent(this, GUI_aboutUs.class);
        this.startActivity(intent);
    }
    private void launchSettingsActivity()
    {
        /**For settings, use "Settings template"*/
    }

    private void createNewProject()
        {
            int ID = new Random().nextInt(999)+1;

           //SETUP NEWPROJECTPOPUP ELEMENT HERE
           EditText newProjectTitle = newProjectView.findViewById(R.id.editText_newProjectNameField);
           EditText cipherTextInput = newProjectView.findViewById(R.id.editText_cipherInputField);

           Button getFromFile = newProjectView.findViewById(R.id.button_getCipherTextFromFile);
           getFromFile.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   openFileBrowser(); //this function will call default device file browser
               }
           });

            framework.popup_custom("Create new project", newProjectView, "create", "cancel", new DialogInterface.OnClickListener() {
                //get the cipher text input from input field
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                        String projectTitle = newProjectTitle.getText().toString();
                        String cipherText = cipherTextInput.getText().toString();

                        if(projectTitle.isEmpty())
                            framework.system_message_small("Project title is still empty");
                        else if(cipherText.isEmpty())
                            framework.system_message_small("Cipher text input is still empty");
                        else
                        {
                            writeToList(projectTitle, ID);

                            String filename = Integer.toString(ID) + projectTitle + "cipherTextOriginal.txt";

                            //saves ciphertext to a file
                            framework.saveAsTxt(filename , cipherText, context, false);
                            adapter.notifyDataSetChanged();//refresh the adapter
                        }
                }
            });
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
    private void writeToList(String newProjectTitle, int ID)
        {
            //add the new project into the arrayList
            frontPageIdentifier newProject = new frontPageIdentifier(Integer.toString(ID), newProjectTitle);
            projectTitle.add(newProject);

            String newID = newProject.getID();
            String newTitle = newProject.getTitle();

            framework.saveAsTxt(projectDirectoryFileName, newID + "||" + newTitle, this, true);
        }

    private void openFileBrowser()
    {
        String returnVal = new String();

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //START INTENT TO CHOOSE FILE USING DEVICE' DEFAULT FILE BROWSER
        intent.addCategory(Intent.CATEGORY_OPENABLE);//SHOW ONLY FILES THAT CAN BE OPENED
        intent.setType("text/plain"); //only plain txt file can be accessed


        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if(data != null) {
                //URI is the return value given by ACTION_OPEN_DOCUMENT intent
                Uri uri = data.getData();

                try
                {
                    cipherTextFromFile = framework.readTextFromUri(uri, context);

                } catch (IOException e)
                {}

                if(!cipherTextFromFile.isEmpty())
                {
                    EditText editText = newProjectView.findViewById(R.id.editText_cipherInputField);
                    editText.setText(cipherTextFromFile);
                }
                else
                    framework.system_message_small("Error opening file (file could be empty)");
            }
        }
    }

}
