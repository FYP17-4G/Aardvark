package com.example.FYP.aardvark_project.Ciphers;


import java.util.*;

public class PlayfairCipher extends AbstractCipher implements CipherInterface {
    //i & j are interchangeable.
    private static final String PLAYFAIR_ALPHABET = "abcdefghiklmnopqrstuvwxyz";
    private Character[][] playfairSquare;

    @Override
    public String encrypt(String plaintext, String key)  {

        List<String> pBigrams; //plaintext bigrams
        List<String> eBigrams = new ArrayList<>(); //encrypted bigrams

        StringBuilder out = new StringBuilder();

        checkKey(key);

        //build square
        buildPlayfairSquare(key);
        System.out.println(getPlayfairSquare());

        //split plaintext into pl ai nt ex tx
        if ( (plaintext.length() % 2) != 0)
            plaintext = plaintext + getRandAlphabet();

        pBigrams = splitStrings(plaintext);

        //encrypt the bigrams
        for (String bigram: pBigrams) {
            eBigrams.add(encryptBlock(bigram));
        }

        for (String bigram: eBigrams) {
            out.append(bigram);
        }

        return out.toString();
    }

    @Override
    public String decrypt(String ciphertext, String key)  {

        List<String> pBigrams; //plaintext bigrams
        List<String> eBigrams = new ArrayList<>(); //encrypted bigrams

        StringBuilder out = new StringBuilder();

        checkKey(key);

        //build square
        buildPlayfairSquare(key);
        System.out.println(getPlayfairSquare());

        //split plaintext into pl ai nt ex tx
        if ( (ciphertext.length() % 2) != 0)
            ciphertext = ciphertext + getRandAlphabet();

        pBigrams = splitStrings(ciphertext);

        //encrypt the bigrams
        for (String bigram: pBigrams) {
            eBigrams.add(decryptBlock(bigram));
        }

        for (String bigram: eBigrams) {
            out.append(bigram);
        }

        return out.toString();
    }

    //<in> will always be a block of two different letters.
    private String encryptBlock(String in) {
        StringBuilder out = new StringBuilder();
        Character one = in.charAt(0);
        Character two = in.charAt(1);
        String cOne = getCoordinates(one);
        String cTwo = getCoordinates(two);

        if (cOne.charAt(0) == cTwo.charAt(0)) {
            //same column
            System.out.println("COL");
            int xOneTwo = Character.getNumericValue(cOne.charAt(0));
            int yOne = Character.getNumericValue(cOne.charAt(1));
            int yTwo = Character.getNumericValue(cTwo.charAt(1));

            Integer xOut, yOut;
            xOut = (xOneTwo + 1) % 5;
            yOut = (yOne) % 5;
            out.append(playfairSquare[xOut][yOut]);

            yOut = (yTwo) % 5;
            out.append(playfairSquare[xOut][yOut]);
        } else if (cOne.charAt(1) == cTwo.charAt(1)) {
            System.out.println("ROW");
            //same row
            int yOneTwo = Character.getNumericValue(cOne.charAt(1));
            int xOne = Character.getNumericValue(cOne.charAt(0));
            int xTwo = Character.getNumericValue(cTwo.charAt(0));

            Integer xOut, yOut;
            xOut = (xOne) % 5;
            yOut = (yOneTwo + 1) % 5;
            out.append(playfairSquare[xOut][yOut]);

            xOut = (xTwo) % 5;
            out.append(playfairSquare[xOut][yOut]);
        } else {
            System.out.println("RECT");
            //rectangle
            int xOne = Character.getNumericValue(cOne.charAt(0));
            int xTwo = Character.getNumericValue(cOne.charAt(1));
            int yOne = Character.getNumericValue(cTwo.charAt(0));
            int yTwo = Character.getNumericValue(cTwo.charAt(1));

            Integer xOut, yOut;
            xOut = (xOne);
            yOut = (yTwo);
            out.append(playfairSquare[xOut][yOut]);

            xOut = (xTwo);
            yOut = (yOne);
            out.append(playfairSquare[xOut][yOut]);
        }

        return out.toString();
    }

    private String decryptBlock (String in) {
        StringBuilder out = new StringBuilder();
        Character one = in.charAt(0);
        Character two = in.charAt(1);
        String cOne = getCoordinates(one);
        String cTwo = getCoordinates(two);

        if (cOne.charAt(0) == cTwo.charAt(0)) {
            //same column
            System.out.println("COL");
            int xOneTwo = Character.getNumericValue(cOne.charAt(0));
            int yOne = Character.getNumericValue(cOne.charAt(1));
            int yTwo = Character.getNumericValue(cTwo.charAt(1));

            Integer xOut, yOut;
            xOut = (xOneTwo - 1) % 5;
            yOut = (yOne) % 5;
            out.append(playfairSquare[xOut][yOut]);

            yOut = (yTwo) % 5;
            out.append(playfairSquare[xOut][yOut]);
        } else if (cOne.charAt(1) == cTwo.charAt(1)) {
            System.out.println("ROW");
            //same row
            int yOneTwo = Character.getNumericValue(cOne.charAt(1));
            int xOne = Character.getNumericValue(cOne.charAt(0));
            int xTwo = Character.getNumericValue(cTwo.charAt(0));

            Integer xOut, yOut;
            xOut = (xOne) % 5;
            yOut = (yOneTwo - 1) % 5;
            out.append(playfairSquare[xOut][yOut]);

            xOut = (xTwo) % 5;
            out.append(playfairSquare[xOut][yOut]);
        } else {
            System.out.println("RECT");
            //rectangle
            int xOne = Character.getNumericValue(cOne.charAt(0));
            int xTwo = Character.getNumericValue(cOne.charAt(1));
            int yOne = Character.getNumericValue(cTwo.charAt(0));
            int yTwo = Character.getNumericValue(cTwo.charAt(1));

            Integer xOut, yOut;
            xOut = (xOne);
            yOut = (yTwo);
            out.append(playfairSquare[xOut][yOut]);

            xOut = (xTwo);
            yOut = (yOne);
            out.append(playfairSquare[xOut][yOut]);
        }

        return out.toString();
    }

    private String getCoordinates (Character c) {
        StringBuilder out = new StringBuilder();

        if (c == 'j') c = 'i';

//        System.out.println("c = " + c);
        for (int col = 0; col < 5; ++col) {
            for (int row = 0; row < 5; ++row) {
//                System.out.print(playfairSquare[col][row] + " ");
                if (c == playfairSquare[col][row]) {
//                    System.out.println(c + " = [" + col + ", " + row + "]");
                    out.append(col);
                    out.append(row);
                }
            }
//            System.out.println();
        }

        return out.toString();
    }

    private String getPlayfairSquare () {
        StringBuilder builder = new StringBuilder();
        for (int outRow = 0; outRow < 5; ++outRow) {
            for (int outCol = 0; outCol < 5; ++outCol) {
                builder.append(playfairSquare[outRow][outCol]);

                //to make it neater when printing.
                if (outCol != 4)
                    builder.append(" ");
            }

            if (outRow != 4)
                builder.append("\n");
        }

        return builder.toString();
    }

    private void buildPlayfairSquare (String key) {
        String playfairKey = removeDuplicates(key + PLAYFAIR_ALPHABET);
        playfairSquare = new Character[5][5];

        int row = 0, column = 0;
        for (Character c: playfairKey.toCharArray()) {
            playfairSquare[row][column++] = c;

            if (column >= 5) {
                column = 0;
                row++;
            }
        }
    }

    private List<String> splitStrings (String in) {
        List<String> out = new ArrayList<>();
        StringBuilder temp = new StringBuilder();

        char one, two;
        int currentCount = 0;

        for (Character c: in.toCharArray()) {
            boolean duplicate;
            do {
                duplicate = false;
                if (!temp.toString().equals(c.toString())) {
                    temp.append(c);
                } else {
                    temp.append(getRandAlphabet());
                    duplicate = true;
                }

                ++currentCount;
                if (currentCount >= 2) {
                    out.add(temp.toString());
                    temp = new StringBuilder();
                    currentCount = 0;
                }
            } while (duplicate);


        }

        return out;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    private Character getRandAlphabet() {
        Integer random = (int) (Math.random() * (ALPHABETS.length()));
        return ALPHABETS.charAt(random);
    }

}
