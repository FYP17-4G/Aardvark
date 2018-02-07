/**
 * CipherInterface.java
 * Defines all the common methods a Cipher should have
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package models.ciphers;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines all the common methods a Cipher should have.
 */
public abstract class AbstractBlockCipher
    extends AbstractCipher
    implements CipherInterface {
    
    String pad(String plaintext, int keylength) {
        StringBuilder sb = new StringBuilder(plaintext);

        while ((sb.length() % keylength) != 0)
            sb.append(getRandAlphabet());

        return sb.toString();
    }

    String unpad(String paddedText, int originalLength) {
        return paddedText.substring(0, originalLength);
    }

    Character getRandAlphabet() {
        Integer random = (int) (Math.random() * (ALPHABETS.length()));
        return ALPHABETS.charAt(random);
    }

    List<String> splitStrings(String input, int keylength) {
        StringBuilder temp = new StringBuilder();
        List<String> out = new ArrayList<>();
        int currentCount = 0;

        for (Character c : input.toCharArray()) {
            temp.append(c);
            ++currentCount;

            if (currentCount >= keylength) {
                out.add(temp.toString());
                temp = new StringBuilder();
                currentCount = 0;
            }
        }
        return out;
    }

}

