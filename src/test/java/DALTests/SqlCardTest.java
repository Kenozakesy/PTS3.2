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
        ArrayList<Cards> cards = sqlCard.getAllCardsFromCardSet(cardSet);
        Assert.assertEquals(20, cards.size());
    }

    @Test
    public void getCardById() throws Exception {
        Cards card = sqlCard.getCardById(3);
        Cards card2 = sqlCard.getCardById(24);
        assertEquals("Powerful thighs.", card.getText());
        assertEquals(null, card2);
    }

}