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

public class MainServerManager extends Observable implements ServerClientEvents {
    private static MainServerManager instance = null;

    private List<Lobby> lobbies;
    private ServerClient client;

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    private MainServerManager(Observer observer) {
        lobbies = new ArrayList<>();
        if (observer != null) this.addObserver(observer);

        client = new ServerClient("145.93.132.154", 1336, this, StaticPlayer.getPlayer());
        client.start();
    }

    public static MainServerManager getInstance(Observer observer) {
        if (instance == null) {
            instance = new MainServerManager(observer);
        }

        return instance;
    }

    public static MainServerManager getInstance() {
        if (instance == null) {
            instance = new MainServerManager(null);
        }

        return instance;
    }

    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    public void addLobby(Lobby lobby) {
        lobbies.add(lobby);
        this.setChanged();
        this.notifyObservers(lobby);
    }

    public void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
        this.setChanged();
        this.notifyObservers(lobby);
    }

    @Override
    public void onHostMessage(String message) {
        String lobbyString = StringUtils.substringBetween(message, "<L>", "</L>");
        if (lobbyString != null) {
            String[] data = lobbyString.split(";");
            this.addLobby(new Lobby(data[0], data[1]));
            return;
        }

        String lobbyQuitString = StringUtils.substringBetween(message, "<LQ>", "</LQ>");
        if (lobbyQuitString != null) {
            for (Lobby lobby : lobbies) {
                if (lobby.getIP().equals(lobbyQuitString)) {
                    this.removeLobby(lobby);
                    return;
                }
            }
        }

        this.setChanged();
        this.notifyObservers(message);
    }

    @Override
    public void onJoin(SocketAddress address) {
        client.sendMessage("<D>" + StaticPlayer.getName() + "</D>");
        this.refreshLobbies();
    }

    @Override
    public void onServerClose() {

    }

    public void refreshLobbies() {
        client.sendMessage("<LR>!</LR>");
    }
}
