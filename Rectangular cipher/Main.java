import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main
{
  public static void main(String[] args)
      {
        String inputFilename = "input.txt";
        String pText = new String();
        String cText = new String();
        String deciphered = new String();
        int key = 5; //HARDCODED CHANGE LATRE

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
        RectangularCipher rc = new RectangularCipher();
        cText = rc.encipher(pText, key);

        System.out.println("Plain: "+pText);
        System.out.println("Cipher: "+cText);

        //TODO(3) decipher cText
        deciphered = rc.decipher(cText, key);

        System.out.println("Deciphered: "+deciphered);
      }

}
