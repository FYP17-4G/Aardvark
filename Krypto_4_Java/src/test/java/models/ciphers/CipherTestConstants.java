/**
 * CipherTestConstants.java
 * Checks if Ciphers are working properly.
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package models.ciphers;

/**
 * Stores all the cipherTestConstants.
 */
public final class CipherTestConstants {

    /**
     * Blocks the constructor.
     */
    private CipherTestConstants() {
        //do nothing
    }

    // KEY(s)

    /**
     * key is null.
     */
    public static final String KEY_NULL = null;

    /**
     * key is an empty string.
     */
    public static final String KEY_EMPTY = new String("");

    /**
     * key consists only of numbers.
     */
    public static final String KEY_ONLY_NUMBERS = new String(
        "0123456789"
    );

    /**
     * key consists of only symbols.
     */
    public static final String KEY_ONLY_SYMBOLS = new String(
        "~!@#$%^&*()`|}{\\][\":';?></.,'"
    );

    /**
     * key consists only of lower-case characters.
     */
    public static final String KEY_ONLY_LOWER = new String(
        "abcdefghijklmnopqrstuvwxyz"
    );

    /**
     * key consists only of UPPER-case characters.
     */
    public static final String KEY_ONLY_UPPER = new String(
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    );

    /**
     * key consists of all unprintables.
     */
    public static final String KEY_ONLY_UNPRINTABLE = new String(
        "\t\n\r\f\b"
    );

    /**
     * key consists of all of the above.
     */
    public static final String KEY_ALL_KINDS = new String(
        KEY_ONLY_LOWER
        + KEY_ONLY_SYMBOLS
        + KEY_ONLY_UPPER
        + KEY_ONLY_UNPRINTABLE
        + KEY_ONLY_NUMBERS
    );



    //PLAINTEXT

    /**
     * key is null.
     */
    public static final String PT_NULL = null;

    /**
     * key is an empty string.
     */
    public static final String PT_EMPTY = new String("");

    /**
     * key consists only of numbers.
     */
    public static final String PT_ONLY_NUMBERS = new String(
        "0123456789"
    );

    /**
     * key consists of only symbols.
     */
    public static final String PT_ONLY_SYMBOLS = new String(
        " ~!@#$%^&*()`|}{\\][\":';?></.,'=-"
    );

    /**
     * key consists only of lower-case characters.
     */
    public static final String PT_ONLY_LOWER = new String(
        "abcdefghijklmnopqrstuvwxyz"
    );

    /**
     * key consists only of UPPER-case characters.
     */
    public static final String PT_ONLY_UPPER = new String(
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    );

    /**
     * key consists of all unprintables.
     */
    public static final String PT_ONLY_UNPRINTABLE = new String(
        "\t\n\r\f\b"
    );

    /**
     * key consists of all of the above.
     */
    public static final String PT_ALL_KINDS = new String(
        KEY_ONLY_LOWER
        + KEY_ONLY_SYMBOLS
        + KEY_ONLY_UPPER
        + KEY_ONLY_UNPRINTABLE
        + KEY_ONLY_NUMBERS
    );

}

