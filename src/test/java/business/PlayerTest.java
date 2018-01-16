package business;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gebruiker on 16-1-2018.
 */
public class PlayerTest {

    @Test
    public void PlayerTest()
    {
        Player player = new Player();
        player.setPoints(3);
        Assert.assertEquals(3, player.getPoints());
        player.setName("meer");
        player.setRole(Role.CZAR);
        Assert.assertEquals(player.getRole(), Role.CZAR);

        player.resetPoints();
        Assert.assertEquals(0, player.getPoints());
        player.increasePoints();
        Assert.assertEquals(1, player.getPoints());

        Assert.assertNotNull(player.getCardsInHand());

        String name = "name";
        Player pley = new Player(name);

        Assert.assertEquals(pley.getName(), name);
        PlayCard card = new PlayCard(1, "text", new CardSet(1, "set"), true);
        PlayCard card2 = new PlayCard(2, "text", new CardSet(1, "set"), true);
        pley.addToHand(card);

        Assert.assertEquals(pley.getCardsInHand().size(), 1);

        pley.removeFromHand(card2);

        Assert.assertEquals(pley.getCardsInHand().size(), 1);

        pley.removeFromHand(card);

        Assert.assertEquals(pley.getCardsInHand().size(), 0);


    }
}
