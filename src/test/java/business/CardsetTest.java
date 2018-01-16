package business;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Gebruiker on 16-1-2018.
 */
public class CardsetTest {

    @Test
    public void constructortest()
    {
        int id = 1;
        String name = "name";

        CardSet set = new CardSet(id, name);

        Assert.assertEquals(set.toString(), name);

        Assert.assertEquals(set.getId(), id);
        set.setId(2);
        Assert.assertEquals(set.getId(), 2);
        Assert.assertEquals(set.getName(), name);
        set.setName("");
        Assert.assertEquals(set.getName(), "");
    }
}
