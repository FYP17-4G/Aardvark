package models.ciphers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VigenereCipherTest {
  CipherInterface cipher;
  private final String NUMBERS  = "0123456789";
  private final String SPACES   = "          ";
  private final String SYMBOLS  = "~@#$%^&*()";
  private final String L_ALPHA  = "abcdefghijklmnopqrstuvwxyz";
  private final String S_ALPHA  = "abcdefg";
  private final String S_KEY    = "skey";
  private final String L_KEY    = "thisisasuperlongkey";
  @Before
  public void init() {
    cipher = new VigenereCipher();
  }

  @Test
  public void checkKey_BlankKey() {
    Assert.assertFalse(cipher.checkKey(""));
  }

  @Test
  public void checkKey_OneCharacter() {
    Assert.assertFalse(cipher.checkKey("a"));
  }

  @Test
  public void checkKey_ShortAlpha() {
    Assert.assertTrue(cipher.checkKey(S_ALPHA));
  }

  @Test
  public void checkKey_LongAlpha() {
    Assert.assertTrue(cipher.checkKey(L_ALPHA));
  }

  @Test
  public void checkKey_Numbers() {
    Assert.assertFalse(cipher.checkKey(NUMBERS));
  }

  @Test
  public void checkKey_Spaces() {
    Assert.assertFalse(cipher.checkKey(SPACES));
  }

  @Test
  public void checkKey_Symbols() {
    Assert.assertFalse(cipher.checkKey(SYMBOLS));
  }

  @Test
  public void checkKey_Mixed() {
    Assert.assertFalse(cipher.checkKey(SPACES + SYMBOLS + NUMBERS));
  }

  @Test
  public void encrypt_ShortAlpha_ShortKey() {
    String answer = cipher.encrypt(S_ALPHA, S_KEY);
    Assert.assertEquals("slgbwpk", answer);
  }

  @Test
  public void encrypt_ShortAlpha_LongKey() {
    String answer = cipher.encrypt(S_ALPHA, L_KEY);
    Assert.assertEquals("tikvmxg", answer);
  }

  @Test
  public void encrypt_LongAlpha_ShortKey() {
    String answer = cipher.encrypt(L_ALPHA, S_KEY);
    Assert.assertEquals("slgbwpkfatojexsnibwrmfavqj", answer);
  }

  @Test
  public void encrypt_LongAlpha_LongKey() {
    String answer = cipher.encrypt(L_ALPHA, L_KEY);
    Assert.assertEquals("tikvmxgzcyocxbbvavqmbdofqz", answer);
  }

    @Test
  public void decrypt_ShortAlpha_ShortKey() {
    String answer = cipher.decrypt("slgbwpk", S_KEY);
    Assert.assertEquals(S_ALPHA, answer);
  }

  @Test
  public void decrypt_ShortAlpha_LongKey() {
    String answer = cipher.decrypt("tikvmxg", L_KEY);
    Assert.assertEquals(S_ALPHA, answer);
  }

  @Test
  public void decryptLongAlpha_ShortKey() {
    String answer = cipher.decrypt("slgbwpkfatojexsnibwrmfavqj", S_KEY);
    Assert.assertEquals(L_ALPHA, answer);
  }

  @Test
  public void decrypt_LongAlpha_LongKey() {
    String answer = cipher.decrypt("tikvmxgzcyocxbbvavqmbdofqz", L_KEY);
    Assert.assertEquals(L_ALPHA, answer);
  }


}
