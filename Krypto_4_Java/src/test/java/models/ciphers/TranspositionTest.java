package models.ciphers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranspositionTest {
  private CipherInterface cipher;
  private final String NUM              = "1234567890";
  private final String SHORT_ALPHA      = "abc";
  private final String SHORT_ALPHA_ALT  = "def";
  private final String LONG_ALPHA       = "abcdefghijklmnopqrstuvwxyz";
  private final String LONG_ALPHA_ALT   = "zyxwvutsrqponmlkjihgfedcba";
  private final String UNIQUE_KEY       = "GHAJRIOBESQCLFVZTYKMXWNUDP".toLowerCase();
  private final String SYMBOLS          = "!@#$%^&*()";
  private final String SPACES           = "          ";
  private final String EMPTY            = "";
  private final String TR_KEY           = "SECRET";

  @Before
  public void init() {
    cipher = new Transposition();
  }

  @Test
  public void checkKey_Duplicates() {
    Assert.assertTrue(cipher.checkKey(LONG_ALPHA + SHORT_ALPHA));
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

  @Test
  public void encrypt_ShortAlpha() {
    String answer = cipher.encrypt(SHORT_ALPHA_ALT, TR_KEY);
    Assert.assertEquals("fed", answer);
  }

  @Test
  public void encrypt_LongAlpha() {
    String answer = cipher.encrypt(LONG_ALPHA_ALT, TR_KEY);
    Assert.assertEquals("xyvwzurspqtolmjknifgdehcab", answer);
  }

  @Test
  public void decrypt_ShortAlpha() {
    String answer = cipher.decrypt("fed", TR_KEY);
    Assert.assertEquals(SHORT_ALPHA_ALT, answer);
  }

  @Test
  public void decrypt_LongAlpha() {
    String answer = cipher.decrypt("xyvwzurspqtolmjknifgdehcab", TR_KEY);
    Assert.assertEquals(LONG_ALPHA_ALT, answer);
  }
}
