
/**
 * ProjectDBInterface.java
 * Defines the interface which other classes interact with the ProjectDB.
 * @version 1.0.0
 */

package models;

import java.util.ArrayList;

/**
 * hello.
 */
interface ProjectDBInterface {

    /**
     *  returns the number of entries in the database.
     * @return number of entries in the database
     */
    int size();

     /**
     * Searches the database to see if 'project' in argument exists.
     * @param project project to be looked up
     * @return true if the project is in the database.
     */
    Boolean contains(Project project);

    /**
     * Returns all the objects in the "database".
     * @return all objects in the "database"
     */
    ArrayList<Project> findAll();

    /**
     * Returns all objects that match the Id given.
     * @param identifier identifier; a unique attribute of the Project class.
     * @return all objects that match the Id given
     */
    ArrayList<Project> findById(int identifier);

    /**
     * Adds a project to the "database".
     * @param project project instance to be added to the database
     * @return true if object was successfully added
     */
    Boolean insertProject(Project project);

    /**
     * Delete the Project instance whose 'identifier' attribute matches project.
     * @param project project to delete
     * @return true if object was successfully removed
     */
    Boolean deleteProject(Project project);

    /**
     * Update the Project instance whose 'identifier' attribute matches project.
     * @param project project to update
     * @return true if object was successfully updated
     */
    Boolean updateProject(Project project);
}

