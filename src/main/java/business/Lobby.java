package business;

import DAL.SqlCardset;
import business.exceptions.AlreadyHostingException;
import business.exceptions.NotClientException;
import business.exceptions.NotHostException;
import business.staticClasses.StaticPlayer;
import networking.ServerClient;
import networking.ServerClientEvents;
import networking.ServerHost;
import networking.ServerHostEvents;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {

    //Relations
    private List<CzarCard> czarDeck;
    private List<PlayCard> deck;
    private ArrayList<Cardset> cardSetsNotUsing = null;
    private ArrayList<Cardset> cardSetsUsing = null;

    private ServerClient mainClient;

    private ServerHost lobbyHost;
    private ServerClient lobbyClient;

    //Fields
    private String ip;
    private String lobbyID;
    private int maxPlayers = 5;
    private int maxSpectators = 5;
    private int scoreLimit = 5;
    private int blankCards = 0;
    private int timeLimit = 0;
    private String password;

    private Map<Socket, Player> spectators = new HashMap<>(maxSpectators);
    private Map<Socket, Player> players = new HashMap<>(maxPlayers);

    //Properties
    public String getLobbyID() {
        return lobbyID;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    public int getMaxSpectators() {
        return maxSpectators;
    }
    public int getScoreLimit() {
        return scoreLimit;
    }
    public int getBlankCards() {
        return blankCards;
    }
    public int getTimeLimit() {
        return timeLimit;
    }
    public String getPassword() {
        return password;
    }
    public Map<Socket, Player> getPlayers() {
        return players;
    }
    public Map<Socket, Player> getSpectators() {
        return spectators;
    }
    public String getIP() {
        return ip;
    }

    public void setMainClient(ServerClient mainClient) {
        this.mainClient = mainClient;
    }

    //Constructor
    public Lobby(String lobbyId, String ip) {
        this.lobbyID = lobbyId;
        this.ip = ip;

        cardSetsNotUsing = new ArrayList<>();
        cardSetsUsing = new ArrayList<>();
    }

    public void getCardSetsDatabase()
    {
        SqlCardset SC = new SqlCardset();
        this.cardSetsNotUsing = SC.getAllCardsets();
    }

    public void startHosting(ServerHostEvents eventHandler) throws AlreadyHostingException, IOException {
        if (lobbyHost != null) throw new AlreadyHostingException();

        lobbyHost = new ServerHost(maxPlayers, eventHandler);
        lobbyHost.start();
    }

    public void joinLobby(String ip, int port, ServerClientEvents eventHandler) throws AlreadyHostingException {
        if (lobbyHost != null) throw new AlreadyHostingException();

        lobbyClient = new ServerClient(ip, port, eventHandler, StaticPlayer.getPlayer());
        lobbyClient.start();
    }

    public void messageClients(String message) throws NotHostException {
        if (lobbyHost == null) throw new NotHostException();

        lobbyHost.messageAll(message);
    }

    public void messageServer(String message) throws NotClientException {
        if (lobbyClient == null) throw new NotClientException();

        lobbyClient.sendMessage(message);
    }

    public void messageMainServer(String message) {
        mainClient.sendMessage(message);
    }

    public void startGame() {

    }

    @Override
    public String toString() {
        return lobbyID + "'s lobby, IP address: " + ip;
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
        if (ip != null ? !ip.equals(lobby.ip) : lobby.ip != null) return false;
        if (lobbyID != null ? !lobbyID.equals(lobby.lobbyID) : lobby.lobbyID != null) return false;
        return password != null ? password.equals(lobby.password) : lobby.password == null;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (lobbyID != null ? lobbyID.hashCode() : 0);
        result = 31 * result + maxPlayers;
        result = 31 * result + maxSpectators;
        result = 31 * result + scoreLimit;
        result = 31 * result + blankCards;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
