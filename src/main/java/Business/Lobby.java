package Business;

import Business.Enums.Status;
import networking.GameClient;
import networking.GameHost;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Gebruiker on 12-9-2017.
 */
public class Lobby {

    //Relations
    private ArrayList<CzarCard> czarDeck;
    private ArrayList<PlayCard> deck;
    private ArrayList<Cardset> cardsets;

    private HashSet<GameClient> clients;
    private GameHost host;

    private ArrayList<Player> spectators;
    private ArrayList<Player> players;

    //Fields
    private String IP;
    private String lobbyID;
    private int maxPlayers;
    private int maxSpectators;
    private int scoreLimit;
    private int blankCards;
    private String timeLimit;
    private String password;
    private Status status;

    //Properties
    public String getLobbyID() { return lobbyID;}

    public int getMaxPlayers() {return maxPlayers;}
    //public void setMaxPlayers(int maxplayers) {this.maxPlayers = maxplayers;}

    public int getMaxSpectators() {return maxSpectators;}
    //public void setMaxSpectators(int maxspectators) {this.maxSpectators = maxspectators;}

    public int getScoreLimit() {return scoreLimit;}
    //public void setScoreLimit(int scoreLimit) {this.scoreLimit = scoreLimit;}

    public int getBlankCards() {return blankCards;}
    //public void setBlankCards(int blankCards) {this.blankCards = blankCards;}

    public String getTimeLimit() {return timeLimit;}
    //public void setTimeLimit(String timeLimit) {this.timeLimit = timeLimit;}

    public String getPassword() {return password;}
    //public void setPassword(String password) {this.password = password;}

    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}

    public List<Player> getPlayers(){return players;}

    public String getIP() {
        return IP;
    }

    //Constructor
    public Lobby(String LobbyID, String IP)
    {
        this.lobbyID = LobbyID;
        this.IP = IP;

        this.maxPlayers = 5;
        this.maxSpectators = 5;
        this.scoreLimit = 5;
        this.blankCards = 0;
        this.timeLimit = "No limit";
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

    @Override
    public String toString() {
        return lobbyID + "'s lobby, IP address: " + IP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lobby lobby = (Lobby) o;

        if (maxPlayers != lobby.maxPlayers) return false;
        if (maxSpectators != lobby.maxSpectators) return false;
        if (scoreLimit != lobby.scoreLimit) return false;
        if (blankCards != lobby.blankCards) return false;
        if (czarDeck != null ? !czarDeck.equals(lobby.czarDeck) : lobby.czarDeck != null) return false;
        if (deck != null ? !deck.equals(lobby.deck) : lobby.deck != null) return false;
        if (cardsets != null ? !cardsets.equals(lobby.cardsets) : lobby.cardsets != null) return false;
        if (clients != null ? !clients.equals(lobby.clients) : lobby.clients != null) return false;
        if (host != null ? !host.equals(lobby.host) : lobby.host != null) return false;
        if (spectators != null ? !spectators.equals(lobby.spectators) : lobby.spectators != null) return false;
        if (players != null ? !players.equals(lobby.players) : lobby.players != null) return false;
        if (IP != null ? !IP.equals(lobby.IP) : lobby.IP != null) return false;
        if (lobbyID != null ? !lobbyID.equals(lobby.lobbyID) : lobby.lobbyID != null) return false;
        if (timeLimit != null ? !timeLimit.equals(lobby.timeLimit) : lobby.timeLimit != null) return false;
        if (password != null ? !password.equals(lobby.password) : lobby.password != null) return false;
        return status == lobby.status;
    }

    @Override
    public int hashCode() {
        int result = czarDeck != null ? czarDeck.hashCode() : 0;
        result = 31 * result + (deck != null ? deck.hashCode() : 0);
        result = 31 * result + (cardsets != null ? cardsets.hashCode() : 0);
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (spectators != null ? spectators.hashCode() : 0);
        result = 31 * result + (players != null ? players.hashCode() : 0);
        result = 31 * result + (IP != null ? IP.hashCode() : 0);
        result = 31 * result + (lobbyID != null ? lobbyID.hashCode() : 0);
        result = 31 * result + maxPlayers;
        result = 31 * result + maxSpectators;
        result = 31 * result + scoreLimit;
        result = 31 * result + blankCards;
        result = 31 * result + (timeLimit != null ? timeLimit.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
