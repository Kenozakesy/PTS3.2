package Business.staticClasses;


import Business.Cardset;
import Business.Enums.*;
import Business.Player;
import DAL.SqlCardset;

import java.util.List;

/**
 * Created by Gebruiker on 19-9-2017.
 */
public class StaticPlayer {

    private static Player player = null;

    public static void initializePlayer()
    {
        if (player == null) {
            player = new Player();
            SqlCardset sqlCardset = new SqlCardset();
            player.setCardsetList(sqlCardset.getAllCardsets());
        }

    }

    public static Player getPlayer()
    {
        return player;
    }
}
