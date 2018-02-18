
package com.example.FYP.aardvark_project.Ciphers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranspositionPeriodic
        extends AbstractBlockCipher
        implements CipherInterface {


    private static final String DESC = "Periodic Transposition Cipher";
    private static final String NAME = "Periodic Transposition Cipher";

    @Override
    public String encrypt(String plaintext, String key) {

        plaintext = pad(plaintext, key.length());

        List<String> splitPlain = splitStrings(plaintext, key.length());
        List<Integer> convertedKey = convertToKey(key);
        StringBuilder ciphertext = new StringBuilder();

        for (String block : splitPlain) {
            ciphertext.append(encryptBlock(block, convertedKey));
        }

        return ciphertext.toString();
    }

    @Override
    public String decrypt(String ciphertext, String key) {

        List<String> splitCipher = splitStrings(ciphertext, key.length());
        List<Integer> convertedKey = convertToKey(key);
        StringBuilder plaintext = new StringBuilder();

        for (String block: splitCipher) {
            plaintext.append(decryptBlock(block, convertedKey));
        }

        //System.out.println("plaintext = " + plaintext);
        return plaintext.toString();
    }

    @Override
    public String getDescription() { return DESC; }

    @Override
    public String getName() { return NAME; }

    private ArrayList<Integer> convertToKey (String key) {
        String temp = key.toLowerCase();

        String sorted = sortString(temp);
        ArrayList<Integer> intKey = new ArrayList<>(temp.length());
        ArrayList<Integer> returnable = new ArrayList<>();
        for (int i = 1; i <= temp.length(); ++i) { intKey.add(i); }

        int ret, index;
        for (Character c: temp.toCharArray()) {
            index = find (c, sorted); //get index of the first c in sorted;

            //remove first instance of c from sorted
            sorted = sorted.replaceFirst(c.toString(), "");

            ret = intKey.get(index);
            intKey.remove(index);

            returnable.add(ret);
        }

        return returnable;
    }

    private String encryptBlock (String plaintext, List<Integer> key) {
        StringBuilder out = new StringBuilder();

        for (Integer k: key) {
            out.append(plaintext.charAt(k - 1));
        }

        return out.toString();
    }

    private String decryptBlock (String ciphertext, List<Integer> key) {
        StringBuilder out = new StringBuilder();
        int nextCharacter = 1;
        int index = 0;

        while (nextCharacter <= key.size()) {
            for (Integer k : key) {
                if (k == nextCharacter) {
                    out.append(ciphertext.charAt(index));
                    ++nextCharacter;
                }

                ++index;
            }

            if (index >= key.size()) {
                index = 0;
            }
        }

        return out.toString();
    }

    //returns the first instance of ch in str
    private int find (Character ch, String str) {
        int count = 0;
        for (Character c: str.toCharArray()) {
            if (ch == c) return count;

            ++count;
        }

        return -1;
    }

    private String sortString (String key) {
        char[] temp = key.toCharArray();
        Arrays.sort(temp);
        return new String (temp);
    }
}