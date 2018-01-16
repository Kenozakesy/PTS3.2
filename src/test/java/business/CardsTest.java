package business;

import dal.SqlCard;
import dal.SqlCardSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.smartcardio.Card;
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
        Assert.assertEquals(0, playerjan.getCardsInHand().size());
    }

    @Test
    public void playCardConstructor()
    {
        CardSet set = new CardSet(1, "name");
        PlayCard play = new PlayCard(1, "test", set, true);

        Assert.assertEquals(play.getId(), 1);
        Assert.assertEquals(play.getText(), "test");
        Assert.assertEquals(play.getCardset(), set);
    }

    @Test
    public void czarCardConstructor()
    {
        CardSet set = new CardSet(1, "name");
        CzarCard czar = new CzarCard(1, "test", set, 1);

        Assert.assertEquals(czar.getBlankSpaces(), 1);
    }



}
