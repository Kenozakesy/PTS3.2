package LogicTests;

import DAL.SqlCard;
import DAL.SqlCardset;
import Business.*;
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
    public void cardDistributeTest()
    {
        cardsets = sqlCardset.getAllCardsets();

        Player playerjan = new Player("TestJan");
        Player playerkees = new Player("TestKees");
        Player playergroente = new Player("TestAardappel");
        Lobby lobby = new Lobby("Test","Placeholder");
        lobby.getPlayers().put(new Socket(), playerjan);
        lobby.getPlayers().put(new Socket(), playerkees);
        lobby.getPlayers().put(new Socket(), playergroente);

        lobby.getCardSetsUsing().addAll(cardsets);
        lobby.startGame();

        System.out.println("Hand size: " + playergroente.getCardsInHand().size());
        Assert.assertEquals(8, playerjan.getCardsInHand().size());
    }


}
