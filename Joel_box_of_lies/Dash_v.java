
import java.io.*;
import java.util.*;

class Dash_v {

    //Attributes
    String originalText;    //should not ever be modified (original text from file)
    String modifiedText;    //file to perform operations on
    String key;             //key required to perform transformations
    int blockLength;        //for printing purposes mostly

    Dash_v(String input_key) {

        System.out.println("Dash_v() called!"); //@debug
        
        //Basic Initialization
        originalText = new String("-EMPTY STRING-");
        modifiedText = new String ("-EMPTY STRING-");
        key = input_key;

        //Sets originalText to contents of inputfile.txt
        readFile();
        System.out.println("originalText: \n" + originalText);  //@debug

        //Ttransforms originalText and store in modifiedText
        modifiedText = originalText;
        transformText();

        //Blocklength
        blockLength = 1;

    }   //end Dash-g()

    //Copies contents of filename variable to originalText
    void readFile() {

        //Initialization
        String filename = new String("inputfile.txt");  //@hardcoded
        originalText = new String("");        
        BufferedReader br = null;

        //Actual Reading
        try {   
            br = new BufferedReader(new FileReader(filename));
            for(String line = new String(""); (line = br.readLine()) != null; ) {
                originalText += line;
            }   //end for
        } catch ( IOException ioe) {
            System.err.println("Error reading file " + filename);
            System.err.println(ioe);
            System.out.println("-----");
            ioe.printStackTrace();
        }   //end try-catch block

    }   //end readfile()


    void transformText() {

        String temp = new String("");  //replaces modifiedString (used as a workspace)
        for(int y = 0; y < modifiedText.length(); y++ ) {
            //Get numerical values for each character
            int PT = modifiedText.charAt(y) - 'A';
            int k = key.charAt( mod(y, key.length())) - 'A';

            //Append the addition of PT and K (mod 26) to the temporary string
            temp += (char) (mod((PT + k), 26) + 'A');
        }   //end y

        modifiedText = temp;
    }   //end transformText

    int mod(int base, int mod) {
        while ( base < 0 || base >= mod ) {
            if( base < 0 ) {
                base += mod;
            }
            if ( base >= mod ) {
                base -= mod;
            }
        }   //end while
        return base;
    }   //end mod
}