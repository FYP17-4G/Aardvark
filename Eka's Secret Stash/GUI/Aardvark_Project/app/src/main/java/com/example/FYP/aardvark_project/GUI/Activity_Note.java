package com.example.FYP.aardvark_project.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.FYP.aardvark_project.Database.DatabaseFramework;
import com.example.FYP.aardvark_project.R;

public class Activity_Note extends AppCompatActivity
{
    private DatabaseFramework database;

    private EditText noteField;

    private String title;
    private String id;

    private String noteVal;

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
        new App_Framework(this, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        database = new DatabaseFramework(this);

        setup();
        loadNote();
    }

    private void setup() {
        this.title = getIntent().getStringExtra("title");
        this.id = getIntent().getStringExtra("id");

        noteField = findViewById(R.id.editText_noteField);

        setTitle("NOTE: "+this.title);
    }

    private void saveNote() {
        this.noteVal = noteField.getText().toString();

        if(!noteVal.isEmpty())
            database.updateData(id, title, "PROJECT_NOTES", this.noteVal);
    }

    private void loadNote() {
        this.noteVal = database.getNotesData(id, title);

        if(this.noteVal != null)
            noteField.setText(this.noteVal);
    }
}
