package Business.staticClasses;


import Business.Enums.*;
import Business.Player;

/**
 * Created by Gebruiker on 19-9-2017.
 */
public class StaticPlayer {

    private static String name = "Test player";
    private static Integer points;
    private static Role role = Role.Nothing;

    public static String getName() {return name;}
    public static void setName(String name) {StaticPlayer.name = name;}

    public static Integer getPoints() {return points;}
    public static void setPoints(Integer points) {StaticPlayer.points = points;}

    public static Role getRole() {return role;}
    public static void setRole(Role role) {StaticPlayer.role = role;}

    public static Player getPlayer()
    {
        Player player = new Player(name);
        return player;
    }


}
