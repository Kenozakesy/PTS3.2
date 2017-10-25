package Business;

import Business.Enums.Role;
import Business.exceptions.AlreadyHostingException;
import Business.exceptions.NotClientException;
import Business.exceptions.NotHostException;
import Business.staticClasses.StaticPlayer;
import DAL.SqlCardset;
import networking.ServerClient;
import networking.ServerClientEvents;
import networking.ServerHost;
import networking.ServerHostEvents;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Lobby {

    //Relations
    private ArrayList<Cardset> cardSetsNotUsing = new ArrayList<>();
    private ArrayList<Cardset> cardSetsUsing = null;

    private ServerHost lobbyHost;
    private ServerClient lobbyClient;

    private Game game;

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
    public void setPassword(String pw) { password = pw; }

    public Game getGame() { return game; }

    public ArrayList<Cardset> getCardSetsNotUsing() {
        return cardSetsNotUsing;
    }
    public void setCardSetsNotUsing(ArrayList<Cardset> n) {
        this.cardSetsNotUsing = n;
    }

    public ArrayList<Cardset> getCardSetsUsing() {
        return cardSetsUsing;
    }
    public void setCardSetsUsing(ArrayList<Cardset> n) {
        this.cardSetsUsing = n;
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

    //Constructor
    public Lobby(String lobbyId, String ip) {
        this.lobbyID = lobbyId;
        this.ip = ip;

        cardSetsNotUsing = new ArrayList<>();
        cardSetsUsing = new ArrayList<>();
        getCardSetsFromDatabase();
    }

    public void getCardSetsFromDatabase() {
        SqlCardset sqlCardset = new SqlCardset();
        this.cardSetsNotUsing = sqlCardset.getAllCardsets();
    }

    // Voor op de UI
    public void setToNotUsingSets(Cardset set) {
        this.cardSetsNotUsing.add(set);
        this.cardSetsUsing.remove(set);
    }

    // Voor op de UI
    public void setToUsingSets(Cardset set) {
        this.cardSetsUsing.add(set);
        this.cardSetsNotUsing.remove(set);
    }

    // Als er nog geen host is, dan wordt deze gemaakt, anders exceptie.
    public void startHosting(ServerHostEvents eventHandler) throws AlreadyHostingException, IOException {
        if (lobbyHost != null) {
            throw new AlreadyHostingException();
        }

        lobbyHost = new ServerHost(maxPlayers, eventHandler);
        lobbyHost.start();
        MainServerManager.getInstance().sendMessage("<L>" + StaticPlayer.getPlayer().getName() + ";" + InetAddress.getLocalHost().getHostAddress() + "</L>");
    }

    public void joinLobby(String ip, int port, ServerClientEvents eventHandler) throws AlreadyHostingException {
        // Als je al een lobby host, kun je niet client zijn bij een andere lobby
        if (lobbyHost != null) {
            throw new AlreadyHostingException();
        }

        lobbyClient = new ServerClient(ip, port, eventHandler, StaticPlayer.getPlayer());
        lobbyClient.start();
    }

    public void messageClients(String message) throws NotHostException {
        if (lobbyHost == null) {
            throw new NotHostException();
        }

        lobbyHost.messageAll(message);
    }

    public void messageServer(String message) throws NotClientException {
        if (lobbyClient == null) {
            throw new NotClientException();
        }

        lobbyClient.sendMessage(message);
    }

    public void messageClient(Player player, String message) throws NotHostException {
        if(lobbyHost == null) {
            throw new NotHostException();
        }

        //smt like this, dit doet Peer
        //players.get(player);
    }

    public void messageMainServer(String message) {
        MainServerManager.getInstance().sendMessage(message);
    }

    public void startGame() {
        game = new Game(this);

        Random ran = new Random();
        int pos = ran.nextInt(players.size() + 1);
        int tel = 1;
        for (Player p: players.values()) {
            if(tel == pos)
            {
                p.setRole(Role.Czar);
            }
            else
            {
                p.setRole(Role.Pleb);
            }
            tel++;
        }
    }

    @Override
    public String toString() {
        return lobbyID + "'s lobby, IP address: " + ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}

        Lobby lobby = (Lobby) o;

        if (maxPlayers != lobby.maxPlayers) {
            return false;
        }
        if (maxSpectators != lobby.maxSpectators) {
            return false;
        }
        if (scoreLimit != lobby.scoreLimit) {
            return false;
        }
        if (blankCards != lobby.blankCards) {
            return false;
        }
        if (ip != null ? !ip.equals(lobby.ip) : lobby.ip != null) {
            return false;
        }
        if (lobbyID != null ? !lobbyID.equals(lobby.lobbyID) : lobby.lobbyID != null) {
            return false;
        }
        return password != null ? password.equals(lobby.password) : lobby.password == null;
    }

    // Hoeven we niet naar te kijken, Peer: "Het werkt".
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
