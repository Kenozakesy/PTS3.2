package Business;

import java.util.ArrayList;
import java.util.List;

public class SingletonGame {
    private List<Lobby> lobbies;

    private static SingletonGame gameDing = null;

    private SingletonGame() {
        lobbies = new ArrayList<>();
    }
    public static SingletonGame getInstance() {
        if(gameDing == null) {
            gameDing = new SingletonGame();
        }
        return gameDing;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }
}
