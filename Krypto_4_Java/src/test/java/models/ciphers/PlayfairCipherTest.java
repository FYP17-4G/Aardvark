package models.ciphers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayfairCipherTest   {
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

  private final String PF_KEY           = "SECRET";

  @Before
  public void init() { cipher = new PlayfairCipher(); }

  @Test
  public void checkKey_LongAlpha() {
    Assert.assertTrue(cipher.checkKey(LONG_ALPHA));
  }

  @Test
  public void checkKey_ShortAlpha() {
    Assert.assertTrue(cipher.checkKey(SHORT_ALPHA));
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

  /*
  * PLAYFAIR RULES:
  * 1. Same Row -> Take character directly below, mod if necessary.
  * 2. Same Col -> Take character directly left,  mod if necessary.
  * 3. Else     -> Anti-Clockwise Rectangle.
  *
  * S E C R T
  * A B D F G
  * H I K L M
  * N O P Q U
  * V W X Y Z
  *
  * DE FX
  * CB DY
  *
  * AB CD EF GH IX JX KL MN OP QR ST UV WX YZ
  * HI RF BR AM WK WK PQ HU WX UT AG NZ EC RT
   */

  @Test
  public void encrypt_ShortAlpha() {
    String answer = cipher.encrypt(SHORT_ALPHA_ALT, PF_KEY);
    Assert.assertEquals(answer, "cbdy");
  }

  @Test
  public void encrypt_LongAlpha() {
    String answer = cipher.encrypt(LONG_ALPHA, PF_KEY);
  }

}
