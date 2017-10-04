package DAL;

import Business.Cards;
import Business.Cardset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by user on 3-10-2017.
 */
public class SqlCardsetTest {

    SqlCardset sqlCardset;

    @Before
    public void setUp() throws Exception {
        sqlCardset = new SqlCardset();
    }

    @Test
    public void getAllCardsets() throws Exception {
        ArrayList<Cardset> cardsets = sqlCardset.getAllCardsets();
        Assert.assertEquals(2, cardsets.size());
    }

    @Test
    public void getAllCardsFromCardSet() throws Exception {
        ArrayList<Cards> cards = sqlCardset.getAllCardsFromCardSet(1);
        Assert.assertEquals(3, cards.size());
    }

    @Test
    public void getCardsetById() throws Exception {

    }

}