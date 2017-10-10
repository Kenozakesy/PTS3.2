
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import networking.ServerClient;
import networking.ServerClientEvents;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LobbyController implements Initializable, ServerClientEvents {

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


    private ServerClient client;

    private HashMap<String, Lobby> lobbies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new ServerClient("145.93.135.78", 1336, this, StaticPlayer.getPlayer());
        client.start();

        lobbies = new HashMap<>();
    }

    @FXML
    public void refresh() {
        client.sendMessage("<LR>!</LR>");
    }

    @FXML
    public void btnSend() {
        String text = tfSend.getText();
        //lvChat.getItems().add(text);
        tfSend.setText("");
        client.sendMessage(text);
    }

    @FXML
    public void btnCreateGame() {
        createLobbyScreen(true);
    }

    @FXML
    public void LisViewClick(MouseEvent click) {
        if (click.getClickCount() == 2) {
            //Use ListView's getSelected Item
            Lobby lobby = lvLobby.getSelectionModel().getSelectedItem();
            createLobbyScreen(false);
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
        client.sendMessage("<D>" + StaticPlayer.getName() + "</D>");
        client.sendMessage("<LR>!</LR>");
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

    private void createLobbyScreen(boolean isHost) {
        Stage stage = (Stage) btnCreateGame.getScene().getWindow();
        stage.hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateGame.fxml"));
        Parent root1 = null;
        try {

            StartGameController startController = new StartGameController();
            if (isHost) {
                startController.setHost(client);
            } else {
                String IP = lvLobby.getSelectionModel().getSelectedItem().getIP();
                startController.setClient(new ServerClient(IP, 1337, startController, StaticPlayer.getPlayer()));
            }

            startController.setPreviousStage(stage);

            fxmlLoader.setController(startController);
            root1 = (Parent) fxmlLoader.load();

            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            stage2.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
