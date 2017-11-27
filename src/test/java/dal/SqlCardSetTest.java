package dal;

import business.CardSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by user on 3-10-2017.
 */
public class SqlCardSetTest {
    SqlCardSet sqlCardSet;

    @Before
    public void setUp() throws Exception {
        sqlCardSet = new SqlCardSet();
    }

    @Test
    public void getAllCardsets() throws Exception {
        List<CardSet> cardSets = sqlCardSet.getAllCardSets();
        Assert.assertFalse( cardSets.size() < 1);
    }
}