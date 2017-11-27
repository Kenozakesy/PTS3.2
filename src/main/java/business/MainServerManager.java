package business;

import business.statics.StaticPlayer;
import networking.MessageType;
import networking.ServerClient;
import networking.ServerClientEvents;

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

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    private MainServerManager(Observer observer) {
        lobbies = new ArrayList<>();
        if (observer != null) this.addObserver(observer);

        client = new ServerClient("localhost", 1336, this, StaticPlayer.getPlayer());
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
    public void sendMessage(MessageType messageType, String message) {
        client.sendMessage(messageType, message);
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
    public void onHostMessage(MessageType messageType, String message) {
        switch (messageType) {
            case CHAT_MESSAGE:
                this.setChanged();
                this.notifyObservers(message);
                break;

            case LOBBY_DATA:
                String[] data = message.split(";");
                this.addLobby(new Lobby(data[0], data[1]));
                break;

            case LOBBY_QUIT:
                for (Lobby lobby : this.getLobbies()) {
                    if (lobby.getIP().equals(message)) {
                        this.removeLobby(lobby);
                        return;
                    }
                }
                break;

            case PLAYER_DATA:
                break;

            case LOBBY_LIST_SYNC_REQUEST:
                break;

            case START_GAME:
                break;
        }
    }

    @Override
    public void onJoin(SocketAddress address) {
        client.sendMessage(MessageType.PLAYER_DATA, StaticPlayer.getPlayer().getName());
        this.refreshLobbies();
    }

    @Override
    public void onServerClose() {
       // Not used for now
    }

    public void refreshLobbies() {
        client.sendMessage(MessageType.LOBBY_LIST_SYNC_REQUEST, "!");
    }
}
