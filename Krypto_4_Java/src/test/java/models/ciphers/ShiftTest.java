package models.ciphers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShiftTest {
  private CipherInterface cipher;
  private final String NUMBERS = "0123456789";
  private final String SHORT_ALPHA  = "abc";
  private final String LONG_ALPHA   = "abcdefghijklmnopqrstuvwxyz";
  private final String SYMBOLS      = "!@#$%^&*()";
  private final String SPACES       = "          ";

  @Before
  public void init() {
    cipher = new Shift();
  }

  @Test
  public void checkKey_Numbers() {
    Assert.assertTrue(cipher.checkKey(NUMBERS));
  }

  @Test
  public void checkKey_SingleNumbers() {
    for (Character n: NUMBERS.toCharArray()) {
      Assert.assertTrue(cipher.checkKey(n.toString()));
    }
  }

  @Test
  public void checkKey_Numbers_Spaces() {
    String key = NUMBERS + SPACES;

    //will always be trimmed when in use irl.
    key = key.trim();
    Assert.assertTrue(cipher.checkKey(key));
  }

  @Test
  public void checkKey_Mixed() {
    Assert.assertFalse(cipher.checkKey(NUMBERS + SHORT_ALPHA));
  }

  @Test
  public void checkKey_Mixed_Spaces() {
    Assert.assertFalse(cipher.checkKey(NUMBERS + SPACES + SHORT_ALPHA));
  }

  @Test
  public void encrypt_LongAlpha() {
    String answer = cipher.encrypt(LONG_ALPHA, "5");
    Assert.assertEquals("fghijklmnopqrstuvwxyzabcde", answer);
  }

  @Test
  public void encrypt_LongAlpha_ZeroShift() {
    String answer = cipher.encrypt(LONG_ALPHA, "0");
    Assert.assertEquals("abcdefghijklmnopqrstuvwxyz", answer);
  }

  @Test
  public void encrypt_ShortAlpha() {
    String answer = cipher.encrypt(SHORT_ALPHA, "5");
    Assert.assertEquals("fgh", answer);
  }

  @Test
  public void encrypt_ShortAlpha_ZeroShift() {
    String answer = cipher.encrypt(SHORT_ALPHA, "0");
    Assert.assertEquals("abc", answer);
  }

  @Test
  public void decrypt_LongAlpha() {
    String answer = cipher.decrypt("fghijklmnopqrstuvwxyzabcde", "5");
    Assert.assertEquals(LONG_ALPHA, answer);
  }

  @Test
  public void decrypt_LongAlpha_ZeroShift() {
    String answer = cipher.decrypt("abcdefghijklmnopqrstuvwxyz", "0");
    Assert.assertEquals(LONG_ALPHA, answer);
  }

  @Test
  public void decrypt_ShortAlpha() {
    String answer = cipher.decrypt("fgh", "5");
    Assert.assertEquals(SHORT_ALPHA, answer);
  }

  @Test
  public void decrypt_ShortAlpha_ZeroShift() {
    String answer = cipher.decrypt("abc", "0");
    Assert.assertEquals(SHORT_ALPHA, answer);
  }
}
