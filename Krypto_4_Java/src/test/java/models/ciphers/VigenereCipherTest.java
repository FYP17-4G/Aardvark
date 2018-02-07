/**
 * GenericCipher.java
 * Checks if Ciphers are working properly.
 * Copyright (C) 2018 fyp17.4g.
 * @author fyp17.4g
 */

package models.ciphers;

//CHECKSTYLE:OFF
import static org.junit.Assert.*;
import org.junit.*;
//CHECKSTYLE:ON

import models.Project;

/**
 * Checks all the methods of the ViginereCipher class.
 */
public class VigenereCipherTest {

    //CIPHERTEXT CONSTANTS

    /**
     * Ciphertext when
     * the plaintext consists of null and
     * the key consists of null.
     */
    public static final String CT_PTNULL_KNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of null and
     * the key consists of empty.
     */
    public static final String CT_PTNULL_KEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of null and
     * the key consists of only number.
     */
    public static final String CT_PTNULL_KONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of null and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTNULL_KONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of null and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTNULL_KONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of null and
     * the key consists of only unprintables.
     */
    public static final String CT_PTNULL_KONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of null and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTNULL_KALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext is empty and
     * the key consists of null.
     */
    public static final String CT_PTEMPTY_KNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext is empty and
     * the key consists of empty.
     */
    public static final String CT_PTEMPTY_KEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext is empty and
     * the key consists of only number.
     */
    public static final String CT_PTEMPTY_KONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext is empty and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTEMPTY_KONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext is empty and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTEMPTY_KONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext is empty and
     * the key consists of only unprintables.
     */
    public static final String CT_PTEMPTY_KONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext is empty and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTEMPTY_KALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );


    /**
     * Ciphertext when
     * the plaintext consists of numbers only, and
     * the key consists of null.
     */
    public static final String CT_PTNUMBERS_KNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of numbers only, and
     * the key consists of empty.
     */
    public static final String CT_PTNUMBERS_KEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of numbers only, and
     * the key consists of only number.
     */
    public static final String CT_PTNUMBERS_KONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of numbers only, and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTNUMBERS_KONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of numbers only, and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTNUMBERS_KONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of numbers only, and
     * the key consists of only unprintables.
     */
    public static final String CT_PTNUMBERS_KONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of numbers only, and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTNUMBERS_KALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of symbols only, and
     * the key consists of null.
     */
    public static final String CT_PTSYMBOLS_KNULLKNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of symbols only, and
     * the key consists of empty.
     */
    public static final String CT_PTSYMBOLS_KNULLKEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of symbols only, and
     * the key consists of only number.
     */
    public static final String CT_PTSYMBOLS_KNULLKONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of symbols only, and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTSYMBOLS_KNULLKONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of symbols only, and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTSYMBOLS_KNULLKONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of symbols only, and
     * the key consists of only unprintables.
     */
    public static final String CT_PTSYMBOLS_KNULLKONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of symbols only, and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTSYMBOLS_KNULLKALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of lower-case characters, and
     * the key consists of null.
     */
    public static final String CT_PTLOWER_KNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of lower-case characters, and
     * the key consists of empty.
     */
    public static final String CT_PTLOWER_KEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of lower-case characters, and
     * the key consists of only number.
     */
    public static final String CT_PTLOWER_KONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of lower-case characters, and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTLOWER_KONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of lower-case characters, and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTLOWER_KONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of lower-case characters, and
     * the key consists of only unprintables.
     */
    public static final String CT_PTLOWER_KONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of lower-case characters, and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTLOWER_KALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of null.
     */
    public static final String CT_PTUPPER_KNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of empty.
     */
    public static final String CT_PTUPPER_KEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only number.
     */
    public static final String CT_PTUPPER_KONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTUPPER_KONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTUPPER_KONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only unprintables.
     */
    public static final String CT_PTUPPER_KONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTUPPER_KALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of unprintable characters, and
     * the key consists of null.
     */
    public static final String CT_PTUNPRINTABLE_KNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of unprintable characters, and
     * the key consists of empty.
     */
    public static final String CT_PTUNPRINTABLE_KEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of unprintable characters, and
     * the key consists of only number.
     */
    public static final String CT_PTUNPRINTABLE_KONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of unprintable characters, and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTUNPRINTABLE_KONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of unprintable characters, and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTUNPRINTABLE_KONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of unprintable characters, and
     * the key consists of only unprintables.
     */
    public static final String CT_PTUNPRINTABLE_KONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of unprintable characters, and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTUNPRINTABLE_KALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of all kinds of characters, and
     * the key consists of null.
     */
    public static final String CT_PTALLKINDS_KNULL = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of all kinds of characters, and
     * the key consists of empty.
     */
    public static final String CT_PTALLKINDS_KEMPTY = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only number.
     */
    public static final String CT_PTALLKINDS_KONLYNUMBERS = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only lower-case alphabets.
     */
    public static final String CT_PTALLKINDS_KONLYLOWER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only UPPER-case alphabets.
     */
    public static final String CT_PTALLKINDS_KONLYUPPER = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only unprintables.
     */
    public static final String CT_PTALLKINDS_KONLYUNPRINTABLE = new String(
        ""//expected result of encrypting the plaintext by key.
    );

    /**
     * Ciphertext when
     * the plaintext consists of all kinds of characters, and
     * the key consists of all kinds of characters.
     */
    public static final String CT_PTALLKINDS_KALLKINDS = new String(
        ""//expected result of encrypting the plaintext by key.
    );


    /**
     * Shared Test Variable.
     */
    private static VigenereCipher cipher;

    /**
     * Shared Test Variable.
     */
    private static Project project;

    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void init() {

        cipher = new VigenereCipher();

        project = new Project(
            "TEST_PROJECT",
            //CHECKSTYLE:OFF
            777,
            //CHECKSTYLE:ON
            "INPUT_TEXT_HERE"
        );
    }

    //checkKey() Tests

    /**
     * Checks if the key is valid when it is NULL.
     */
    @Test
    public void checkKeyTestNULL() {
        //Assert.assertTrue(checkKey(CipherTestConstants.KEY_NULL));
        //Assert.assertFalse(checkKey(CipherTestConstants.KEY_NULL));
    }

    /**
     * Checks if the key is valid when it is EMPTY.
     */
    @Test

    public void checkKeyTestEMPTY() {
        // Assert.assertTrue(cipher.checkKey(CipherTestConstants.KEY_EMPTY));
        Assert.assertFalse(cipher.checkKey(CipherTestConstants.KEY_EMPTY));
    }

    /**
     * Checks if the key is valid when it consists only of numbers.
     */
    @Test

    public void checkKeyTestNUMBERS() {
        //Assert.assertTrue(checkKey(CipherTestConstants.KEY_ONLY_NUMBERS));
        //Assert.assertFalse(checkKey(CipherTestConstants.KEY_ONLY_NUMBERS));
    }

    /**
     * Checks if the key is valid when it consists only of SYMBOLS.
     */
    @Test

    public void checkKeyTestSYMBOLS() {
        //Assert.assertTrue(checkKey(CipherTestConstants.KEY_ONLY_SYMBOLS));
        //Assert.assertFalse(checkKey(CipherTestConstants.KEY_ONLY_SYMBOLS));
    }

    /**
     * Checks if the key is valid when it consists only of lower-case char.
     */
    @Test

    public void checkKeyTestLOWER() {
        //Assert.assertTrue(checkKey(CipherTestConstants.KEY_ONLY_LOWER));
        //Assert.assertFalse(checkKey(CipherTestConstants.KEY_ONLY_LOWER));
    }

    /**
     * Checks if the key is valid when it consists only of UPPER-case char.
     */
    @Test

    public void checkKeyTestUPPER() {
        //Assert.assertTrue(checkKey(CipherTestConstants.KEY_ONLY_UPPER));
        //Assert.assertFalse(checkKey(CipherTestConstants.KEY_ONLY_UPPER));
    }

    /**
     * Checks if the key is valid when it consists only of unprintables.
     */
    @Test

    public void checkKeyTestUNPRINTABLE() {
        //Assert.assertTrue(checkKey(CipherTestConstants.KEY_ONLY_UNPRINTABLE));
    //Assert.assertFalse(checkKey(CipherTestConstants.KEY_ONLY_UNPRINTABLE));
    }

    /**
     * Checks if the key is valid when it consists of all kinds of char.
     */
    @Test

    public void checkKeyTestALLKINDS() {
        //Assert.assertTrue(checkKey(CipherTestConstants.KEY_ALL_KINDS));
        //Assert.assertFalse(checkKey(CipherTestConstants.KEY_ALL_KINDS));
    }



    //encrypt() tests

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of null and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTNULLKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTNULL_KNULL
        );
    }

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of null and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTNULLKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTNULL_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of null and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTNULLKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTNULL_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of null and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTNULLKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTNULL_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of null and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTNULLKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTNULL_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of null and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTNULLKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTNULL_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of null and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTNULLKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTNULL_KALLKINDS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext is empty and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTEMPTYKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_EMPTY,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTEMPTY_KNULL
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext is empty and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTEMPTYKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_EMPTY,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTEMPTY_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext is empty and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTEMPTYKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_EMPTY,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTEMPTY_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext is empty and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTEMPTYKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_EMPTY,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTEMPTY_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext is empty and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTEMPTYKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_EMPTY,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTEMPTY_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext is empty and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTEMPTYKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_EMPTY,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTEMPTY_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext is empty and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTEMPTYKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_EMPTY,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTEMPTY_KALLKINDS
        );
    };


    /**
     * Checks the output of encrypt() when
     * the plaintext consists of numbers only, and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTNUMBERSKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_NUMBERS,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTNULL_KNULL
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of numbers only, and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTNUMBERSKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_NUMBERS,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTNULL_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of numbers only, and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTNUMBERSKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_NUMBERS,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTNULL_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of numbers only, and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTNUMBERSKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_NUMBERS,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTNULL_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of numbers only, and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTNUMBERSKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_NUMBERS,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTNULL_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of numbers only, and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTNUMBERSKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_NUMBERS,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTNULL_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of numbers only, and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTNUMBERSKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_NUMBERS,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTNULL_KALLKINDS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of symbols only, and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTSYMBOLSKNULLKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTNULL_KNULL
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of symbols only, and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTSYMBOLSKNULLKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_ONLY_SYMBOLS,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTNULL_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of symbols only, and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTSYMBOLSKNULLKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTNULL_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of symbols only, and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTSYMBOLSKNULLKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTNULL_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of symbols only, and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTSYMBOLSKNULLKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTNULL_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of symbols only, and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTSYMBOLSKNULLKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTNULL_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of symbols only, and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTSYMBOLSKNULLKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTNULL_KALLKINDS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of lower-case characters, and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTLOWERKNULLKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTNULL_KNULL
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of lower-case characters, and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTLOWERKNULLKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTNULL_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of lower-case characters, and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTLOWERKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTNULL_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of lower-case characters, and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTLOWERKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTNULL_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of lower-case characters, and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTLOWERKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTNULL_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of lower-case characters, and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTLOWERKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTNULL_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of lower-case characters, and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTLOWERKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTNULL_KALLKINDS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTUPPERKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTNULL_KNULL
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTUPPERKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTNULL_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTUPPERKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTNULL_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTUPPERKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTNULL_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTUPPERKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTNULL_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTUPPERKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTNULL_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of UPPER-case characters, and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTUPPERKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTNULL_KALLKINDS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of unprintable characters, and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTUNPRINTABLEKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTNULL_KNULL
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of unprintable characters, and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTUNPRINTABLEKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTNULL_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of unprintable characters, and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTUNPRINTABLEKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTNULL_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of unprintable characters, and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTUNPRINTABLEKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTNULL_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of unprintable characters, and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTUNPRINTABLEKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTNULL_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of unprintable characters, and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTUNPRINTABLEKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTNULL_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of unprintable characters, and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTUNPRINTABLEKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTNULL_KALLKINDS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of all kinds of characters, and
     * the key consists of null.
     */
    @Test
    public void encryptTestPTALLKINDSKNULL() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_NULL
            ),
            CT_PTNULL_KNULL
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of all kinds of characters, and
     * the key consists of empty.
     */
    @Test
    public void encryptTestPTALLKINDSKEMPTY() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_EMPTY
            ),
            CT_PTNULL_KEMPTY
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only number.
     */
    @Test
    public void encryptTestPTALLKINDSKONLYNUMBERS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_NUMBERS
            ),
            CT_PTNULL_KONLYNUMBERS
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only lower-case alphabets.
     */
    @Test
    public void encryptTestPTALLKINDSKONLYLOWER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_LOWER
            ),
            CT_PTNULL_KONLYLOWER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only UPPER-case alphabets.
     */
    @Test
    public void encryptTestPTALLKINDSKONLYUPPER() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UPPER
            ),
            CT_PTNULL_KONLYUPPER
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of all kinds of characters, and
     * the key consists of only unprintables.
     */
    @Test
    public void encryptTestPTALLKINDSKONLYUNPRINTABLE() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ONLY_UNPRINTABLE
            ),
            CT_PTNULL_KONLYUNPRINTABLE
        );
    };

    /**
     * Checks the output of encrypt() when
     * the plaintext consists of all kinds of characters, and
     * the key consists of all kinds of characters.
     */
    @Test
    public void encryptTestPTALLKINDSKALLKINDS() {
        Assert.assertEquals(
            cipher.encrypt(
                CipherTestConstants.PT_NULL,
                CipherTestConstants.KEY_ALL_KINDS
            ),
            CT_PTNULL_KALLKINDS
        );
    };

    /**
     * shuts checkstyle up.
     */
    public void foo() {
        //do nothing
    }
}

