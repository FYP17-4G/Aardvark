package com.example.FYP.aardvark_project.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.example.FYP.aardvark_project.Database.DatabaseFramework;
import com.example.FYP.aardvark_project.R;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

public class Activity_Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int READ_REQUEST_CODE = 42;

    private App_Framework framework;
    private DatabaseFramework database = new DatabaseFramework(this);

    private MainActivityAdapter adapter;
    private RecyclerView list; //the 'array' of project list

    //front page identifier = struct like class for the adapter to simplify the data reading
    private ArrayList<FrontPageIdentifier> projectTitle = new ArrayList();

    private Context context = this;

    private View newProjectView;
    private String cipherTextFromFile = new String(); //for use when the user enters a file as cipher text input


    private EditText searchBar;

    private FloatingActionButton fab;

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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            System.exit(0); // EXIT THE APPLICATION NORMALLY
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu); // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    /**This function will be executed on return from the device's file browser*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if(data != null) {
                Uri uri = data.getData(); //URI is the return value given by ACTION_OPEN_DOCUMENT intent

                try
                {
                    cipherTextFromFile = framework.readTextFromUri(uri, context);
                } catch (IOException e)
                {framework.system_message_small(e.getMessage());}

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        framework = new App_Framework(this, false);
        overrideTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newProjectView = getLayoutInflater().inflate(R.layout.pop_new_project, null);

        clearSharedPrefs();

        setFloatingActionButton();

        setRecycler(); //set the recycler view, recycler = container of the card view
        setNavDrawer();
        setSearchBar();

        setTitle("Project Manager");
        getListFromDB();

        getSupportActionBar().setElevation(0);
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
        SharedPreferences prefs = getSharedPreferences("PREF_SESSION", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    private void setRecycler()
    {
        list = findViewById(R.id.rv_numbers);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        list.setLayoutManager(layout);

        list.setHasFixedSize(true);

        /**
         * This sets the view of the floating action button and the search bar.
         * if the user is not at the top of the recycler view, both of them will not be visible
         * */
        list.setOnScrollChangeListener((View view, int x, int y, int oldX, int oldY) -> {
                final int MINIMUM_RECYCLERVIEW_ENTRY = 2;

                if(layout.findFirstVisibleItemPosition() != 0 && projectTitle.size() >= MINIMUM_RECYCLERVIEW_ENTRY)
                {
                    searchBar.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                }
                else
                {
                    searchBar.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }
        });
    }

    private void setFloatingActionButton()
    {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> createNewProject());

        if(framework.isDarkTheme())
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.dark_secondaryColor));
        else
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
    }

    private void setNavDrawer()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true); //highlights "front page" item menu
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setSearchBar()
    {
        /**set up the search bar*/
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
                getListFromDB();

                if(!editable.toString().isEmpty() || editable.toString() != null)
                    filter(editable.toString());
                else
                    adapter.filterList(projectTitle);
            }
        });
    }

    /**Filters the recycler view container to only contain words in the search bar*/
    private void filter(String input)
    {
        ArrayList<FrontPageIdentifier> filteredTitle = new ArrayList();

        for(FrontPageIdentifier item: projectTitle)
            if(item.getTitle().toLowerCase().contains(input.toLowerCase()))
                filteredTitle.add(item);

        adapter.filterList(filteredTitle);
    }

    /**CALL THIS FUNCTION DURING STARTUP AND WHEN THERE IS ANY UPDATE TO THE DATABASE TABLE*/
    protected void getListFromDB()
    {
        projectTitle = database.getAllTitle();
        if(!projectTitle.isEmpty())
        {
            findViewById(R.id.text_view_empty).setVisibility(View.GONE);

            adapter = new MainActivityAdapter(projectTitle);

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
        startActivity(new Intent(this, Activity_Main.class));
    }

    private void launchEncryptionDecryptionActivity()
    {
        startActivity(new Intent(this, Activity_Enc_Dec.class));
    }

    private void launchAboutUsActivity()
    {
        this.startActivity(new Intent(this, Activity_About_Us.class));
    }
    private void launchSettingsActivity()
    {
        this.startActivity(new Intent(this, Activity_Settings.class));
    }

    private void createNewProject()
    {
        int ID = new Random().nextInt(999)+1;

        EditText newProjectTitle = newProjectView.findViewById(R.id.editText_newProjectNameField);
        EditText cipherTextInput = newProjectView.findViewById(R.id.editText_cipherInputField);

        newProjectTitle.setText("");
        cipherTextInput.setText("");

        Button getFromFile = newProjectView.findViewById(R.id.button_getCipherTextFromFile);
        getFromFile.setOnClickListener(view -> openFileBrowser()); //this function will call default device file browser

        AlertDialog alertDialog = framework.popup_custom("Create new project", newProjectView, "create", "cancel", (dialogInterface, i) -> {

            String projectTitle = newProjectTitle.getText().toString();
            String cipherText = cipherTextInput.getText().toString();

            if(projectTitle.isEmpty())
                framework.system_message_small("Project title is still empty");
            else if(cipherText.isEmpty())
                framework.system_message_small("Cipher text input is still empty");
            else if(projectExist(projectTitle))
                framework.system_message_small("Project title already exist");
            else
            {
                database.addNewComposite(Integer.toString(ID), projectTitle);
                database.updateData(Integer.toString(ID), projectTitle, "PROJECT_ORIGINAL_CIPHER_TEXT", cipherText);

                getListFromDB();
                adapter.notifyDataSetChanged();//refresh the adapter
            }
            });
        alertDialog.show();
        }

    /**THIS FUNCTION WILL BE USED IN "MainActivityAdapter.java"*/
    protected void editProject(String ID, String title, String cText)
    {
        String viewTitle = "Edit Project";
        String positiveButtonText = "Save Changes";

        EditText newProjectTitle = newProjectView.findViewById(R.id.editText_newProjectNameField);
        EditText cipherTextInput = newProjectView.findViewById(R.id.editText_cipherInputField);

        newProjectTitle.setText(title);
        cipherTextInput.setText(cText);

        Button getFromFile = newProjectView.findViewById(R.id.button_getCipherTextFromFile);
        getFromFile.setOnClickListener(view -> openFileBrowser());

        /**get the cipher text input from input field*/
        AlertDialog alertDialog = framework.popup_custom(viewTitle, newProjectView, positiveButtonText, "cancel", (dialogInterface, i) -> {

            String projectTitle = newProjectTitle.getText().toString();
            String cipherText = cipherTextInput.getText().toString();

            if(projectTitle.isEmpty())
                framework.system_message_small("Project title is still empty");
            else if(cipherText.isEmpty())
                framework.system_message_small("Cipher text input is still empty");
            else if(projectExist(projectTitle) && !projectTitle.equals(title))
                framework.system_message_small("Project title already exist");
            else
            {
                database.updateData(ID, title, "PROJECT_ORIGINAL_CIPHER_TEXT", cipherText);
                database.updateData(ID, title, "PROJECT_TITLE", projectTitle);

                getListFromDB();
                adapter.notifyDataSetChanged();//refresh the adapter
            }
        });
        alertDialog.show();
    }

    protected boolean projectExist(String newProjectTitle)
    {
        for(FrontPageIdentifier fpi: projectTitle)
            if(fpi.getTitle().toLowerCase().equals(newProjectTitle.toLowerCase()))
                return true;

        return false;
    }

    /**
     * This opens device's default file browser
     * */
    private void openFileBrowser()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //START INTENT TO CHOOSE FILE USING DEVICE' DEFAULT FILE BROWSER
        intent.addCategory(Intent.CATEGORY_OPENABLE);//SHOW ONLY FILES THAT CAN BE OPENED
        intent.setType("text/plain"); //only plain txt file can be accessed

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

}
