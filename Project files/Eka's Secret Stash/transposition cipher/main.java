import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class main
{
  public static void main(String[] args)
    {
      String inputFilename = "input.txt";
      String pText = new String();
      String key = "42531"; // n digits key must contain the largest value of n
      String cText;
      String dText = new String(); //this is the deciphered text, derived from cText variable

      String temp;

      //TODO (1) get plaintext from a text file
      try
        {
          BufferedReader fileInput = new BufferedReader(new FileReader(inputFilename));

          while((temp = fileInput.readLine()) != null && temp.length() != 0)
            {
              pText += temp;
            }
        }catch(FileNotFoundException e)
          {System.out.println("[ERROR]: " + inputFilename + "not found");}
            catch(IOException x)
              {System.out.println("[ERROR]: IO ERROR");}

      //TODO (2) do the calculation
      Transpose transpose = new Transpose();
      cText = transpose.encypher(pText, key);
      System.out.println("plain: " + pText);
      System.out.println("cipher: " +cText );

      //TODO(3) decipher cText
      dText = transpose.decipher(cText, key);
      System.out.println("deciphered: " + dText);
    }
}
