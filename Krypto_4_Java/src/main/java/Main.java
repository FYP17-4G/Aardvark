import models.Project;
import models.ciphers.*;

/**
 * Copyright (C) 2018 Krypto.
 */


/**
 * Main class.
 *
 * @version 1.0.0
 */
public final class Main {

  /**
   * s
   * Private constructor to prevent instantiation.
   */
  private Main() {
    //do nothing
  }

  /**
   * Main code.
   *
   * @param args command-line arguments
   */
  public static void main(final String[] args) {

    //Initialization
    AbstractCipher cipher = new TranspositionPeriodic();
    String projectName = "TEST_PROJECT";
    //CHECKSTYLE:OFF
    int identifier = 1234;
    //CHECKSTYLE:ON
    String key = "sEcret";
    String ciphertext = "Jack and Jill ran up the hill to fetch a pail of water, Jack fell down and broke his crown, " +
      "and Jill came tumbling after.";
    Project project = new Project(projectName, identifier, ciphertext);

    //Show BEFORE state
    System.out.println("BEFORE");
    System.out.println("======");
    System.out.println(project.toString());


    //Perform Encryption
    project.setModifiedText(
      cipher.encrypt(project.getModifiedText(), key)
    );

    //Display results
    System.out.println("AFTER");
    System.out.println("=$$===");

    System.out.println("project.print(5, 5, true)");
    System.out.println(project.print(5, 5, true));

    System.out.println("project.print(5, 5, false)");
    System.out.println(project.print(5, 5, false));

    System.out.println("project.print(0, 0, true)");
    System.out.println(project.print(0, 0, true) + "\n");

    System.out.println("project.print(0, 0, false)");
    System.out.println(project.print(0, 0, false) + "\n");

//    //Perform decryption
//    project.setModifiedText(
//      cipher.decrypt(project.getModifiedText(), key)
//    );

//    System.out.println(project.print(3, 5));

//    System.out.println(project.getModifiedText(3));

//    //Display results
//    System.out.println("AFTER");
//    System.out.println("=====");
//    System.out.println(project.toString());
  }

}

