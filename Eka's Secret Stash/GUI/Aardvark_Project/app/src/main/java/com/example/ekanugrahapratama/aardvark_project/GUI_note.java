package com.example.ekanugrahapratama.aardvark_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

public class GUI_note extends AppCompatActivity
{

    private App_Framework framework;

    private EditText noteField;

    //use these 2 for saving files
    private String title;
    private String id;

    private String noteVal;

    private String notefileName;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveNote();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        saveNote();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_note);

        setup();
        loadNote();
    }

    private void setup()
    {
        this.framework = new App_Framework(this);

        this.title = getIntent().getStringExtra("title");
        this.id = getIntent().getStringExtra("id");

        this.notefileName = id+title+"notes.txt";

        noteField = findViewById(R.id.editText_noteField);
    }

    private void saveNote()
    {
        this.noteVal = noteField.getText().toString();

        if(!noteVal.isEmpty())
            framework.saveAsTxt(notefileName, noteVal, false);
    }

    private void loadNote()
    {
        this.noteVal = framework.getTextFromFile(notefileName);

        if(!this.noteVal.isEmpty())
            noteField.setText(this.noteVal);
    }
}
