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

    private static StaticPlayer player = new StaticPlayer();
    private String name;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    private StaticPlayer()
    {

    }

    public static StaticPlayer getinstance()
    {
        if(player == null)
        {
            player = new StaticPlayer();
        }
        return player;
    }

    public Player getPlayer(Lobby lobby)
    {
        Player player = null;
        for (Player P: lobby.getPlayers().values()) {
            if(P.getName() == name)
            {
                player = P;
            }
        }
        return player;
    }




}
