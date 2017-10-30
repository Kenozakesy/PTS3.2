package Business;

import Business.staticClasses.StaticPlayer;
import networking.ServerClient;
import networking.ServerClientEvents;
import org.apache.commons.lang3.StringUtils;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

// Singleton
public class MainServerManager extends Observable implements ServerClientEvents {
    private static MainServerManager instance = null;

    private List<Lobby> lobbies;
    private ServerClient client;

    public synchronized List<Lobby> getLobbies() {
        return lobbies;
    }

    private MainServerManager(Observer observer) {
        lobbies = new ArrayList<>();
        if (observer != null) this.addObserver(observer);

        client = new ServerClient("145.93.128.208", 1336, this, StaticPlayer.getPlayer());
        client.start();
    }

    // Waarom is lobbycontroller observer, en niet lobby?
    public static MainServerManager getInstance(Observer observer) {
        if (instance == null) {
            instance = new MainServerManager(observer);
        }
        return instance;
    }

    // het is dus gewoon raar.
    public static MainServerManager getInstance() {
        if (instance == null) {
            instance = new MainServerManager(null);
        }
        return instance;
    }

    // Stuurt een message naar de mainserver
    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    public void addLobby(Lobby lobby) {
        this.getLobbies().add(lobby);
        this.setChanged();
        this.notifyObservers(lobby);
    }

    public void removeLobby(Lobby lobby) {
        this.getLobbies().remove(lobby);
        this.setChanged();
        this.notifyObservers(lobby);
    }

    @Override
    public void onHostMessage(String message) {
        String lobbyString = StringUtils.substringBetween(message, "<L>", "</L>");
        if (lobbyString != null) {
            String[] data = lobbyString.split(";");
            this.addLobby(new Lobby(data[0], data[1]));
        }

        String lobbyQuitString = StringUtils.substringBetween(message, "<LQ>", "</LQ>");
        if (lobbyQuitString != null) {
            for (Lobby lobby : this.getLobbies()) {
                if (lobby.getIP().equals(lobbyQuitString)) {
                    this.removeLobby(lobby);
                }
            }
        }

        String chatMessage = StringUtils.substringBetween(message, "<C>", "</C>");
        if (chatMessage != null) {
            this.setChanged();
            this.notifyObservers(message);
        }
    }

    public Lobby getLobbyByIP(String ip) {
        for(Lobby lobby : this.getLobbies()) {
            if(lobby.getIP().equals(ip)) {
                return lobby;
            }
        }

        return null;
    }

    @Override
    public void onJoin(SocketAddress address) {
        client.sendMessage("<D>" + StaticPlayer.getPlayer().getName() + "</D>");
        this.refreshLobbies();
    }

    @Override
    public void onServerClose() {
        //TODO als de server dichtgaat? oid Peer weet dit wel
    }

    public void refreshLobbies() {
        client.sendMessage("<LR>!</LR>");
    }
}
