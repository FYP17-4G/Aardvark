package models.ciphers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KamasutraTest {
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

  @Before
  public void init() { cipher = new Kamasutra(); }

  @Test
  public void checkKey_CorrectLengthAlpha() {
    Assert.assertTrue(cipher.checkKey(LONG_ALPHA));
  }

  @Test
  public void checkKey_SuperLongAlpha() {
    Assert.assertFalse(cipher.checkKey(LONG_ALPHA + LONG_ALPHA_ALT));
  }

  @Test
  public void checkKey_ShortAlpha() {
    Assert.assertFalse(cipher.checkKey(SHORT_ALPHA));
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

  @Test
  public void encrypt_ShortAlpha () {
    String answer = cipher.encrypt(SHORT_ALPHA, UNIQUE_KEY);

    // G H A J R I O B E S Q C L
    // F V Z T Y K M X W N U D P

    Assert.assertTrue(answer.equals("zxd"));
  }

  @Test
  public void encrypt_LongAlpha () {
    String answer = cipher.encrypt(LONG_ALPHA, UNIQUE_KEY);
    Assert.assertTrue(answer.equals("zxdcwgfvktiposmluynjqhebra"));
  }

  @Test
  public void decrypt_ShortAlpha () {
    String answer = cipher.encrypt("zxd", UNIQUE_KEY);

    // G H A J R I O B E S Q C L
    // F V Z T Y K M X W N U D P

    Assert.assertTrue(answer.equals(SHORT_ALPHA));
  }

  @Test
  public void decrypt_LongAlpha () {
    String answer = cipher.encrypt("zxdcwgfvktiposmluynjqhebra", UNIQUE_KEY);
    Assert.assertTrue(answer.equals(LONG_ALPHA));
  }
}
