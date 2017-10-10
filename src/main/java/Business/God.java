package Business;

import Business.Enums.Status;
import java.util.ArrayList;
import java.util.Collections;

// FACADE
public class God {

    private static ArrayList<Lobby> lobbies = new ArrayList<>();

    public God()
    {

    }

    // Creeert een lobby zonder eigenschappen (i.e. maxPlayers, status etc.)
    public String createLobby(String id, String ip) {
        Lobby lobby = new Lobby(id, ip);
        lobbies.add(lobby);
        return id;
    }

    // Start de game, met de lobby van het meegegeven id
    public void startGame(String id) {
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID().equals(id)) {
                lobby.startGame();
            }
        }
    }

    // Returnt alle bestaande lobbies, die niet gestart zijn
    public ArrayList<Lobby> getLobbiesNotStarted() {
        ArrayList<Lobby> lobbiesNotStarted = new ArrayList<>();
        for(Lobby lobby : lobbies) {
            if(lobby.getStatus() == Status.Not_started) {
                lobbiesNotStarted.add(lobby);
            }
        }
        return lobbiesNotStarted;
    }

    // Returnt de spelers uit een bepaalde lobby
    public ArrayList<Player> getPlayersFromLobby(String id) {
        Lobby lobby = getLobbyFromId(id);
        ArrayList<Player> players = new ArrayList<>();
        if(lobby != null) {
            players.addAll(lobby.getPlayers());
            return players;
        }
        return null;
    }

    // Deze method is om een lobby uit de lijst met lobbies te vissen met een bepaald Id
    private Lobby getLobbyFromId(String id) {
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID().equals(id)) {
                return lobby;
            }
        }
        return null;
    }

    // Deze method is om een lobby uit de lijst met lobbies te vissen met een bepaald Id
    private boolean RemoveLobby(String id) {
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID() == id) {
                lobbies.remove(lobby);
                return true;
            }
        }
        return false;
    }

}
