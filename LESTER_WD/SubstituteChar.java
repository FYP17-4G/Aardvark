import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SubstituteChar {

    //To test functionality
    public static void main(String[] args) {

        //Get user to input the data file name
        System.out.print("Enter the input file name: ");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        //Invoke the constructor with the file name
        SubstituteChar subChar = new SubstituteChar(fileName);
        System.out.println(subChar.originalData);//TEST
        //Define a modifiedData for modification, so as to preserve the original data
        subChar.setModifiedData(subChar.originalData);

        while(true){
            System.out.print("Enter character to be replaced: ");
            //Grab the first character of the input, assuming there's no input validation
            char ch1 = scanner.next().charAt(0);
            //Grab the first character of the input, assuming there's no input validation
            System.out.print("Enter character to replace with: ");
            char ch2 = scanner.next().charAt(0);

            //Substitute the character and print out the modifiedData for reference
            subChar.substitute(ch1, ch2);
            System.out.println(subChar.modifiedData);
        }

    }

    //Private variables
    private String originalData; //Preserve original data
    private String modifiedData; //Modification data

    //Default constructor
    public SubstituteChar(String fileName){
        //When the object is created, original data will be initialized
        setOriginalData(readFile(fileName));
    }

    //Function to set originalData
    public void setOriginalData(String data){
        this.originalData = data;
    }

    //Function to set modifiedData
    public void setModifiedData(String data){
        this.modifiedData = data;
    }

    //Function to substitute character
    public void substitute(char one, char two){
        //Convert everything to lowercase
        modifiedData.toLowerCase();
        one = Character.toLowerCase(one);
        two = Character.toLowerCase(two);

        setModifiedData(modifiedData.replace(one, two));

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
