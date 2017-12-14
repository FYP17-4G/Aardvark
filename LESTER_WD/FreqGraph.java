import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FreqGraph {

    private String originalData;
    private ArrayList<Integer> frequencyList;
    private ArrayList<ArrayList<Integer>> frequencyByPeriod;
    private static final String characterList = "abcdefghijklmnopqrstuvwxyz";

    //For testing purposes
    public static void main(String[] args) {
        //Get user to input the data file name
//        System.out.print("Enter the input file name: ");
        Scanner scanner = new Scanner(System.in);
//        String fileName = scanner.nextLine();

        //Invoke the constructor with filename
        FreqGraph freqGraph = new FreqGraph("HELLO.TXT");
        freqGraph.calcFrequency(freqGraph.originalData);

        //Testing purpose
        while(true){
            System.out.print("Enter index:");
            int indexOfKey = scanner.nextInt();
            System.out.print("Enter period:");
            int periodOfKey = scanner.nextInt();
            freqGraph.calcFrequency(freqGraph.originalData, indexOfKey, periodOfKey);
            System.out.println("\n");
        }

    }

    //Default Constructor
    public FreqGraph(){

    }

    //Secondary Constructor
    public FreqGraph(String fileName){
        setOriginalData(readFile(fileName));
        initalizeArray();
    }

    //Calculate the frequency of each letter with given indexOfKey and periodOfKey
    public void calcFrequency(String originalData, int indexOfKey, int periodOfKey){
        //Convert all to lowercase
        String tempString = originalData.toLowerCase();
        //Remove all symbols
        tempString = tempString.replaceAll("[^a-z]", "");

        ArrayList<String> splitByPeriod = new ArrayList<>();

        //Split the string based on periodOfKey
        int startIndex = 0, endIndex = periodOfKey;
        for(int i = 0; i < tempString.length()/periodOfKey; i++){
            splitByPeriod.add(tempString.substring(startIndex, endIndex));
            startIndex += periodOfKey;
            endIndex += periodOfKey;
        }

        //Initialize the ArrayList for frequencyOfPeriod
        this.frequencyByPeriod = new ArrayList<>(tempString.length()/periodOfKey + 1);
        for(int h = 0; h < tempString.length()/periodOfKey; h++){
            this.frequencyByPeriod.add(new ArrayList<>(26));
            for(int l = 0; l < 26; l++){
                this.frequencyByPeriod.get(h).add(0);
            }
        }

        //Calculate the frequency of each letter by period
        for(int j = 0; j < splitByPeriod.size(); j++){
            for(Character c: splitByPeriod.get(j).toCharArray()){
                for(int k = 0; k < characterList.length(); k++){
                    if(c.equals(characterList.charAt(k))){
                        //Set the frequency list at k, of ArrayList j
                        this.frequencyByPeriod.get(j).set(k, (frequencyByPeriod.get(j).get(k) + 1));
                    }
                }
            }
        }

        //TEST results
        for(int h = 0; h < tempString.length()/periodOfKey; h++){
            for(int l = 0; l < 26; l++){
                System.out.print("[" + this.characterList.charAt(l) + ":" + this.frequencyByPeriod.get(h).get(l) + "]");
            }
            System.out.println("\n");
        }

        System.out.println("The graph at index " + indexOfKey + " of period " + periodOfKey + " is: ");
        for(int l = 0; l < 26; l++){
            System.out.print("[" + this.characterList.charAt(l) + ":" + this.frequencyByPeriod.get(indexOfKey).get(l) + "]");
        }
//        System.out.println(tempString);
//        System.out.println(tempString.length() + " " + periodOfKey + " " + tempString.length()/periodOfKey);
//        System.out.println(tempString.length()/periodOfKey);
//        System.out.println(splitByPeriod + "\n");//TEST

    }

    //Calculate the frequency of each letter
    public void calcFrequency(String originalData){
        String tempString = originalData.toLowerCase();

        for(Character c: tempString.toCharArray()){
            for(int i = 0; i < characterList.length(); i++){
                if(c.equals(characterList.charAt(i))){
                    this.frequencyList.set(i, frequencyList.get(i)+1);
                }
            }
        }

        //TEST
        System.out.println("Original function(g) results: ");
        for(int i = 0; i < 26; i++){
            System.out.print("[" + characterList.charAt(i) + ": " + frequencyList.get(i) + "]");
        }
        System.out.println("\n");
    }

    //compiles every nth letter into an ArrayList of strings, according to n.
    //data entered here is the whole set of data. the function will split the string accordingly.
    //ignores punctuation and spaces.
    //e.g. getEveryNthLetter (4, hello world!) results in:
    //1. hol    2. ewd
    //3. lo     4. lr
    private static ArrayList<StringBuilder> getEveryNthLetter(Integer n, String data) {
        data = data.replaceAll("[^A-Za-z]", "");
        ArrayList<StringBuilder> split = new ArrayList<>(n);
        int counter = 0;

        for (int i = 0; i < n; ++i) {
            split.add(new StringBuilder());
        }

        for (Character c: data.toLowerCase().toCharArray()) {
            split.get(counter).append(c);
            ++counter;

            if (counter >= n)
                counter = 0;
        }

        return split;
    }

    public void initalizeArray(){

        this.frequencyList = new ArrayList<>(26);
        for(int i = 0; i < 26; i++){
            this.frequencyList.add(0);
        }
    }

    //Function to set originalData
    public void setOriginalData(String data){
        this.originalData = data;
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
