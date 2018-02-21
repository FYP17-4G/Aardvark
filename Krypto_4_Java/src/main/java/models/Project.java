/**
 * Project.java
 * Plain Old Java Class (POJO) - holds information to about the project.
 * Copyright (C) 2018 fyp17.4g.
 *
 * @author fyp17.4g
 */

package models;

import java.util.Random;

/**
 * This class defines an abstract representation of a project users can work on.
 * e.g. if a user working on decrypting some ciphertext,
 * he/she might store the ciphertext as the originaltext so as to
 * analyzing its properties and perform various operations on it.
 */
public class Project {


  //Attribute(s)

  /**
   * User given name for this project.
   */
  private String name;


  /**
   * Unique identifier for project objects.
   */
  private int identifier;

  /**
   * This attribute aims to store the text at which users are analyzing.
   * It represents the text the user is working at
   * and SHOULD NOT BE MODIFIED!
   * NOTE: 'originalText' characters are often abbreviated to 'ot' in the code
   */
  private String originalText;

  /**
   * This attribute stores a sanitized version of the user-input
   * originalText that can easily be worked upon by encrypt() and decrypt().
   *
   * NOTE:'sanitizedText'characters are often abbreviated to 'st' in the code
   */
  private String sanitizedText;

  /**
   * Attribute aims to store the result of changes made to 'sanitizedText'.
   * Examples include the result of a character substution and other
   * operations such as encryption and decryption
   * NOTE: 'modifiedText' characters are often abbreviated to 'mt' in the code
   */
  private String modifiedText;


  //Constructor(s)

  /**
   * Constructor for debugging purposes - DO NOT USE.
   */
  public Project() {
    setName("Project Name");

    Random random = new Random();
    setIdentifier(random.nextInt(Integer.MAX_VALUE));

    setOriginalText("OriginalText");
    setSanitizedText(sanitizeString(originalText));
    setModifiedText("ModifiedText");
  }

  /**
   * Simple constructor that is expected to be used at all times.
   * @param newName user given name for the project
   * @param newIdentifier unique id to differentiate projects from each other
   * @param newOriginalText string that the 'originalText' attribute
   * will be set to.
   */
  public Project(final String newName, final int newIdentifier,
                 final String newOriginalText) {
    setName(newName);
    setIdentifier(newIdentifier);
    setOriginalText(newOriginalText);
    setSanitizedText(sanitizeString(originalText));
    setModifiedText(sanitizedText);
  }


  //Getter(s)

  /**
   * Returns the user-given name for the Project object.
   * @return name - a String given by the user to help him identify his
   * projects.
   */
  public String getName() {
    return name;
  }

  /**
   * Accessor method for the 'originalText' attribute.
   * @return originalText
   */
  public String getOriginalText() {
    return originalText;
  }

  /**
   * Accessor method for the 'modifiedText' attribute.
   * @return modifiedText
   */
  public String getModifiedText() {
    return modifiedText;
  }

  /**
   * Accessor method for the 'identifier' attribute.
   * @return the identifier
   */
  public int getIdentifier() {
    return identifier;
  }

  /**
   * Accessor method for the 'sanitizedText' attribute.
   * @return the 'originalText' without invalid characters
   */
  public String getSanitizedText() {
    return sanitizedText;
  }


  //Setter(s)

  /**
   * Updates the 'name' attribute with 'newName'.
   * @param newName new value that 'name' will be set to
   */
  public void setName(final String newName) {
    name = newName;
  }

  /**
   * Mutator method for the 'originalText' attribute.
   * IT IS HIGHLY RECOMMENDED THAT THIS METHOD IS NOT USED.
   * Giving the project a new original text is logically equivalent to
   * creating a new project with a new original text
   * (the modifiedText attribute will be updated to the new
   * originalText as well)
   * @param newOriginalText new value that 'originalText' will be set to
   */
  public void setOriginalText(final String newOriginalText) {
    originalText = newOriginalText;
    setSanitizedText(sanitizeString(originalText));
    setModifiedText(sanitizedText); //performed a 2nd time for sanity
  }

  /**
   * Mutator method for the 'modifiedText' attribute.
   * @param newModifiedText new value that 'modifiedText' will be set to
   */
  public void setModifiedText(final String newModifiedText) {
    modifiedText = sanitizeString(newModifiedText);
  }

  /**
   * Mutator method for the 'identifier' attribute.
   * @param newIdentifier new value that 'identifier' will be set to
   */
  public void setIdentifier(final int newIdentifier) {
    identifier = newIdentifier;
  }

  /**
   * Mutator method for the 'sanitizedText' attribute.
   * Should only be used once - during instantiation.
   * @param newSanitizedText new value that 'sanitizedText' will be set to
   */
  public void setSanitizedText(final String newSanitizedText) {
    sanitizedText = sanitizeString(newSanitizedText);
    setModifiedText(sanitizedText);
  }


  //Other methods

  /**
   * Removes characters that should not be considered for computation.
   * In this case, it means removing all non-alphabetic characters.
   * Expected to be used to sanitize originalText and produce sanitizedText.
   * @param input any string to be be used for computation later
   * @return a version of 'input' argument prepared for computations
   */
  public static String sanitizeString(final String input) {
    String sanitizedString = new String("");

    for (int i = 0; i < input.length(); i++) {
      char ch = input.charAt(i);

      if (Character.isUpperCase(ch)) {
        ch = Character.toLowerCase(ch);
      }

      if (Character.isLowerCase(ch)) {
        sanitizedString += ch;
      }

      //ignore all other characters
    }

    return sanitizedString;
  }

  /**
   * String that shows the values of all variables of the object.
   * @return state of the object.
   */
  @Override
  public String toString() {
    String returnable = new String("");

    returnable += "name: " + name + "\n";
    returnable += "idenfifier: \n" + identifier + "\n\n";
    returnable += "originalText: \n" + originalText + "\n\n";
    returnable += "sanitizedText: \n" + sanitizedText + "\n\n";
    returnable += "modifiedText: \n" + modifiedText + "\n\n";

    return returnable;
  }

  /**
   * Provides deep comparison.
   * @param obj another abject to compare to
   * @return true if all elements are equals
   */
  @Override
  public boolean equals(final Object obj) {

    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    } else {

      Project other = (Project) obj;

      return (this.name.equals(other.name)
        && this.identifier == other.identifier
        && this.originalText.equals(other.originalText)
        && this.sanitizedText.equals(other.sanitizedText)
        && this.modifiedText.equals(other.modifiedText)
      );

    }
  }

  /**
   * Adds the lengths of all the strings and the identifier.
   * @return always same number if object is in the same state.
   */
  @Override
  public int hashCode() {

    return (this.name.length()
      + identifier
      + this.originalText.length()
      + this.sanitizedText.length()
      + this.modifiedText.length()
    );

  }

  private String restorePunctuation() {
    StringBuilder output = new StringBuilder();

    int currentIndex = 0, originalIndex = 0;
    Character currentChar, currentOriginal;

    while (originalIndex < originalText.length()) {
      currentChar = modifiedText.charAt(currentIndex);
      currentOriginal = originalText.charAt(originalIndex);

      if (Character.isLowerCase(currentOriginal)) {
        output.append(currentChar);
        ++currentIndex;
        ++originalIndex;
      } else if (Character.isUpperCase(currentOriginal)) {
        output.append(Character.toUpperCase(currentChar));
        ++currentIndex;
        ++originalIndex;
      } else {
        output.append(currentOriginal);
        ++originalIndex;
      }
    }

    return output.toString();
  }

  // Print function, takes in blockSize, blocksPerLine and showOriginalText
  // output for print (6, 5, true): mBlock -> Modified, oBlock -> Original
  // mBlock mBlock mBlock mBlock mBlock
  // oBlock oBlock oBlock oBlock oBlock

  // mBlock mBlock mBlock mBlock mBlock
  // oBlock oBlock oBlock oBlock oBlock

  // mBlock mBlock mBlock mBlock mBlock
  // oBlock oBlock oBlock oBlock oBlock
  public String print(int blockSize, int blocksPerLine, Boolean showOriginalText) {
    String data;
    StringBuilder output = new StringBuilder();
    if (blockSize <= 0) {
      data = restorePunctuation();
      output.append(data);

      if (showOriginalText) {
        output.append("\n");
        output.append(originalText);
      }
    } else {
      data = modifiedText;

      int currentCharacter = 0;
      int counter = 0;
      int lineCounter = 0;
      StringBuilder originalLine = new StringBuilder();
      StringBuilder modifiedLine = new StringBuilder();

      for (Character c : sanitizedText.toCharArray()) {
        originalLine.append(c);
        modifiedLine.append(data.charAt(currentCharacter++));

        if (++counter == blockSize) {
          if (showOriginalText)
            originalLine.append(" ");

          modifiedLine.append(" ");
          counter = 0;
          ++lineCounter;
        }

        if (lineCounter == blocksPerLine) {
          output.append(modifiedLine);
          output.append("\n");

          if (showOriginalText) {
            output.append(originalLine);
            output.append("\n");
          }

          output.append("\n");

          modifiedLine = new StringBuilder();
          originalLine = new StringBuilder();

          //MODIFIED LINE MODIFIED LINE "\n"
          //ORIGINAL LINE ORIGINAL LINE "\n"
          //"\n"

          lineCounter = 0;
        }
      }

      if (!originalLine.toString().equals("")) {
        output.append(modifiedLine);
        output.append("\n");

        if (showOriginalText) {
          output.append(originalLine);
          output.append("\n");
        }

        output.append("\n");
      }
    }

    return output.toString();
  }
}

