package LogicTests;

import Business.*;
import Business.Enums.Role;
import DAL.SqlCard;
import DAL.SqlCardset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Gebruiker on 25-10-2017.
 */
public class GameTest {

    Lobby lobby;

    @Before
    public void setUp() throws Exception {
        lobby = new Lobby("1", "127.0.0.1");
        lobby.getCardSetsFromDatabase();

        ArrayList<Cardset> cardsets = new ArrayList<>();
        for (Cardset cs: lobby.getCardSetsNotUsing()) {
            cardsets.add(cs);
        }
        for (Cardset cs: cardsets)
        {
            lobby.setToUsingSets(cs);
        }

        lobby.getPlayers().put(new Socket(), new Player("test1"));
        lobby.startGame();

        lobby.getGame().pickBlackCard();
    }

    @Test
    public void cardsShowtest()
    {
        int totalCards = lobby.getGame().getCzarCards().size() + lobby.getGame().getPlayCards().size();
        Assert.assertEquals(23, totalCards + 9);
        
        for (Map.Entry<Socket, Player> entry : lobby.getPlayers().entrySet())
        {
            if(entry.getValue().getName().equals("test1"))
            {
                Assert.assertEquals(entry.getValue().getRole(), Role.Czar);
            }
        }
    }


}
