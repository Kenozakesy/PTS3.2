package StaticTest;

import business.statics.StaticPlayer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Gebruiker on 16-1-2018.
 */
public class staticPlayerTest {

    @Test
    public void PlayerTest()
    {
        StaticPlayer.initializePlayer();
        Assert.assertNotNull(StaticPlayer.getPlayer());
    }
}
