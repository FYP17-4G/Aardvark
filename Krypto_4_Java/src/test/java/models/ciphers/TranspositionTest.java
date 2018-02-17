package models.ciphers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranspositionTest {
  private CipherInterface cipher;
  private final String NUM              = "1234567890";
  private final String SHORT_ALPHA      = "abc";
  private final String SHORT_ALPHA_ALT  = "abcde";
  private final String LONG_ALPHA       = "abcdefghijklmnopqrstuvwxyz";
  private final String LONG_ALPHA_ALT   = "zyxwvutsrqponmlkihgfedcba";
  private final String SYMBOLS          = "!@#$%^&*()";
  private final String SPACES           = "          ";
  private final String EMPTY            = "";
  private final String TR_KEY           = "SECRT";

  @Before
  public void init() {
    cipher = new Transposition();
  }

  @Test
  public void checkKey_Duplicates() {
    Assert.assertFalse(cipher.checkKey(LONG_ALPHA + SHORT_ALPHA));
  }

  @Test
  public void checkKey_Num() {
    Assert.assertFalse(cipher.checkKey(NUM));
  }

  @Test
  public void checkKey_Symbols() {
    Assert.assertFalse(cipher.checkKey(SYMBOLS + SPACES));
  }

  @Test
  public void checkKey_Spaces() {
    Assert.assertFalse(cipher.checkKey(SPACES));
  }

  @Test
  public void checkKey_Mixed () {
    Assert.assertFalse(cipher.checkKey(SHORT_ALPHA + NUM + SYMBOLS + SPACES));
  }

  @Test
  public void checkKey_EmptyString () {
    Assert.assertFalse(cipher.checkKey(EMPTY));
  }

//   TODO : Find a way to test padding and unpadding.
//  @Test
//  public void misc_Padding () {
//    String plaintext  = "ovnoncojndsouvpiq";
//    String key        = "thisiskey";
//
//    plaintext = cipher.pad(plaintext, key.length());
//
//    Assert.assertTrue((plaintext.length() % key.length() == 0);
//  }

  @Test
  public void encrypt_ShortAlpha() {
    String answer = cipher.encrypt(SHORT_ALPHA_ALT, TR_KEY);
     Assert.assertEquals("dbace", answer);
  }

  @Test
  public void encrypt_LongAlpha() {
    String answer = cipher.encrypt(LONG_ALPHA_ALT, TR_KEY);
    Assert.assertEquals("wyzxvrtusqmopnlgikhfbdeca", answer);
  }

  @Test
  public void decrypt_ShortAlpha() {
    String answer = cipher.decrypt("dbace", TR_KEY);
    Assert.assertEquals(SHORT_ALPHA_ALT, answer);
  }

  @Test
  public void decrypt_LongAlpha() {
    String answer = cipher.decrypt("wyzxvrtusqmopnlgikhfbdeca", TR_KEY);
    Assert.assertEquals(LONG_ALPHA_ALT, answer);
  }
}
