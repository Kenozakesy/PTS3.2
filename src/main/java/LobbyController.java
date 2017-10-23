
import Business.Lobby;
import Business.MainServerManager;
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

import java.io.IOException;
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
        mainServerManager.sendMessage(text);
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

    private void refreshLobbyListView() {
        Platform.runLater(() -> {
            lvLobby.getItems().clear();

            for (Lobby lobby : mainServerManager.getLobbies()) {
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
                startController.setHost();
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

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            Platform.runLater(() -> lvChat.getItems().add(arg));
            return;
        }

        this.refreshLobbyListView();
    }
}
