
import Business.Lobby;
import Business.MainServerManager;
import Business.exceptions.AlreadyHostingException;
import Business.staticClasses.StaticPlayer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import networking.ServerClientEvents;
import networking.ServerHostEvents;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class LobbyController implements Initializable, Observer {

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

    private MainServerManager mainServerManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainServerManager = MainServerManager.getInstance(this);
    }

    @FXML
    public void refresh() {
        mainServerManager.refreshLobbies();
    }

    @FXML
    public void btnSend() {
        String text = tfSend.getText();
        tfSend.setText("");
        mainServerManager.sendMessage("<C>" + text + "</C>");
    }

    @FXML
    public void btnCreateGame() {
        createLobbyScreen(true, new Lobby(StaticPlayer.getPlayer().getName(), "localhost"));
    }

    @FXML
    public void LisViewClick(MouseEvent click) {
        if (click.getClickCount() == 2) {
            //Use ListView's getSelected Item
            Lobby lobby = lvLobby.getSelectionModel().getSelectedItem();
            createLobbyScreen(false, lobby);
        }
    }

    private void refreshLobbyListView() {
        Platform.runLater(() -> {
            lvLobby.getItems().clear();

            for (Lobby lobby : mainServerManager.getLobbies()) {
                lvLobby.getItems().add(lobby);
            }
        });
    }

    private void createLobbyScreen(boolean isHost, Lobby lobby) {
        Stage stage = (Stage) btnCreateGame.getScene().getWindow();
        stage.hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateGame.fxml"));
        Parent root1 = null;

        try {
            CreateGameController CreateGame = new CreateGameController();
            CreateGame.setLobby(lobby);

            if (isHost) {
                try {
                    lobby.startHosting(CreateGame);
                } catch (AlreadyHostingException e) {
                    e.printStackTrace();
                }
            } else {
                String IP = lvLobby.getSelectionModel().getSelectedItem().getIP();
                try {
                    lobby.joinLobby(IP, 1337, CreateGame);
                } catch (AlreadyHostingException e) {
                    e.printStackTrace();
                }
            }

            CreateGame.setPreviousStage(stage);

            fxmlLoader.setController(CreateGame);
            root1 = fxmlLoader.load();

            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            stage2.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String chatMessage = getChatMessage((String) arg);
            if (chatMessage != null) {
                Platform.runLater(() -> lvChat.getItems().add(chatMessage));
                return;
            }
        }

        this.refreshLobbyListView();
    }

    private String getChatMessage(String message) {
        return StringUtils.substringBetween(message, "<C>", "</C>");
    }
}
