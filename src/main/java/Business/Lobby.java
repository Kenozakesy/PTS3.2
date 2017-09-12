package Business;

import Business.Enums.Status;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class Lobby {

    //Relations
    private ArrayList<CzarCard> CzarDeck;
    private ArrayList<PlayCard> Deck;
    private ArrayList<Cardset> cardsets;

    private ArrayList<Player> Spectators;
    private ArrayList<Player> Players;

    //Fields
    private String lobbyID;
    private int maxplayers;
    private int maxspectators;
    private int scorelimit;
    private int blankcards;
    private Time timelimit;
    private String password;
    private Status status;

    //Properties
    public String getLobbyID() { return lobbyID;}

    public int getMaxplayers() {return maxplayers;}
    public void setMaxplayers(int maxplayers) {this.maxplayers = maxplayers;}

    public int getMaxspectators() {return maxspectators;}
    public void setMaxspectators(int maxspectators) {this.maxspectators = maxspectators;}

    public int getScorelimit() {return scorelimit;}
    public void setScorelimit(int scorelimit) {this.scorelimit = scorelimit;}

    public int getBlankcards() {return blankcards;}
    public void setBlankcards(int blankcards) {this.blankcards = blankcards;}

    public Time getTimelimit() {return timelimit;}
    public void setTimelimit(Time timelimit) {this.timelimit = timelimit;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}


    //Constructor
    public Lobby(String LobbyID)
    {
        this.lobbyID = LobbyID;
    }

    //Operators
    public void StartGame()
    {
        //starts the game
    }

    public void addplayer(Player pleb)
    {
        Players.add(pleb);
    }

    public void removeplayer(Player pleb)
    {
        Players.remove(pleb);
    }

    public void addSpectator(Player spectator)
    {
        Players.add(spectator);
    }

    public void removeSpectator(Player spectator)
    {
        Players.remove(spectator);
    }







}
