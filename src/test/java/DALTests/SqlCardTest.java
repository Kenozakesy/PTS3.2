package DALTests;

import Business.*;
import DAL.SqlCard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

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
    public void getAllPlayCardsFromCardSet() throws Exception {
        Cardset cardSet = new Cardset(0, "Test");
        List<PlayCard> cards = sqlCard.getAllPlayCardsFromCardSet(cardSet);
        Assert.assertFalse(cards.size() < 1);
    }

    @Test
    public void getAllCzarCardsFromCardSet() throws Exception {
        Cardset cardSet = new Cardset(0, "Test");
        List<CzarCard> cards = sqlCard.getAllCzarCardsFromCardSet(cardSet);
        Assert.assertFalse(cards.size() < 1);
    }

    @Test
    public void getCardById() throws Exception {
        Cards card = sqlCard.getCardById(3);

        assertFalse(card.getText() == null);
        assertTrue(card.getId() == 3);
    }

}