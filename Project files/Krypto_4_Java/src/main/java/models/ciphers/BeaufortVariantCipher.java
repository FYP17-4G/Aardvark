/*
 * ---------
 * Module Name: BeaufortVariant.java
 * An cipher where the ciphertext is obtained by subtracting the key from the plaintext.
 * to the value of the repeating key (mod 26). CT_i = PT_i - K_i (mod 26)
 * i [<p>] in Krypto.exe
 * ---------
 * @params: data            -> input
 *          sequenceLength  -> length of sequence to check for
 *          maxEntries      -> max number of entries to display
 * Returns the ciphertext (if encrypting) or the plaintext (if decrypting)
 * ---------
 */

package models.ciphers;

public class BeaufortVariantCipher
  extends AbstractCipher
  implements CipherInterface {

  private static final String NAME = "Variant Beaufort Cipher";
  private static final String DESC = "The Variant Beaufort Cipher is a substitution cipher, similar to the Vigenere " +
    "Cipher, and is a variant on the usual Beaufort." + "\n" +
    "EXAMPLE" + "\n" +
    "-------" + "\n" +
    "PT:  THIS IS THE PLAINTEXT" + "\n" +
    "KEY: BEAU FO RTB EAUFORTBE" + "\n" +
    "CT:  SDIY DE COD LLGDZCLWP";

  @Override
  public String encrypt(String plaintext, String key) {
    key = key.toLowerCase();
    if (checkKey(key)) {
      StringBuilder out = new StringBuilder();
      int position = 0;

      for (Character c : plaintext.toCharArray()) {

        if (position >= (key.length())) {
          position = 0;
        }

        if (Character.isAlphabetic(c)) {
          out.append(encryptOne(c, key.charAt(position++)));
        } else {
          out.append(c);
        }
      }

      return out.toString();
    }

    return "Failed.";
  }

  @Override
  public String decrypt(String ciphertext, String key) {
    key = key.toLowerCase();
    if (checkKey(key)) {
      StringBuilder out = new StringBuilder();
      int position = 0;
      for (Character c : ciphertext.toCharArray()) {
        if (position >= (key.length())) {
          position = 0;
        }

        if (Character.isAlphabetic(c)) {
          out.append(decryptOne(c, key.charAt(position++)));
        } else {
          out.append(c);
        }
      }

      return out.toString();
    }

    return "Failed.";
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  //encrypt ONE character
  private static Character encryptOne(Character plaintext, Character key) {
    int res, k;
    k = key - 'a';

    res = plaintext - 'a';
    res -= k;

    if (res < 0)
      res += 26;

    res %= ALPHABETS.length();

    return ALPHABETS.charAt(res);
  }

  //decrypt ONE character
  private static Character decryptOne(Character plaintext, Character key) {
    int res, k;
    k = key - 'a';

    res = plaintext - 'a';
    res += k;

    res %= ALPHABETS.length();

    return ALPHABETS.charAt(res);
  }

  @Override
  public Boolean checkKey(String key) {
    int originalLength = key.length();
    key = key.replaceAll("[^A-Za-z]", "");

    if (key.length() != originalLength) return false;

    return super.checkKey(key);
  }
}
