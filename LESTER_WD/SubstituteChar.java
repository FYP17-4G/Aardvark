import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SubstituteChar {

    //To test functionality
    public static void main(String[] args) {

    }

    //Private variables
    private String originalData;
    private String modifiedData;

    //Default constructor
    public SubstituteChar(){
        //When the object is created, the readFile function will store the original data into private variable
    }

    //Function to read in a file
    public static String readFile(String fileName){

        //Use FileReader & BufferedReader to read data in file, line by line
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.err.println("Failed to open file " + fileName);
        }

        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            System.err.println("Failed to read data in " + fileName);
        }

        //Use a StringBuilder to store all readLine first
        StringBuilder stringBuilder = new StringBuilder();

        //While not EOF
        while(line != null){
            //Add the newly read line into stringBuilder
            stringBuilder.append(line);
            //Adds back the newLine taken out by readLine()
            stringBuilder.append("\n");
            //Assign the bufferedReader to next line
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                System.err.println("Failed to read data in " + fileName);
            }
        }

        //Close the bufferedReader
        try {
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Failed to close the buffered reader");
        }

        //Converts the stringBuilder back to String and return
        String data = stringBuilder.toString();
        return data;
    }
}
