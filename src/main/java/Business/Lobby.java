package Business;

import Business.Enums.Status;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class Lobby {

    //Relations
    private ArrayList<CzarCard> czarDeck;
    private ArrayList<PlayCard> deck;
    private ArrayList<Cardset> cardsets;

    private ArrayList<Player> spectators;
    private ArrayList<Player> players;

    //Fields
    private String lobbyID;
    private int maxPlayers;
    private int maxSpectators;
    private int scoreLimit;
    private int blankCards;
    private Time timeLimit;
    private String password;
    private Status status;

    //Properties
    public String getLobbyID() { return lobbyID;}

    public int getMaxPlayers() {return maxPlayers;}
    public void setMaxPlayers(int maxplayers) {this.maxPlayers = maxplayers;}

    public int getMaxSpectators() {return maxSpectators;}
    public void setMaxSpectators(int maxspectators) {this.maxSpectators = maxspectators;}

    public int getScoreLimit() {return scoreLimit;}
    public void setScoreLimit(int scoreLimit) {this.scoreLimit = scoreLimit;}

    public int getBlankCards() {return blankCards;}
    public void setBlankCards(int blankCards) {this.blankCards = blankCards;}

    public Time getTimeLimit() {return timeLimit;}
    public void setTimeLimit(Time timeLimit) {this.timeLimit = timeLimit;}

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
        players.add(pleb);
    }

    public void removeplayer(Player pleb)
    {
        players.remove(pleb);
    }

    public void addSpectator(Player spectator)
    {
        players.add(spectator);
    }

    public void removeSpectator(Player spectator)
    {
        players.remove(spectator);
    }







}
