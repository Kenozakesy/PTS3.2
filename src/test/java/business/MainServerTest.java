package business;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Gebruiker on 16-1-2018.
 */
public class MainServerTest {

    @Test
    public void PlayerTest()
    {
        MainServerManager server = MainServerManager.getInstance();
        Assert.assertEquals(server.getClass(), MainServerManager.class);

        Assert.assertEquals(0, server.getLobbies().size());


    }
}
