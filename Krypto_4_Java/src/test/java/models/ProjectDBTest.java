/**
 * ProjectDBTest.java
 * Tests to ensure ProjectDB is working as intended.
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package models;

//CHECKSTYLE:OFF
import  org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;
import org.junit.*;
//CHECKSTYLE:ON


/**
 * Tests on the Project object, that holds information to be operated on.
 */
public final class ProjectDBTest {

    /**
     * Shared ProjectDB attributes to be used by most tests.
     */
    private static ProjectDB pdb;

    /**
     * Used to craete unique identifiers within the test class.
     */
    private static int identifierCtr = 0;

    /**
     * Sets up the test environment.
     */
    @BeforeClass
    public static void init() {

        //Initialize pdb
        pdb = new ProjectDB();


        //Initialize the database within pdb
        ArrayList<Project> tempDB = new ArrayList<Project>();
        tempDB.add(new Project("ZERO", identifierCtr++, "ZEROZEROZERO"));
        tempDB.add(new Project("ONE", identifierCtr++, "ONEONEONE"));
        tempDB.add(new Project("TWO", identifierCtr++, "TWOTWOTWO"));

        pdb.setDatabase(tempDB);

    }


    /**
     * Checks if the size of the DB changes after insertion.
     */
    @Test
    public void insertProjectTest() {

        int insertionPoint = pdb.getDatabase().size();

        //Initialization
        Project temp0 = new Project(
            "THREE", identifierCtr++, "THREETHREETHREE"
        );
        Project temp1 = new Project("FOUR", identifierCtr++, "FOURFOURFOUR");
        Project temp2 = new Project("FIVE", identifierCtr++, "FIVEFIVEFIVE");

        //Test Initial State
        Assert.assertEquals(pdb.size(), insertionPoint);


        //Perform insertProject()
        pdb.insertProject(temp0); insertionPoint++;
        pdb.insertProject(temp1); insertionPoint++;
        pdb.insertProject(temp2); insertionPoint++;

        //Test State
        Assert.assertEquals(pdb.size(), insertionPoint);

    }

    /**
     * Checks if the data of the objects in the DB is remains correct.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void insertProjectDeepComparisonTest() {

        int insertionPoint = pdb.getDatabase().size();

        Project dataTest = new Project(
            "dataTest", identifierCtr++, "dataTest dataTest"
        );

        pdb.insertProject(dataTest);
        Assert.assertEquals(
            dataTest, pdb.getDatabase().get(insertionPoint)
        );

    }

    /**
     * Checks if insertProject() rejects duplicate entries.
     */
    @Test
    public void insertProjectDataTest() {
        String name = "PROJECT NAME";
        String originalText = "ORIGINALTEXT";

        //CHECKSTYLE:OFF
        int id = 123;
        //CHECKSTYLE:ON

        Project proj = new Project(name, id, originalText);
        Project other = new Project(name, id, originalText);

        pdb.insertProject(proj);
        int expectedSize = pdb.size();

        Assert.assertFalse(pdb.insertProject(other));
        Assert.assertEquals(expectedSize, pdb.size());
    }

    /**
     * Create Project instance.
     * Check that the database does not yet have the instance.
     * Insert Project instance.
     * Check that database now shows that the instance now exists.
     */
    @Test
    public void containsTest() {
        Project proj = new Project(
            //CHECKSTYLE:OFF
            "LOCAL_PROJ", 777, "ORIG_TEXT"
            //CHECKSTYLE:ON
        );

        Assert.assertFalse(pdb.contains(proj));

        pdb.insertProject(proj);
        Assert.assertTrue(pdb.contains(proj));
    }

    /**
     * Inserts projects into database and see if they all return.
     */
    @Test
    public void findAllTest() {

        ProjectDB localDB = new ProjectDB();
        Project p0 = new Project("ZERO", identifierCtr++, "ZEROZEROZERO");
        Project p1 = new Project("ONE", identifierCtr++, "ONEONEONE");
        Project p2 = new Project("TWO", identifierCtr++, "TWOTWOTWO");
        Project notAdding = new Project();

        localDB.insertProject(p0);
        localDB.insertProject(p1);
        localDB.insertProject(p2);

        ArrayList<Project> list = localDB.findAll();
        Assert.assertTrue(list.contains(p0));
        Assert.assertTrue(list.contains(p1));
        Assert.assertTrue(list.contains(p1));
        Assert.assertFalse(list.contains(notAdding));

    }

    /**
     * Inserts an object and see if the ProjectDB is able to find it.
     */
    @Test
    public void findByIDTest() {
        String name = "PROJECT NAME";
        String originalText = "ORIGINALTEXT";

        //CHECKSTYLE:OFF
        int id = 123;
        //CHECKSTYLE:ON

        Project proj = new Project(name, id, originalText);
        pdb.insertProject(proj);
        ArrayList<Project> list = pdb.findById(id);

        Assert.assertEquals(list.get(0), proj);
    }


}

