package com.example.ekanugrahapratama.aardvark_project.kryptoTools;

public class ShiftCipher {
    private static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    //to prevent instantiation
    public ShiftCipher() {}

    /**Slightly modified*/
    public static String encrypt(String data, Integer offset) {
        String returnVal = new String();
        int output;

        for(int i = 0; i < data.length(); i++)
        {
            if(Character.isLetter(data.charAt(i)))
            {
                output = data.charAt(i) - 'a' + offset;
                output %= 26;
                returnVal += alphabet.charAt(output);
            }
            else
                returnVal += data.charAt(i);
        }

        return returnVal;
    }

    /**Slightly modified*/
    public static String decrypt (String data, Integer offset) {
        String returnVal = new String();
        int output;

        for(int i = 0; i < data.length(); i++)
        {
            if(Character.isLetter(data.charAt(i)))
            {
                output = data.charAt(i)- 'a' - offset;
                if (output < 0) {
                    output += 26;
                }
                output %= 26;
                returnVal += alphabet.charAt(output);
            }
            else
                returnVal += data.charAt(i);
        }

        return returnVal;
    }
}
