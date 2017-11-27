package business.statics;


import business.Player;

/**
 * Created by Gebruiker on 19-9-2017.
 */
public class StaticPlayer {

    private static Player player = null;

    private StaticPlayer() {}

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
