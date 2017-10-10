package LogicTests;

import business.*;
import DAL.SqlCard;
import DAL.SqlCardset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordi on 10-10-2017.
 */

public class CardsTest {

    private SqlCard sqlCard;
    private SqlCardset sqlCardset;
    List<Cards> cards;
    ArrayList<Cardset> cardsets;

    @Before
    public void setUp() throws Exception {
        sqlCard = new SqlCard();
        sqlCardset = new SqlCardset();
    }

    @Test
    public void cardsShowtest()
    {
        for (Cards card: cards ) {
            System.out.println( card.getText());
        }

    }

    @Test
    public void cardDistributeTest()
    {
        cardsets = sqlCardset.getAllCardsets();

        Player playerjan = new Player("TestJan");
        Player playerkees = new Player("TestKees");
        Player playergroente = new Player("TestAardappel");
        Lobby lobby = new Lobby("Test","Placeholder");
        lobby.getPlayers().put(new Socket(), playerjan);

        Game game = new Game(lobby);
        game.getDecks(cardsets);

        game.endTurn();

        System.out.println("Player hand size= " + playerjan.getCardsInHand().size());
        Assert.assertTrue(playerjan.getCardsInHand().size() == 8);
    }


}
