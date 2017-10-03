import Business.Lobby;
import Business.staticClasses.StaticPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import networking.GameClient;
import networking.GameClientEvents;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by Gebruiker on 26-9-2017.
 */
public class LobbyController implements Initializable, GameClientEvents {

    //private ArrayList lobbyList

    private GameClient client;

    @FXML
    private Button btnCreateGame;

    @FXML
    private ListView<Lobby> lvLobby;

    @FXML
    private Button btnSend;

    @FXML
    private TextField tfSend;

    @FXML
    private ListView lvChat;

    private HashMap<String, Lobby> lobbies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new GameClient("145.93.133.180", 1336, this, StaticPlayer.getPlayer());
        client.start();

        lobbies = new HashMap<>();
        //get every lobby
    }

    @FXML
    public void refresh() {
        client.sendMessage("<LR>!</LR>");
    }

    @FXML
    public void btnSend() {
        String text = tfSend.getText();
        //lvChat.getItems().add(text);
        client.sendMessage(text);
    }

    @FXML
    public void btnCreateGame() {
        //create a new lobby


        //goes to different view
        //starts the game with current options
        Stage stage = (Stage) btnCreateGame.getScene().getWindow();
        stage.hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateGame.fxml"));
        Parent root1 = null;
        try {

            StartGameController startcontroller = new StartGameController();
            startcontroller.setClient(client);

            startcontroller.setPreviousStage(stage);

            fxmlLoader.setController(startcontroller);
            root1 = (Parent) fxmlLoader.load();

            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            stage2.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHostMessage(String message) {
        String lobbyString = StringUtils.substringBetween(message, "<L>", "</L>");
        if (lobbyString != null) {
            String[] data = lobbyString.split(";");
            lobbies.put(data[1], new Lobby(data[0], data[1]));
            refreshLobbyListView();
            return;
        }

        String lobbyQuitString = StringUtils.substringBetween(message, "<LQ>", "</LQ>");
        if (lobbyQuitString != null) {
            lobbies.remove(lobbyQuitString);
            refreshLobbyListView();
            return;
        }

        Platform.runLater(() -> {
            lvChat.getItems().add(message);
        });
    }

    @Override
    public void onJoin(SocketAddress address) {

    }

    @Override
    public void onServerClose() {

    }

    private void refreshLobbyListView() {
        Platform.runLater(() -> {
            lvLobby.getItems().clear();

            for (Lobby lobby : lobbies.values()) {
                lvLobby.getItems().add(lobby);
            }
        });
    }
}
