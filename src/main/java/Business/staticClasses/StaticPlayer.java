package Business.staticClasses;


import Business.Cardset;
import Business.Enums.*;
import Business.Lobby;
import Business.Player;
import DAL.SqlCardset;

import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Created by Gebruiker on 19-9-2017.
 */
public class StaticPlayer {

    private static Player player = null;

    public static void initializePlayer()
    {
        if (player == null) {
            player = new Player();
        }
    }

    public static Player getPlayer()
    {
        return player;
    }




}
