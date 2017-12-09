public class Transpose
{
  private char paddingCharacter = '*'; // the character that will be used for padding, stray away from using standard alphanumberic characters

  //constructor
  public Transpose()
    {
    }

//the padding will be deleted later during decryption  !!!!!!
private String padding(String s, String key)
  {
    String a = s;

    while((a.length() % key.length()) != 0)
      {
        a += paddingCharacter; //add padding
      }

    return a;
  }

public String encypher(String pText, String key)
  {
    pText = padding(pText, key);

    String cipherText = new String();

    int keyLength = key.length();
    int plainLength = pText.length();

    for(int i = 0; i < plainLength; i += keyLength) // i = ptext pointer, j = key pointer
      {
        String subCipher = pText.substring(i, i + keyLength);

        for(int j = 0; j < keyLength; j++)
          {
            int idx = Character.getNumericValue(key.charAt(j));
            cipherText += subCipher.charAt(idx - 1);
          }
      }

    return cipherText;
  }

//TODO (22)  do the decypher
//THE OUTPUT IS STILL NOT CORRECT!!!
public String decipher(String cText, String key)
  {
    String decipheredText = cText;

    int cipherLen = cText.length();
    int keyLen = key.length();

    for(int i = 0; i < cipherLen; i+= keyLen)
      {
        String subCipher = cText.substring(i, i + keyLen);

        for(int j = 0; j < keyLen; j++)
          {
            int idx = Character.getNumericValue(key.charAt(j));
            char c = cText.charAt(j);

            char[] chars = decipheredText.toCharArray();
            chars[idx - 1] = c;
            decipheredText = new String(chars);
          }
      }

  return decipheredText;
  }
}

// 1   2   3   4   5
// 3   4   2   5   1
