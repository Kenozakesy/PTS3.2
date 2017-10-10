package Business;

import Business.Enums.Status;
import java.util.ArrayList;
import java.util.Collections;

// FACADE
public class God {
    private static ArrayList<Lobby> lobbies = new ArrayList<>();

    public God() {
        // Is empty, voor de vorm ofzo
    }

    // Creeert een lobby zonder eigenschappen (i.e. maxPlayers, status etc.)
    public String createLobby(String id, String ip) {
        Lobby lobby = new Lobby(id, ip);
        // Alle andere parameters moeten nog worden toegevoegd aan de gecreeerde lobby, op deze plek.
        lobbies.add(lobby);
        return id;
    }

    // Start de game, met de lobby van het meegegeven id
    public void startGame(int id) {
        String lobbyId = String.valueOf(id);
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID().equals(lobbyId)) {
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
    public ArrayList<Player> getPlayersFromLobby(int lobbyId) {
        Lobby lobby = getLobbyFromId(lobbyId);
        ArrayList<Player> players = new ArrayList<>();
        if(lobby != null) {
            players.addAll(lobby.getPlayers());
            return players;
        }
        return null;
    }

    // Deze method is om een lobby uit de lijst met lobbies te vissen met een bepaald Id
    private Lobby getLobbyFromId(int id) {
        String lobbyId = String.valueOf(id);
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID().equals(lobbyId)) {
                return lobby;
            }
        }
        return null;
    }

}
