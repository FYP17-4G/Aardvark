package com.example.FYP.aardvark_project.GUI;

public class FrontPageIdentifier
{
    private String ID;
    private String title;

    public FrontPageIdentifier(String ID, String title) {
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
    public void setTitle(String newTitle)
    {
        this.title = newTitle;
    }
}
