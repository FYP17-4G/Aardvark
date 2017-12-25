/*
* This is a class for intelligent suggestion system
* It analyzes the cipher text and determines what kind of classical cipher text being used
* accuracy of the algorithm: UNKNOWN
* */

package com.example.ekanugrahapratama.aardvark_project.analysisTools;

import com.example.ekanugrahapratama.aardvark_project.App_Framework;
import com.example.ekanugrahapratama.aardvark_project.GUI_projectView;

public class CryptoAnalysis extends GUI_projectView
{
    private String originalCipherText;

    private App_Framework framework = new App_Framework(this);

    //common english character distribution letter A -> Z (HARDCODED)
    private double[] englishDist = {8.12, 1.49, 2.71, 4.32, 12.02, 2.30, 2.03, 5.92, 7.31, 0.10, 0.69, 3.98, 2.61, 6.95, 7.68, 1.82, 0.11, 6.02, 6.28, 9.10, 2.88, 1.11, 2.09, 0.17, 2.11, 0.07};
    private double englishIC = 0.065;
    private double originalCipherTextIC = getCipherIC(originalCipherText);

    CryptoAnalysis(String cText)
    {
        this.originalCipherText = cText;

        analyze();
    }

    public void analyze()
    {

    }

    //Step #1: Transposition
    //check if the cipher text is encyrpted using transposition algorithm using monogram FREQUENCY
    //since transpo only changes the order of letter, it should not affect the IC value.
    //if true = columnar transposition, simple transposition, Rail fence
    private boolean isTransposition(double dev) // dev = degree of deviation, try value around 1.00 to 2.00
    {
        for(int i = 0; i < 26; i++) // 26 length of englishDist
        {
            double ub = englishDist[i] + (englishDist[i]/dev); //upper bound
            double lb = englishDist[i] - (englishDist[i]/dev); //lower bound
            if(lb < 0)
                lb = 0;

            //get the frequency of a letter F/N (F = frequency of a letter; N = length of the cipher text). Then, if its NOT within ub, lb, return false
        }

        return true;
    }

    //TODO(1) RECHECK THIS ALGORITHM
    //Step #2: Substitution
    //if true = Affine, simple substitution, Homophonic,
    private boolean isSubstitution(double IC, double dev) // dev = degree of deviation, the value of dev variable here is in small decimal, try to pass 0.010
    {
        double ub = englishIC + dev;
        double lb = englishIC - dev;
        if(lb < 0)
            lb = 0;

        if((IC < lb) || (IC > ub))
            return false;

        return true;
    }

    //step #3: Polymorphic
    //if true = Vigenere, Beufort, Porta, Gronsfeld
    private boolean isPolymorphic()
    {
        //calculate index of coincidence for every period for the cipher text, if any of them has IC value around 0.065, then that is likely the key
        //also, since we are analyzing the IC of a polymorpic ciphertext, if the cipher text is found to be a polymorphic, set the default value of the period in project view --
        //activity to whatever the period that has (+ -) 0.065 IC.

        return true;
    }

    //step #4: Bigraphic Cipher
    //if true = Playfair, Foursquare
    private boolean isBigraphic()
    {
        int cipherLength = framework.stringNoWhiteSpace(originalCipherText).length();

        //meaning the length of the ciphertext is not even
        if((cipherLength%2) == 1)
            return false;

        return true;
    }
}
