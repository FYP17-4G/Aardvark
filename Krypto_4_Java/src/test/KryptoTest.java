
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class KryptoTest {

    static Krypto krypto;
    //    private static String displayOriginalString()
    //
    //    private static String displayModifiedString ()
    //
    //    private static String displayModifiedString (int blockSize, int blocksPerLine)

    @BeforeClass
    public static void setUp() {
        krypto = new Krypto();
    }


    @org.junit.Test
    public void displayOriginalStringTest() {
        Assert.assertEquals("originalText", "asdf;lkjasdf");
    }

    @Test
    public void displayModifiedStringTest() {
        Assert.assertEquals("modifiedtext", "functioncall");
    }

    @Test
    public void displayModifiedStringBlocksTest() {
        Assert.assertEquals("modifiedtext", "functioncall");
    }
}

