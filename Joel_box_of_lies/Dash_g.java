
import java.io.*;
import java.util.*;

class Dash_g {

    //Attributes
    String originalText;    //should not ever be modified (original text from file)
    String modifiedText;    //file to perform operations on
    String key;             //key required to perform transformations
    int blockLength;        //for printing purposes mostly

    Dash_g(String input_key) {

        System.out.println("Dash_g() called!"); //@debug
        
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

    void readFile() {
        String filename = new String("inputfile.txt");  //@hardcoded
        originalText = new String("");        
        BufferedReader br = null;
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
        ArrayList< ArrayList <String> > rectangle = new ArrayList< ArrayList <String> >();
        
        String temp = key + modifiedText;

        temp = removeDuplicates(temp);

        //Places each character in their spot in the rectangle
        for(int y = 0, c = 0; c < temp.length(); y++ ) {
            ArrayList<String> row = new ArrayList<String>();
            for(int x = 0; x < key.length(); x++, c++ ) {
                if(c < temp.length()) {
                    row.add(""+ temp.charAt(c));
                } else {
                    row.add("#");
                }
            }   //end for x
            rectangle.add(row);
        }   //end for y    

        //Print rectangle
        for( int y = 0; y < rectangle.size(); y++ ) {
            for(int x = 0; x < rectangle.get(y).size(); x++ ) {
                System.out.print(rectangle.get(y).get(x) + " ");
            }
            System.out.println();
        }

        modifiedText = new String("");
        for( int x = 0; x < key.length(); x++ ) {
            for(int y = 0; y < rectangle.size(); y++ ) {
                String ch = rectangle.get(y).get(x);
                if(!ch.equals("#")) {
                    modifiedText += ch;                    
                }   //end if
            }   //end y
        }   //end x

    }   //end transformText



    String removeDuplicates(String input) {
        String returnable = new String("");

        ArrayList<String> checkOffList = new ArrayList<String>();
        for(char ch = 'A'; ch <= 'Z'; ch++) {
            checkOffList.add(""+ch);
        }

        //removes all characters except alphabets, converts lowercase to uppercase
        for(int i = 0; i < input.length(); i++) {

            char ch = input.charAt(i);

            if ( ch >= 'a' && ch <= 'z' ) { //essentially toUpper
                ch = (char) ('A' + ch - 'a');
            } else if ( ch >= 'A' && ch <= 'Z') { 
                //do nothing
            } else {
                ch = '#';
            }   //end if-else 

            if ( ch != '#' && checkOffList.get(ch - 'A').equals(""+ch)) {   //checks if valid char AND if char was alrdy used
                returnable += ch;
                checkOffList.set(ch-'A', ""+'*');
            } //end if
        }   //end for
        return returnable;
    }   //end removeDuplicates

}