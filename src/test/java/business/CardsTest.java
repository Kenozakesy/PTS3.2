package business;

import dal.SqlCard;
import dal.SqlCardSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.Socket;
import java.util.List;

/**
 * Created by Jordi on 10-10-2017.
 */

public class CardsTest {

    private SqlCard sqlCard;
    private SqlCardSet sqlCardSet;
    List<Cards> cards;
    List<CardSet> cardSets;

    @Before
    public void setUp() throws Exception {
        sqlCard = new SqlCard();
        sqlCardSet = new SqlCardSet();
    }

    @Test
    public void cardDistributeTest()
    {
        cardSets = sqlCardSet.getAllCardSets();

        Player playerjan = new Player("TestJan");
        Player playerkees = new Player("TestKees");
        Player playergroente = new Player("TestAardappel");
        Lobby lobby = new Lobby("Test","Placeholder");
        lobby.getPlayers().put(new Socket(), playerjan);
        lobby.getPlayers().put(new Socket(), playerkees);
        lobby.getPlayers().put(new Socket(), playergroente);

        lobby.getCardSetsUsing().addAll(cardSets);
        lobby.startGame();

        System.out.println("Hand size: " + playergroente.getCardsInHand().size());
        Assert.assertEquals(8, playerjan.getCardsInHand().size());
    }


}
