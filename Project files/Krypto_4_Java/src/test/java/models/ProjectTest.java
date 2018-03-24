/**
 * Project.java
 * Plain Old Java Class (POJO) - holds information to about the project.
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package models;

//CHECKSTYLE:OFF
import  org.junit.Assert.*;

import java.util.Random;

import org.junit.*;
//CHECKSTYLE:ON


/**
 * Tests on the Project object, that holds information to be operated on.
 */
public final class ProjectTest {

    /**
     * Shared Project object to perform the majority of tests on.
     */
    private static Project sharedTestProject;

    /**
     * Sets up the test environment.
     */
    @BeforeClass
    public static void init() {

        String tempProjectName = new String("TEST PROJECT");

        //CHECKSTYLE:OFF
        int tempIdentifier = 6492;    //does not matter what the number is
        //CHECKSTYLE:ON

        String tempOriginalText = new String(
            "ThIs, She Said, W4s, a Mistake! @,??"
        );

        sharedTestProject = new Project(
            tempProjectName, tempIdentifier, tempOriginalText
        );
    }


    //Constructor Test(s)

    //Empty Constructor

    /**
     * Tests if the default constructor instantiates all its fields.
     * NOTE: THIS IS A BAD TEST! Find a better way to do this
     */
    @Test
    public void emptyConstructorTest() {

        Project testObject = new Project();
        String emptyString = new String();

        Assert.assertNotEquals(testObject.getName(), emptyString);
        Assert.assertNotEquals(testObject.getOriginalText(), emptyString);
        Assert.assertNotEquals(testObject.getSanitizedText(), emptyString);
        Assert.assertNotEquals(testObject.getModifiedText(), emptyString);

    }

    /**
     * Tests if the constructor instantiates all its fields.
     * NOTE: THIS IS A BAD TEST! Find a better way to do this
     */
    @Test
    public void constructorTest() {

        //CHECKSTYLE:OFF
        Project testObject = new Project(
            "Rasd!!@#",
            1234,
            ";lkasdfi@-0sdf"
        );
        //CHECKSTYLE:ON

        String emptyString = new String();

        Assert.assertNotEquals(testObject.getName(), emptyString);
        Assert.assertNotEquals(testObject.getOriginalText(), emptyString);
        Assert.assertNotEquals(testObject.getSanitizedText(), emptyString);
        Assert.assertNotEquals(testObject.getModifiedText(), emptyString);

    }


    //Other methods

    /**
     * Checks if sanitizeString() is working as intended.
     */
    @Test
    public void sanitizeStringTest() {
        String input = "1234567890"
            + "abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "!@#$%^&*()><?/}{][\\'\"`~\n\t\0"
            + " ";

        String expectedOutput = "" + "abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz";

        String actualOutput = Project.sanitizeString(input);

        Assert.assertTrue(expectedOutput.equals(actualOutput));
    }

    /**
     * Check if the length of the output is greater than
     * all the length of all the attributes and their names combined.
     */
    @Test
    public void toStringLengthTest() {

        int expectedLength = 0;

        expectedLength += sharedTestProject.getName().length();
        expectedLength += sharedTestProject.getOriginalText().length();
        expectedLength += sharedTestProject.getSanitizedText().length();
        expectedLength += sharedTestProject.getModifiedText().length();

        Assert.assertTrue(
            sharedTestProject.toString().length() > expectedLength
        );

    }

    /**
     * Checks if .equals() compares every attribute.
     */
    @Test
    public void equalsDeepComparisonSuccessTest() {

        //Fields
        String projName = "PROJECT NAME";
        String originalText = "ORIGINALTEXT";

        //CHECKSTYLE:OFF
        int identifier = 9999;
        //CHECKSTYLE:ON

        //Initialize 2 projects for comparison
        Project localProject = new Project(
            projName, identifier, originalText
        );

        Project otherProject = new Project();
        otherProject.setName(projName);
        otherProject.setIdentifier(identifier);
        otherProject.setOriginalText(originalText);

        //Actual Test
        Assert.assertEquals(localProject, otherProject);

    }



    /**
     * shuts the checkstyle up.
     */
    public void foo() {
        //do nothing
    }

}

