package business;

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
    Socket socket;

    @Before
    public void setUp() throws Exception {
        socket = new Socket();

        lobby = new Lobby("1", "127.0.0.1");
        lobby.getCardSetsFromDatabase();

        ArrayList<CardSet> cardSets = new ArrayList<>();
        for (CardSet cs: lobby.getCardSetsNotUsing()) {
            cardSets.add(cs);
        }
        for (CardSet cs: cardSets)
        {
            lobby.setToUsingSets(cs);
        }

        lobby.getPlayers().put(socket, new Player("test1"));
        lobby.startGame();

        lobby.getGame().pickBlackCard();
    }

    @Test
    public void startGameTest()
    {
        int totalCards = lobby.getGame().getCzarCards().size() + lobby.getGame().getPlayCards().size();
        Assert.assertFalse(totalCards < 10);

        for (Map.Entry<Socket, Player> entry : lobby.getPlayers().entrySet())
        {
            if(entry.getValue().getName().equals("test1"))
            {
                Assert.assertNotEquals(entry.getValue().getRole(), Role.CZAR);
            }
        }
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void pickPlayCardTest()
    {
        Player pleb = lobby.getPlayers().get(socket);
        lobby.getGame().playerPicksCard(2, pleb);
        lobby.getGame().playerPicksCard(2, pleb);

        int handsize = pleb.getCardsInHand().size();
        Assert.assertEquals(handsize, 6);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void pickCzarCardTest()
    {
        Player pleb = lobby.getPlayers().get(socket);
        lobby.getGame().playerPicksCard(2, pleb);
        lobby.getGame().playerPicksCard(2, pleb);
        lobby.getGame().playerPicksCard(2, pleb);
        lobby.getGame().playerPicksCard(2, pleb);

        int handsize = pleb.getCardsInHand().size();
        Assert.assertEquals(handsize, 4);
        Assert.assertEquals(lobby.getGame().getChosenCards().size(), 1);

        String cardText = lobby.getGame().getChosenCards().get(pleb).getText();
        lobby.getGame().czarPicksCards(cardText);

        pleb = lobby.getPlayers().get(socket);
        Assert.assertEquals(1, pleb.getPoints());
    }

    @Test
    public void GameTest()
    {
        Lobby lobby = new Lobby("1", "127.0.0.1");
        Game game = new Game(lobby);
        CardSet set = new CardSet(1, "name");

        Player player = new Player();
        CzarCard Czar = new CzarCard(1, "1____", set, 1);

        Assert.assertEquals(game.getIsCzarTurn(), false);
        Assert.assertEquals(game.getCurrentCzarCard(), null);
        Assert.assertEquals(game.getAllCards().size(), 0);
        Assert.assertEquals(game.getChosenCards().size(), 0);

        game.setCurrentCzar(Czar);

        Assert.assertEquals(game.getCurrentCzarCard(), Czar);
    }


}
