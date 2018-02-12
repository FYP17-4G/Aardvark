package com.example.FYP.aardvark_project.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import com.example.FYP.aardvark_project.FrontPageIdentifier;

/**MAKE FUNCTIONS STATIC*/

public class DatabaseFramework extends SQLiteOpenHelper
{
    /**
     * Things needed for column:
     *
     * (Composite key)
     * - ID
     * - Name
     *
     * (Other columns)
     * <...>
     */

    /**COLUMNS LIST REFERENCE
     *
     *
     * #NOTE:
     *  > THE CURSOR NUMBER DEPENDS ON THE NUMBER OF COLUMNS WE ARE SELECTING!!!
     *  > WHEN USING THE DATABASE AND NEED TO INSERT DATA INTO A COLUMN, USE THE VARIABLE NAME HERE
     * */
    private static final String TABLE_NAME = "Projects_List";

    private static final String PROJECT_ID = "ID"; //COLUMN 0
    private static final String PROJECT_TITLE = "Title"; //COLUMN 1
    private static final String PROJECT_ORIGINAL_CIPHER_TEXT = "OriginalCipherText"; //COLUMN 2, STORE ORIGINAL CIPHER TEXT AND ITS CHANGES HERE
    private static final String PROJECT_NOTES = "notes";

   public DatabaseFramework(Context context)
    {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(command_CreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private String command_CreateTable()
    {
        return  "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    PROJECT_ID + " INT NOT NULL, " + // COLUMN 0
                    PROJECT_TITLE + " VARCHAR(20) NOT NULL, " + // COLUMN 1
                    PROJECT_ORIGINAL_CIPHER_TEXT + " TEXT, " + // COLUMN 2
                    PROJECT_NOTES + " TEXT, " + // COLUMN 3
                    " PRIMARY KEY(" + PROJECT_ID + ", " + PROJECT_TITLE + "));";
    }

    /**INSERT DATA INTO A COLUMN, THIS WILL CREATE ID+TITLE COMPOSITE*/
    public void addNewComposite(String id, String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO " + TABLE_NAME + "(" + PROJECT_ID + ", " + PROJECT_TITLE +") VALUES ( '" + id + "', '"+ title +"');";

        db.execSQL(query);
    }

    /**ADDS DATA INTO AN ALREADY EXISTING COLUMN*/
    public void addData(String id, String title, String columnName, String data)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        columnName = checkColumnNaming(columnName);

        String query = "INSERT INTO " + TABLE_NAME + "(" + columnName + ") VALUES ('" + data + "') WHERE " +
                PROJECT_ID + "='" + id + "' AND " +
                PROJECT_TITLE + "='" + title + "'";

        db.execSQL(query);
    }

    public void updateData(String id, String title, String columnName, String newData)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        columnName = checkColumnNaming(columnName);

        String query = "UPDATE " + TABLE_NAME +
                        " SET " + columnName + "='" + newData + "' WHERE " + PROJECT_ID + "='" + id + "' AND " + PROJECT_TITLE + "='" + title + "'";
        db.execSQL(query);
    }

    public void deleteEntry(String id, String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
            PROJECT_ID + "='" + id + "' AND " + PROJECT_TITLE + "='" + title + "'";
        db.execSQL(query);
    }

    /**
     * For adapter use, call this function to adapter use
     * */
    public ArrayList<FrontPageIdentifier> getAllTitle()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //SELECT PROJECT_ID AND PROJECT_TITLE FROM TABLE_NAME

        String query = "SELECT " + PROJECT_ID + ", " + PROJECT_TITLE + " FROM " + TABLE_NAME;

        ArrayList<FrontPageIdentifier> temp = new ArrayList<>();

        try {

            Cursor data = db.rawQuery(query, null);

            //THIS GETS ALL COLUMN 0 AND COLUMN 2 FROM THE CURSOR
            while(data.moveToNext())
            {
                String id = data.getString(0);
                String title = data.getString(1);
                temp.add(new FrontPageIdentifier(id, title));
            }

        }catch(NullPointerException  e)
        {}

        return temp;
    }

    /**
     * THIS WILL RETURN THE NOTES DATA
     * */
    public String getNotesData(String id, String title)
    {
        Cursor cursor = getRawData(id, title, PROJECT_NOTES);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    /**
     * THIS WILL RETURN ORIGINAL CIPHER TEXT ALONG WITH ITS CHANGES
     *
     * OriginalCText||change1||change2||change3
     * */
    public String getCipherText(String id, String title)
    {
        Cursor cursor = getRawData(id, title, PROJECT_ORIGINAL_CIPHER_TEXT);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void updateCipherText(String id, String title, String newCText)
    {
        updateData(id, title, "PROJECT_ORIGINAL_CIPHER_TEXT", newCText);
    }

    /**
     * This selects all column from the ID and the title
     * Do note this will always return ONE entry since (ID + title) is a composite
     * */
    private Cursor getRawData(String id, String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + PROJECT_ID + "='" + id +
                        "' AND " + PROJECT_TITLE +"='"+ title + "'";
        return db.rawQuery(query, null);
    }

    //return
    private Cursor getRawData(String id, String title, String columnName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT "+ columnName +" FROM " + TABLE_NAME +
                " WHERE " + PROJECT_ID + "='" + id +
                "' AND " + PROJECT_TITLE +"='"+ title + "';";

        return db.rawQuery(query, null);
    }

    private String checkColumnNaming(String columnName)
    {
        String col = new String();

        switch(columnName)
        {
            case "PROJECT_ID":
                col = PROJECT_ID;
                break;
            case "PROJECT_TITLE":
                col = PROJECT_TITLE;
                break;
            case "PROJECT_ORIGINAL_CIPHER_TEXT":
                col = PROJECT_ORIGINAL_CIPHER_TEXT;
                break;
            case "PROJECT_NOTES":
                col = PROJECT_NOTES;
                break;
        }

        return col;
    }

    /**FORMATTING METHODS*/
    private String insertValue(String value)
    {
        return "'" + value + "'";
    }
}
