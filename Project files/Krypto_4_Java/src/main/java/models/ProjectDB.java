/**
 * ProjectDB.java
 * Manages a List of Project instances.
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package models;

import java.util.ArrayList;

/**
 * Manages a List of Project instances.
 */
public class ProjectDB implements ProjectDBInterface {

    //Attribute(s)

    /**
     * An list of database objects that this class will manage.
     */
    private ArrayList<Project> database;

    //Constructor(s)

    /**
     * Constructor.
     */
    public ProjectDB() {
        database = new ArrayList<Project>();
    }


    //Getter(s)

    /**
     * Returns the entire database in the form of an ArrayList.
     * @return the database
     */
    public ArrayList<Project> getDatabase() {
        return database;
    }


    //Setter(s)

    /**
     * Replaces the entire 'database' attribute.
     * @param newDatabase the database to set
     */
    public void setDatabase(final ArrayList<Project> newDatabase) {
        database = newDatabase;
    }

    //Other Method(s)
    /**
     *  returns the number of entries in the database.
     * @return number of entries in the database
     */
    public int size() {
        return database.size();
    }

    /**
     * Searches the database to see if 'project' in argument exists.
     * @param project project to be looked up
     * @return true if the project is in the database.
     */
    public Boolean contains(final Project project) {

        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).equals(project)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns all the objects in the "database".
     * @return all objects in the "database"
     */
    public ArrayList<Project> findAll() {
        return database;
    }

    /**
     * Returns all objects that match the Id given.
     * @param identifier identifier; a unique attribute of the Project class.
     * @return all objects that match the Id given (0 if none found)
     */
    public ArrayList<Project> findById(final int identifier) {

        ArrayList<Project> returnable = new ArrayList<Project>();

        for (int i = 0; i < database.size(); i++) {
            Project proj = database.get(i);
            if (proj.getIdentifier() == identifier) {
                returnable.add(proj);
            }
        }

        return returnable;
    }

    /**
     * Adds a project to the "database".
     * @param project project instance to be added to the database
     * @return true if object was successfully added
     */
    public Boolean insertProject(final Project project) {

        try {
            database.add(project);
            return true;
        } catch (Exception x) {
            System.err.println(x);
            return false;
        }

    }

    /**
     * Delete the Project instance whose 'identifier' attribute matches project.
     * @param project project to delete
     * @return true if object was successfully removed
     */
    public Boolean deleteProject(final Project project) {
        return false;
    }

    /**
     * Update the Project instance whose 'identifier' attribute matches project.
     * @param project project to update
     * @return true if object was successfully updated
     */
    public Boolean updateProject(final Project project) {
        return false;
    }
}

