package Business.staticClasses;

import Business.Cardset;
import Business.CzarCard;
import Business.Enums.Status;
import Business.PlayCard;
import Business.Player;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Gebruiker on 19-9-2017.
 */
public class StaticLobby {

    private static ArrayList<CzarCard> CzarDeck;
    private static ArrayList<PlayCard> Deck;
    private static ArrayList<Cardset> cardsets;

    private static ArrayList<Player> Spectators;
    private static ArrayList<Player> Players;

    //Fields
    private static String lobbyID;
    private static int maxplayers = 3;
    private static int maxspectators = 3;
    private static int scorelimit = 3;
    private static int blankcards = 0;
    private static String timelimit = "no limit";
    private static String password;
    private static Status status;

    //Properties
    public static String getLobbyID() { return lobbyID;}

    public static int getMaxplayers() {return maxplayers;}
    public static void setMaxplayers(int Maxplayers) {maxplayers = Maxplayers;}

    public static int getMaxspectators() {return maxspectators;}
    public static void setMaxspectators(int Maxspectators) {maxspectators = Maxspectators;}

    public static int getScorelimit() {return scorelimit;}
    public static void setScorelimit(int Scorelimit) {scorelimit = Scorelimit;}

    public static int getBlankcards() {return blankcards;}
    public static void setBlankcards(int Blankcards) {blankcards = Blankcards;}

    public static String getTimelimit() {return timelimit;}
    public static void setTimelimit(String Timelimit) {timelimit = Timelimit;}

    public static String getPassword() {return password;}
    public static void setPassword(String Password) {password = Password;}

    public static Status getStatus() {return status;}
    public static void setStatus(Status Status) {status = Status;}

    //Operators
    public static void StartGame()
    {
        //starts the game
    }

    public static void addplayer(Player pleb)
    {
        Players.add(pleb);
    }

    public static void removeplayer(Player pleb)
    {
        Players.remove(pleb);
    }

    public static void addSpectator(Player spectator)
    {
        Players.add(spectator);
    }

    public static void removeSpectator(Player spectator)
    {
        Players.remove(spectator);
    }



}
