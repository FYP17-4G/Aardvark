/*
 * ---------
 * Module Name: RectangularKeyTransposition.java
 * Generates a key based on the key provided. It then uses this new key as a substitution key.
 * e.g. key -> wihmlz
 * The program takes in this key <wihmlz>, and returns <wagqxibjryhcksmdntleouzfpv>.
 * This is obtained by taking <w i h m l z>, adding the missing letters of the alphabet <abcdefgjknopqrstuvxy> to it,
 * and forming a rectangle (of width key.length) out of it, like so:
 *                  w i h m l z
 *                  a b c d e f
 *                  g j k n o p
 *                  q r s t u v
 *                  x y
 * The key is generated by reading vertically from left to right: wagqxibjryhcksmdntleouzfpv
 *
 * This new key is then used to perform substitution on the plaintext, where:
 *      PT: a b c d e f g h i j k l m n o p q r s t u v w x y z
 *      CT: w a g q x i b j r y h c k s m d n t l e o u z f p v
 * ---------
 * @params: plaintext/ciphertext -> the text to be encrypted/decrypted.
 *          key -> the key to use to perform the encryption/decryption.
 * ---------
 */

package models.ciphers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RectangularKeyTransposition
  extends AbstractBlockCipher
  implements CipherInterface {

  private static final String NAME = "Rectangular Key Transposition Cipher";
  private static final String DESC = "The Rectangular Key Transposition Cipher is a combination of the regular " +
    "Transposition Cipher and the Substitution Cipher." + "\n" +
    "EXAMPLE" + "\n" +
    "-------" + "\n" +
    "KEY -> RECTANGLE" + "\n" +
    "R E C T A N G L" + "\n" +
    "B D F H I J K M" + "\n" +
    "O P Q S U V W X" + "\n" +
    "Y Z" + "\n" +
    "ALP: A B C D E F G H I J K L M N O P Q R S T U V W X Y Z" + "\n" +
    "KEY: R B O Y E D P Z C F Q T H S A I U N J V G K W L M X" + "\n" +
    "PT:  THIS IS THE PLAINTEXT" + "\n" +
    "CT:  VZCJ CJ VZE ITRCSVELV";

  @Override
  public String encrypt(String plaintext, String key) {
    key = key.toLowerCase();
    key = removeDuplicates(key);
    checkKey(key);

    key = generateOTP(key);
    System.out.println("key = " + key);
    AbstractCipher sub = new Substitution();

    return sub.encrypt(plaintext, key);
  }

  @Override
  public String decrypt(String ciphertext, String key) {
    key = key.toLowerCase();
    key = removeDuplicates(key);
    checkKey(key);

    key = generateOTP(key);
    AbstractCipher sub = new Substitution();

    return sub.decrypt(ciphertext, key);
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  private String generateOTP(String key) {
    List<List<Character>> rectangle = new ArrayList<>();
    StringBuilder out = new StringBuilder(key);
    for (Character c : ALPHABETS.toCharArray()) {
      if (!out.toString().contains(c.toString())) {
        out.append(c);
      }
    }

    System.out.println("out.toString() = " + out.toString());

    List<Character> row = new ArrayList<>();
    for (Character c : out.toString().toCharArray()) {
      row.add(c);

      if (row.size() >= key.length()) {
        rectangle.add(row);
        row = new ArrayList<>();
      }
    }

    if (row.size() != 0)
      rectangle.add(row);

    out = new StringBuilder();
    rectangle = flip(rectangle);
    for (List<Character> r : rectangle) {
      for (Character c : r) {
        out.append(c);
      }
    }

    return out.toString();
  }

  @Override
  public Boolean checkKey(String key) {
    //not sure if i should do this or allow duplicates and remove them myself.
    if (removeDuplicates(key).length() != key.length()) {
      return false;
    }

    if (key.length() < 2) {
      return false;
    }

    return super.checkKey(key);
  }

  private List<List<Character>> flip(List<List<Character>> input) {
    List<List<Character>> output = new ArrayList<>();
    List<Character> newRow;

    int maxCols = input.get(0).size();

    for (int i = 0; i < maxCols; ++i) {
      newRow = new ArrayList<>();

      for (List<Character> row : input) {
        if (i < row.size())
          newRow.add(row.get(i));
      }

      output.add(newRow);
    }

    return output;
  }
}
