package DALTests;

import Business.Cards;
import Business.Cardset;
import DAL.SqlCard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by user on 3-10-2017.
 */
public class SqlCardTest {
    private SqlCard sqlCard;

    @Before
    public void setUp() throws Exception {
        sqlCard = new SqlCard();
    }

    @Test
    public void getAllCards() throws Exception {
        ArrayList<Cards> cards = sqlCard.getAllCards();
        Assert.assertEquals(23, cards.size());
    }

    @Test
    public void getAllCardsFromCardSet() throws Exception {
        Cardset cardSet = new Cardset(0, "Test");
        sqlCard.getAllCardsFromCardSet(cardSet);
    }

    @Test
    public void getCardById() throws Exception {

    }

}