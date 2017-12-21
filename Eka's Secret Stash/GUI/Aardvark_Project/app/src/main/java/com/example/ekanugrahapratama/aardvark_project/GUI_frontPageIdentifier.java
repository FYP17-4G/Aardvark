package com.example.ekanugrahapratama.aardvark_project;

/**
 * This is a struct like class that contains the content of the front page recycler item.
 * Use this class to identify which item being selected and to access relevant data
 */

public class GUI_frontPageIdentifier
{
    private String ID;
    private String title;

    public GUI_frontPageIdentifier(String ID, String title)
        {
        this.ID = ID;
        this.title = title;
        }

    public String getID()
        {
            return this.ID;
        }
    public String getTitle()
        {
            return this.title;
        }
}
