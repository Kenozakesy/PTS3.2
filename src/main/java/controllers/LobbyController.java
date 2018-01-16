package controllers;

import business.Lobby;
import business.MainServerManager;
import business.exceptions.AlreadyHostingException;
import business.statics.StaticPlayer;
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
import networking.MessageType;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LobbyController implements Initializable, Observer {

    @FXML
    private Button btnCreateGame;
    @FXML
    private ListView<Lobby> lvLobby;
    @FXML
    private TextField tfSend;
    @FXML
    private ListView lvChat;

    private Logger log = Logger.getLogger("warning");
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
        mainServerManager.sendMessage(MessageType.CHAT_MESSAGE, text);
    }

    @FXML
    public void btnCreateGame() {
        createLobbyScreen(true, new Lobby(StaticPlayer.getPlayer().getName(), "localhost"));
    }

    @FXML
    public void LisViewClick(MouseEvent click) {
        if (click.getClickCount() == 2 && lvLobby.getSelectionModel().getSelectedIndex() != -1) {
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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../CreateGame.fxml"));
        Parent root1 = null;

        try {
            CreateGameController createGameController = new CreateGameController();
            createGameController.setLobby(lobby);

            if (isHost) {
                try {
                    lobby.startHosting(createGameController);
                } catch (AlreadyHostingException e) {
                    log.warning(e.toString());
                }
            } else {
                String ip = lvLobby.getSelectionModel().getSelectedItem().getIP();
                try {
                    lobby.joinLobby(ip, 1337, createGameController);
                } catch (AlreadyHostingException e) {
                    log.warning(e.toString());
                }
            }

            createGameController.setPreviousStage(stage);

            fxmlLoader.setController(createGameController);
            root1 = fxmlLoader.load();

            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            stage2.show();
        } catch (IOException e) {
            log.warning(e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            Platform.runLater(() -> lvChat.getItems().add(arg.toString()));
        }

        if (arg instanceof Lobby) {
            this.refreshLobbyListView();
        }
    }
}
