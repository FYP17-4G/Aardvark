import models.Project;
import models.ciphers.AbstractCipher;
import models.ciphers.VigenereCipher;

/**
 * Copyright (C) 2018 Krypto.
 */


/**
 * Main class.
 *
 * @version 1.0.0
 */
public final class Main {

    /**s
     * Private constructor to prevent instantiation.
     */
    private Main() {
        //do nothing
    }

    /**
     * Main code.
     * @param args command-line arguments
     */
    public static void main(final String[] args) {

        //Initialization
        AbstractCipher cipher = new VigenereCipher();
        String projectName = "TEST_PROJECT";
        //CHECKSTYLE:OFF
        int identifier = 1234;
        //CHECKSTYLE:ON
        String key = "SecrEt";
        String ciphertext = "CIPHERTEXT BELONG IN HERE, PUNKS!";
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
        System.out.println("=====");
        System.out.println(project.toString());
        
    }

}

