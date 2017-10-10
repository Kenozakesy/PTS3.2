package DAL;

import business.Cardset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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
    public void getCardsetById() throws Exception {

    }

}