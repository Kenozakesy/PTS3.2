package business;

import business.exceptions.AlreadyHostingException;
import business.exceptions.NotClientException;
import business.exceptions.NotHostException;
import business.statics.StaticPlayer;
import dal.SqlCardSet;
import networking.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class Lobby {

    //Relations
    private List<CardSet> cardSetsNotUsing = new ArrayList<>();
    private List<CardSet> cardSetsUsing = null;

    private ServerHost lobbyHost;
    private ServerClient lobbyClient;

    private Game game;

    //Fields
    private int playerCzarCheck = 0;
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
    private List<Player> playerList = new ArrayList<>();

    //Properties
    public boolean isHost() {
        return lobbyHost != null;
    }

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

    public void setPassword(String pw) {
        password = pw;
    }

    public Game getGame() {
        return game;
    }

    public List<CardSet> getCardSetsNotUsing() {
        return cardSetsNotUsing;
    }

    public void setCardSetsNotUsing(List<CardSet> n) {
        this.cardSetsNotUsing = n;
    }

    public List<CardSet> getCardSetsUsing() {
        return cardSetsUsing;
    }

    public void setCardSetsUsing(List<CardSet> n) {
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
        SqlCardSet sqlCardSet = new SqlCardSet();
        this.cardSetsNotUsing = sqlCardSet.getAllCardSets();
    }

    // Voor op de UI
    public void setToNotUsingSets(CardSet set) {
        this.cardSetsNotUsing.add(set);
        this.cardSetsUsing.remove(set);
    }

    // Voor op de UI
    public void setToUsingSets(CardSet set) {
        this.cardSetsUsing.add(set);
        this.cardSetsNotUsing.remove(set);
    }

    public void setHostEventHandler(ServerHostEvents eventHandler) throws NotHostException {
        if (lobbyHost == null) {
            throw new NotHostException();
        }

        lobbyHost.setEventHandler(eventHandler);
    }

    public void setClientEventHandler(ServerClientEvents eventHandler) throws NotClientException {
        if (lobbyClient == null) {
            throw new NotClientException();
        }

        lobbyClient.setEventHandler(eventHandler);
    }

    // Als er nog geen host is, dan wordt deze gemaakt, anders exceptie.
    public void startHosting(ServerHostEvents eventHandler) throws AlreadyHostingException, IOException {
        if (lobbyHost != null) {
            throw new AlreadyHostingException();
        }

        lobbyHost = new ServerHost(maxPlayers, eventHandler);
        lobbyHost.start();

        MainServerManager.getInstance().sendMessage(MessageType.LOBBY_DATA, StaticPlayer.getPlayer().getName() + ";" + InetAddress.getLocalHost().getHostAddress());
    }

    public void joinLobby(String ip, int port, ServerClientEvents eventHandler) throws AlreadyHostingException {
        // Als je al een lobby host, kun je niet client zijn bij een andere lobby
        if (lobbyHost != null) {
            throw new AlreadyHostingException();
        }

        lobbyClient = new ServerClient(ip, port, eventHandler, StaticPlayer.getPlayer());
        lobbyClient.start();
    }

    public void messageClients(MessageType messageType, String message) throws NotHostException {
        if (lobbyHost == null) {
            throw new NotHostException();
        }

        lobbyHost.messageAll(messageType, message);
    }

    public void messageServer(MessageType messageType, String message) throws NotClientException {
        if (lobbyClient == null) {
            throw new NotClientException();
        }

        lobbyClient.sendMessage(messageType, message);
    }

    public void messageClient(Socket socket, MessageType messageType, String message) throws NotHostException {
        if (lobbyHost == null) {
            throw new NotHostException();
        }

        lobbyHost.messageClient(socket, messageType, message);
    }

    public void messageClient(Player player, MessageType messageType, String message) throws NotHostException {
        if (lobbyHost == null) {
            throw new NotHostException();
        }

        for (Map.Entry<Socket, Player> entry : players.entrySet()) {
            if (entry.getValue() == player) {
                this.messageClient(entry.getKey(), messageType, message);
            }
        }
    }

    public void messageMainServer(MessageType messageType, String message) {
        MainServerManager.getInstance().sendMessage(messageType, message);
    }

    public void startGame() {
        game = new Game(this);

        //voegd spelers toe aan lijst boor orde
        for (Map.Entry<Socket, Player> entry : players.entrySet()) {
            playerList.add(entry.getValue());
        }

        //verdeeld rollen in het spel
        setRoles();

    }

    public void setRoles() {
            if (this.isHost()) {
            for (Player P : playerList) {
                if (playerCzarCheck == playerList.indexOf(P)) {
                    P.setRole(Role.CZAR);
                } else {
                    P.setRole(Role.PLEB);
                }
            }
            playerCzarCheck++;

            if (playerCzarCheck >= maxPlayers) {
                playerCzarCheck = 0;
            }

            //hier nog het versturen naar de andere client
            StringBuilder builder = new StringBuilder();
            for (Player P : playerList) {
                if(StaticPlayer.getPlayer() != P) {
                    builder.append(P.getName() + "," + P.getRole().ordinal() + ",");
                }
            }

            try {
                    messageClients(MessageType.GET_ROLE, builder.toString());
            } catch (NotHostException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (isHost()) {
           this.close();
        } else{
            lobbyClient.close();
        }
    }

    private void close()  {
        try {
            this.messageMainServer(MessageType.LOBBY_QUIT, "!");
            lobbyHost.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return lobbyID + "'s lobby, IP address: " + ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

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
