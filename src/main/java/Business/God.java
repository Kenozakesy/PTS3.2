package Business;

import Business.Enums.Status;

import java.util.ArrayList;

// FACADE
public class God {
    private ArrayList<Lobby> lobbies;

    public God() {
        lobbies = new ArrayList<>();
    }

    // Creeert een lobby;
    public void createLobby(int id, int ip, int maxPlayers, int maxSpectators, int scoreLimit, int blankCards, String timeLimit, String password, String status) {
        String lobbyId = String.valueOf(id);
        String hostIp = String.valueOf(ip);
        Lobby lobby = new Lobby(lobbyId, hostIp);
    }

    // Start de game, met de lobby van het meegegeven id;
    public void startGame(int id) {
        String lobbyId = String.valueOf(id);
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID() == lobbyId) {
                lobby.startGame();
            }
        }
    }

    // Returnt alle bestaande lobbies, die niet gestart zijn;
    public ArrayList<Lobby> getLobbiesNotStarted() {
        ArrayList<Lobby> lobbiesNotStarted = new ArrayList<Lobby>();
        for(Lobby lobby : lobbies) {
            if(lobby.getStatus() == Status.Not_started) {
                lobbiesNotStarted.add(lobby);
            }
        }
        return lobbiesNotStarted;
    }

    public ArrayList<Player> getPlayersFromLobby() {

    }

    // Deze method is om een lobby uit de lijst met lobbies te vissen met een bepaald Id;
    private Lobby getLobbyFromId(int id) {
        String lobbyId = String.valueOf(id);
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID() == lobbyId) {
                return lobby;
            }
        }
    }

}
