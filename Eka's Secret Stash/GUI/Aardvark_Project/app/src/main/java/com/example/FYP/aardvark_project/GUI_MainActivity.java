package com.example.FYP.aardvark_project;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.example.FYP.aardvark_project.Database.DatabaseFramework;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

public class GUI_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int READ_REQUEST_CODE = 42;

    private App_Framework framework;
    private DatabaseFramework database = new DatabaseFramework(this);

    private GUI_adaptr adapter;
    private RecyclerView list; //the 'array' of project list

    //this is the text file where the project list contents are stored. The contents will be used by recycler view in the main menu
    private String projectDirectoryFileName = "projectDirectory.txt";

    //front page identifier = struct like class for the adapter to simplify the data reading
    private ArrayList<FrontPageIdentifier> projectTitle = new ArrayList();

    private Context context = this;

    private View newProjectView;
    private String cipherTextFromFile = new String(); //for use when the user enters a file as cipher text input


    private EditText searchBar;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        framework = new App_Framework(this, false);

        overrideTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newProjectView = getLayoutInflater().inflate(R.layout.pop_new_project, null);

        clearSharedPrefs();

        setFloatingActionButton();

        setRecycler();
        setNavDrawer();
        setSearchBar();

        setTitle("PROJECT MANAGER"); //change the title in the action bar
        getListFromDB();
    }

    /**Functions for onCreate()*/

    /**Use this because this class needs "no action bar" theme for it to work */
    private void overrideTheme()
    {
        if(framework.isDarkTheme())
            this.setTheme(R.style.DarkTheme_NoActionBar);
        else
            this.setTheme(R.style.AppTheme_NoActionBar);
    }

    private void clearSharedPrefs()
    {
        //clear shared preferenes
        SharedPreferences prefs = getSharedPreferences("PREF_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    private void setRecycler()
    {
        list = (RecyclerView) findViewById(R.id.rv_numbers);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        list.setLayoutManager(layout);

        list.setHasFixedSize(true);

        /**
         * This sets the view of the floating action button and the search bar.
         * if the user is not at the top of the recycler view, both of them will not be visible
         * */
        list.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldX, int oldY) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(layout.findFirstVisibleItemPosition() != 0 && projectTitle.size() > 3)
                        {
                            searchBar.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);

                        }
                        else
                        {
                            searchBar.setVisibility(View.VISIBLE);
                            fab.setVisibility(View.VISIBLE);
                        }
                    }
                }, 0);
            }
        });
    }

    private void setFloatingActionButton()
    {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createNewProject();
            }
        });

        if(framework.isDarkTheme())
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.dark_secondaryColor));
        else
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
    }

    private void setNavDrawer()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setSearchBar()
    {
        //set up the search bar
        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint("Search");
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //make changes here
                if(!editable.toString().isEmpty())
                    filter(editable.toString());
            }
        });
    }




    private void filter(String input)
    {
        //if the search bar is empty, pass in the complete list
        if(input.isEmpty())
        {
            getListFromDB();
            adapter.filterList(projectTitle);
        }
        //else, if the search bar contains something, filter the list
        else
        {
            ArrayList<FrontPageIdentifier> filteredTitle = new ArrayList();

            for(FrontPageIdentifier item: projectTitle)
            {
                if(item.getTitle().toLowerCase().contains(input.toLowerCase()))
                {
                    filteredTitle.add(item);
                }
            }

            adapter.filterList(filteredTitle);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            System.exit(0); // EXIT THE APPLICATION NORMALLY
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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

    /**CALL THIS FUNCTION DURING STARTUP AND WHEN THERE IS ANY UPDATE TO THE DATABASE TABLE*/
    protected void getListFromDB()
    {
        projectTitle = database.getAllTitle();
        if(!projectTitle.isEmpty())
        {
            findViewById(R.id.text_view_empty).setVisibility(View.GONE);

            adapter = new GUI_adaptr(projectTitle);

            if(adapter.getItemCount() > 0)
                list.setAdapter(adapter);
        }
        else if(projectTitle.isEmpty())
            findViewById(R.id.text_view_empty).setVisibility(View.VISIBLE);

        /**SET THE VISIBILITY OF THE SEARCH BAR, IF THERE IS NOTHING IN THE ADAPTER, DONT DISPLAY THE SEARCH BAR
         * */

        if(projectTitle.isEmpty())
            searchBar.setVisibility(View.GONE);
        else
            searchBar.setVisibility(View.VISIBLE);
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
        //TODO()BUG HERE PLS FIX
        /**Just create empty activity with a paragraph of text and some contact info*/
        Intent intent = new Intent(this, GUI_aboutUs.class);
        this.startActivity(intent);
    }
    private void launchSettingsActivity()
    {
        /**For settings, use "Settings template"*/
        Intent intent = new Intent(this, Settings.class);
        this.startActivity(intent);
    }

    private void createNewProject()
    {
            int ID = new Random().nextInt(999)+1;

           //SETUP NEWP ROJECT POPUP ELEMENT HERE
           EditText newProjectTitle = newProjectView.findViewById(R.id.editText_newProjectNameField);
           EditText cipherTextInput = newProjectView.findViewById(R.id.editText_cipherInputField);

           newProjectTitle.setText("");
           cipherTextInput.setText("");

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
                            database.addNewComposite(Integer.toString(ID), projectTitle);
                            //database.addData(Integer.toString(ID), projectTitle, "PROJECT_ORIGINAL_CIPHER_TEXT", cipherText);
                            database.updateData(Integer.toString(ID), projectTitle, "PROJECT_ORIGINAL_CIPHER_TEXT", cipherText);

                            getListFromDB();
                            adapter.notifyDataSetChanged();//refresh the adapter
                        }
                }
            });
        }

    /**THIS FUNCTION WILL BE USED IN "GUI_adaptr.java"*/
    protected void editProject(String ID, String title, String cText)
    {
        String viewTitle = "Edit Project";
        String positiveButtonText = "Save Changes";

        //SETUP NEWP ROJECT POPUP ELEMENT HERE
        EditText newProjectTitle = newProjectView.findViewById(R.id.editText_newProjectNameField);
        EditText cipherTextInput = newProjectView.findViewById(R.id.editText_cipherInputField);

        newProjectTitle.setText(title);
        cipherTextInput.setText(cText);

        Button getFromFile = newProjectView.findViewById(R.id.button_getCipherTextFromFile);
        getFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileBrowser(); //this function will call default device file browser
            }
        });

        framework.popup_custom(viewTitle, newProjectView, positiveButtonText, "cancel", new DialogInterface.OnClickListener() {
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
                    database.updateData(ID, title, "PROJECT_ORIGINAL_CIPHER_TEXT", cipherText);
                    database.updateData(ID, title, "PROJECT_TITLE", projectTitle);

                    getListFromDB();
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
