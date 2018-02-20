package models.ciphers;

public class Kamasutra extends AbstractCipher implements CipherInterface {
  private static final String DESC = "Kamasutra Cipher";
  private static final String NAME = "The Kamasutra Cipher is a substitution cipher, and involves randomly pairing " +
    "letters of the alphabet, and then substituting each letter in the original message with its partner." + "\n" +
    "EXAMPLE" + "\n" +
    "-------" + "\n" +
    "KEY: " + "\n" +
    "A B C D E F G H I J K L M " + "\n" +
    "N O P Q R S T U V W X Y Z" + "\n" +
    "PT:  THIS IS THE PLAINTEXT" + "\n" +
    "CT:  GUVF VF GUR CYNVAGRKG";

  @Override
  public String encrypt(String plaintext, String key) {
    key = key.toLowerCase();
    checkKey(key);

    StringBuilder out = new StringBuilder();
    for (Character c : plaintext.toCharArray()) {
      int position = find(c, key);
      if (position < 13)
        position += 13;
      else {
        position -= 13;
      }

      out.append(key.charAt(position));
    }

    return out.toString();
  }

  @Override
  public String decrypt(String ciphertext, String key) {
//        key = key.toLowerCase();
//        checkKey(key);
//
//        StringBuilder out = new StringBuilder();
//        for (Character c: ciphertext.toCharArray()) {
//            int position = find (c, key);
//            if (position < 13)
//                position += 13;
//            else {
//                position -= 13;
//            }
//
//            out.append(key.charAt(position));
//        }
//
//        return out.toString();

    return encrypt(ciphertext, key);
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public Boolean checkKey(String key) {
    if (!(removeDuplicates(key).equals(key))) return false;
    if (key.length() != ALPHABETS.length()) return false;

    return super.checkKey(key);
  }

  private int find(Character in, String data) {
    int index = 0;
    for (Character c : data.toCharArray()) {
      if (c == in) break;

      ++index;
    }

    return index;
  }
}
