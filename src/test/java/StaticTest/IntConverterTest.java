package StaticTest;

import business.statics.IntConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Gebruiker on 16-1-2018.
 */
public class IntConverterTest {

    @Test
    public void ConverterTest()
    {
        byte[] data = new byte[2];
        Assert.assertEquals(IntConverter.intToByteArray(90).getClass(), data.getClass());

        int i = 0;
        byte[] data2 = IntConverter.intToByteArray(i);
        int k = IntConverter.byteArrayToInt(data2);

        Assert.assertEquals(k, i);
    }
}
