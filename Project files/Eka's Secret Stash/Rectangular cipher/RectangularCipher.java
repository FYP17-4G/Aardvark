public class RectangularCipher
{
  private char paddingCharacter = '*'; // the character that will be used for padding, stray away from using standard alphanumberic characters

  RectangularCipher()
    {}

  private String padding(String s, int key)
    {
      String a = s;

      while((a.length() % key) != 0)
        {a += paddingCharacter;} //add padding

      return a;
    }

  public String encipher(String pText, int key)
    {
      String cText = new String();

      pText = padding(pText, key);

      for(int i = 0; i < key; i++)
        {
          for(int j = i; j < pText.length(); j+=key)
            {
              cText += pText.charAt(j);
            }
        }

    return cText;
    }

  public String decipher(String cText, int key)
    {
    return encipher(cText, (cText.length()/key)).replaceAll("\\"+this.paddingCharacter, "");
    }
}
