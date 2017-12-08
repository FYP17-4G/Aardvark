import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PrintModData {

    //Private variables
    private String originalData; //Preserve original data
    private String modifiedData; //Modification data

    //For testing purposes
    public static void main(String[] args) {
        //Get user to input the data file name
//        System.out.print("Enter the input file name: ");
        Scanner scanner = new Scanner(System.in);
//        String fileName = scanner.nextLine();

        //Invoke the constructor with filename
        PrintModData printModData = new PrintModData("HELLO.TXT");

        //Define a modifiedData for modification purposes, to preserve original data
        printModData.setModifiedData(printModData.originalData);
        //Print a normal modified string
        printModData.print();

        //Print a customized modified string
        System.out.print("Enter number of characters: ");
        int noOfChar = scanner.nextInt();
        System.out.print("Enter period: ");
        int inBlocksOf = scanner.nextInt();

        printModData.print(noOfChar, inBlocksOf);

    }

    //Default Constructor
    public PrintModData(){

    }

    //Secondary Constructor
    public PrintModData(String fileName){
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

    //Function to print modifiedData
    public void print(){
        System.out.println(this.modifiedData);
    }

    //Function to print modifiedData with row/column specifications
    public void print(int noOfChar, int inBlocksOf){

        int startIndex = 0;

        //So total there will be dataLength/noOfChar blocks of data
        for(int k = 0; k < this.modifiedData.length()/(noOfChar*inBlocksOf); k++){

            System.out.println("Row: " + (k+1));//TEST

            //Print how many blocks at once
            for(int j = 0; j < inBlocksOf; j++) {
                System.out.print("Block: " + (j+1) + "[");//TEST

                //Print noOfChar at once
                for (int i = 0; i < noOfChar; i++) {
                    System.out.print("Char " + (i+1));//TEST
                    System.out.print(this.modifiedData.subSequence(startIndex, noOfChar) + " ");
                    System.out.print("yo");//TEST
                    startIndex = noOfChar;
                }

                System.out.print("]");
            }

            //Newline to split rows
            System.out.println("\n");
        }




    }

//    //Breaks the modifiedData inBlocks of noOfChars
//            for(int i = 0; i < inBlocksOf; i++){
//        System.out.println(this.modifiedData.subSequence(0, noOfChar) + " ");
//    }
//            System.out.println("\n");

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
