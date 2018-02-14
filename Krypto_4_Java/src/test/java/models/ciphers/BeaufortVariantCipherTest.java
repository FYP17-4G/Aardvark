package models.ciphers;

import org.junit.*;

public class BeaufortVariantCipherTest {
  private CipherInterface cipher;
  private final String NUM              = "1234567890";
  private final String SHORT_ALPHA      = "abc";
  private final String SHORT_ALPHA_ALT  = "def";
  private final String LONG_ALPHA       = "abcdefghijklmnopqrstuvwxyz";
  private final String LONG_ALPHA_ALT   = "zyxwvutsrqponmlkjihgfedcba";
  private final String SYMBOLS          = "!@#$%^&*()";
  private final String SPACES           = "          ";
  private final String EMPTY            = "";

  @Before
  public void init() {
    cipher = new BeaufortVariantCipher();
  }

  @Test
  public void checkKey_ShortAlpha() {
    Assert.assertTrue(cipher.checkKey(SHORT_ALPHA));
  }

  @Test
  public void checkKey_LongAlpha() {
    Assert.assertTrue(cipher.checkKey(LONG_ALPHA));
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
    Assert.assertFalse(cipher.checkKey(SHORT_ALPHA +
      NUM + SYMBOLS + SPACES));
  }

  @Test
  public void checkKey_EmptyString () {
    Assert.assertFalse(cipher.checkKey(EMPTY));
  }

  @Test //encrypt short short
  public void encrypt_ShortAlpha_ShortAlpha() {
    String answer = cipher.encrypt(SHORT_ALPHA, SHORT_ALPHA_ALT);
    Assert.assertEquals("xxx", answer);
  }

  @Test //encrypt short long
  public void encrypt_ShortAlpha_LongAlpha() {
    String answer = cipher.encrypt(SHORT_ALPHA, LONG_ALPHA_ALT);
    Assert.assertEquals("bdf", answer);
  }

  @Test //encrypt long short
  public void encrypt_LongAlpha_ShortAlpha() {
    String answer = cipher.encrypt(LONG_ALPHA, SHORT_ALPHA_ALT);
    Assert.assertEquals("xxxaaadddgggjjjmmmpppsssvv", answer);
  }

  @Test //encrypt long long
  public void encrypt_LongAlpha_LongAlpha() {
    String answer = cipher.encrypt(LONG_ALPHA, LONG_ALPHA_ALT);
    Assert.assertEquals("bdfhjlnprtvxzbdfhjlnprtvxz", answer);
  }

  @Test //decrypt short short
  public void decrypt_ShortAlpha_ShortAlpha() {
    String answer = cipher.decrypt("xxx", SHORT_ALPHA_ALT);
    Assert.assertEquals(SHORT_ALPHA, answer);
  }

  @Test //decrypt short long
  public void decrypt_ShortAlpha_LongAlpha() {
    String answer = cipher.decrypt("bdf", LONG_ALPHA_ALT);
    Assert.assertEquals(SHORT_ALPHA, answer);
  }

  @Test //decrypt long short
  public void decrypt_LongAlpha_ShortAlpha() {
    String answer = cipher.decrypt("xxxaaadddgggjjjmmmpppsssvv", SHORT_ALPHA_ALT);
    Assert.assertEquals(LONG_ALPHA, answer);
  }

  @Test //decrypt long long
  public void decrypt_LongAlpha_LongAlpha() {
    String answer = cipher.decrypt("bdfhjlnprtvxzbdfhjlnprtvxz", LONG_ALPHA_ALT);
    Assert.assertEquals(LONG_ALPHA, answer);
  }
}
