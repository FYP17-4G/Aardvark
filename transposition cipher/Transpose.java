public class Transpose
{
  private char paddingCharacter = '*'; // the character that will be used for padding, stray away from using standard alphanumberic characters

  //constructor
  public Transpose()
    {
    }

private String padding(String s, String key)
  {
    String a = s;

    while((a.length() % key.length()) != 0)
      {a += paddingCharacter;} //add padding

    return a;
  }

public String encypher(String pText, String key)
  {
    //Call padding function to match with the length of the key
    pText = padding(pText, key);

    String cipherText = new String();

    int keyLen = key.length();
    int plainLen = pText.length();

    for(int i = 0; i < plainLen; i += keyLen) // i = ptext pointer, j = key pointer
      {
        String subStr = pText.substring(i, i + keyLen);

        for(int j = 0; j < keyLen; j++)
          {
            int idx = Character.getNumericValue(key.charAt(j));
            cipherText += subStr.charAt(idx - 1);
          }
      }

    return cipherText;
  }

//Do the decypher
public String decipher(String cText, String key)
  {
    String dText = new  String();

    int cipherLen = cText.length();
    int keyLen = key.length();

    for(int i = 0; i < cipherLen; i+= keyLen)
      {
        String subStr = cText.substring(i, i + keyLen);
        char[] chars = subStr.toCharArray();

        for(int j = 0; j < keyLen; j++)
          {
            int idx = Character.getNumericValue(key.charAt(j));//get numeric value in the key of specified index
            chars[idx - 1] = cText.charAt(i + j);
          }
        subStr = new String(chars);
        dText += subStr;
      }
  //REMOVE THE PADDING BEFORE RETURNING
  dText = dText.replaceAll("\\*", "");

  return dText;
  }
}

// 1   2   3   4   5
// 3   4   2   5   1
