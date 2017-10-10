package Business;

import java.util.ArrayList;

// FACADE
public class App {

    private static ArrayList<Lobby> lobbies = new ArrayList<>();

    public App()
    {
        // Usefull comment, anders geeft ie een stomme melding.
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
//    public List<Lobby> getLobbiesNotStarted() {
//        List<Lobby> lobbiesNotStarted = new ArrayList<>();
//        for(Lobby lobby : lobbies) {
//            if(lobby.getStatus() == Status.Not_started) {
//                lobbiesNotStarted.add(lobby);
//            }
//        }
//        return lobbiesNotStarted;
//    }

    // Returnt de spelers uit een bepaalde lobby
//    public List<Player> getPlayersFromLobby(String id) {
//        Lobby lobby = getLobbyFromId(id);
//        List<Player> players = new ArrayList<>();
//        if(lobby != null) {
//            players.addAll(lobby.getPlayers());
//            return players;
//        }
//        return null;
//    }

    // Deze method is om een lobby uit de lijst met lobbies te vissen met een bepaald Id
    public Lobby getLobbyFromId(String id) {
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID().equals(id)) {
                return lobby;
            }
        }
        return null;
    }

    // Deze method is om een lobby uit de lijst met lobbies te vissen met een bepaald Id
    public boolean removeLobby(String id) {
        for(Lobby lobby : lobbies) {
            if(lobby.getLobbyID() == id) {
                lobbies.remove(lobby);
                return true;
            }
        }
        return false;
    }

}
