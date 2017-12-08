//This java class is designed to calculate the Index of Coincidence of
//a text.

import java.io.*;

public class CalculateIC {

    //Main function for testing purposes
    public static void main(String[] args) {//TEST
        CalculateIC test = new CalculateIC();
        String abc = test.readFile("HELLO.txt");
        System.out.println(test.calculate(abc));//TEST

    }

    //Default Constructor
    public CalculateIC(){

    }

    //Function to calculate the Index of Coincidence of entire text
    public double calculate(String data){

        //Check the number of different characters in the passage
        //Symbols are excluded
        int numberOfChars = 0;

        double sum = 0.0;
        double total = 0.0;
        //Convert all the letters to upper case
        data = data.toUpperCase();

        //initialize array of values to count frequency of each letter
        int[] values = new int[26];
        for(int i=0; i<26; i++){
            values[i] = 0;
        }

        //calculate frequency of each letter in data
        int ch;
        for(int i=0; i<data.length(); i++){
            ch = data.charAt(i)-65;
            if(ch>=0 && ch<26){
                values[ch]++;
                numberOfChars++;
            }
        }

        //calculate the sum of each frequency
        for(int i=0; i<26; i++){
            ch = values[i];
            sum = sum + (ch * (ch-1));
        }

        //divide by N(N-1)
        total = sum/(numberOfChars*(numberOfChars-1));

        //return the result
        return total;

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
