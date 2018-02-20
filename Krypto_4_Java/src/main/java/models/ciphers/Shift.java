/*
 * ---------
 * Module Name: Shift.java
 * Performs the "Shift Cipher"
 * ---------
 * @params: key -> shift value
 * Returns the ciphertext(String), when the plaintext is shifted by key, OR
 * Returns the plaintext(String), when the ciphertext is shifted by key.
 *
 * If no key is provided, the program will assume a key of '3', which is, essentially, a Caesar Cipher.
 * ---------
*/

package models.ciphers;

public class Shift extends AbstractCipher implements CipherInterface {
    private final String NAME = "Shift Cipher";
  private final String DESC = "Shifts the input by 'key'. Allows encryption & decryption.";

//    //so it can't be instantiated
//    private Shift(){}

    //caesar cipher by default
    public String encrypt (String plaintext) {
        StringBuilder sb = new StringBuilder();

        Integer offset = 3;
        int output;
        for (Character c: plaintext.toCharArray()) {
            output = c - 'a' + offset;
            output %= 26;

            sb.append(ALPHABETS.charAt(output));
        }

        return sb.toString();
    }

    public String decrypt (String ciphertext) {
        StringBuilder sb = new StringBuilder();

        Integer offset = 3;
        int output;

        for (Character c: ciphertext.toCharArray()) {
            output = c - 'a' - offset;

            if (output < 0) {
                output += 26;
            }
            output %= 26;

            sb.append(ALPHABETS.charAt(output));
        }

        return sb.toString();
    }

    @Override
    public String encrypt (String plaintext, String key)  {
      key = key.trim();

      if (checkKey(key)) {
        StringBuilder sb = new StringBuilder();

        Integer offset = Integer.parseInt(key);
        int output;
        for (Character c : plaintext.toCharArray()) {
          output = c - 'a' + offset;
          output %= 26;

          sb.append(ALPHABETS.charAt(output));
        }
        return sb.toString();
      }

      return "failed.";
    }

    @Override
    public String decrypt (String ciphertext, String key)  {
      key = key.trim();
      if (checkKey(key)) {
        StringBuilder sb = new StringBuilder();

        Integer offset = Integer.parseInt(key);
        int output;

        for (Character c : ciphertext.toCharArray()) {
          output = c - 'a' - offset;

          if (output < 0) {
            output += 26;
          }
          output %= 26;

          sb.append(ALPHABETS.charAt(output));
        }

        return sb.toString();
      }

      return "failed.";

//      key = String.valueOf(Integer.parseInt(key) * (-1));
//      return encrypt(ciphertext, key);
    }

    @Override
    public String getDescription() { return cipherDescription; }

    @Override
    public String getName() {
        return cipherName;
    }

    /**
     * Checks if the string can be converted to an integer.
     */
    @Override
    public Boolean checkKey(String key)  {

        try {
            Integer.parseInt(key);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
}
