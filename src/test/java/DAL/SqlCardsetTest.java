package DAL;

import Business.Cardset;
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
        Assert.assertFalse( cardsets.size() < 1);
    }

}