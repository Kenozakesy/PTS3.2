import Business.Cardset;
import Business.Lobby;
import Business.MainServerManager;
import Business.Player;
import Business.exceptions.NotClientException;
import Business.exceptions.NotHostException;
import Business.staticClasses.StaticPlayer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import networking.ServerClientEvents;
import networking.ServerHostEvents;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Gebruiker on 12-9-2017.
 */


public class StartGameController implements Initializable, ServerHostEvents, ServerClientEvents {

    @FXML
    public Button btnStartGame;

    @FXML
    private ListView lvPickedCards;

    @FXML
    private ListView lvCardsets;

    @FXML
    private ComboBox ddScorelimit;

    @FXML
    private ComboBox ddPlayerLimit;

    @FXML
    private ComboBox ddSpectatorLimit;

    @FXML
    private ComboBox ddIdleTimer;

    @FXML
    private ComboBox ddBlankCards;

    @FXML
    private TextField tfChatBox;

    @FXML
    private ListView lvChat;

    @FXML
    private ListView lvScore;

    private String lobbyId;

    private MainServerManager mainServerManager = MainServerManager.getInstance();

    private Stage previousStage;

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    private Lobby lobby;

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void initialize(URL location, ResourceBundle resources) {
        lobby.getPlayers().put(new Socket(), StaticPlayer.getPlayer());

        updateScoreBoard();
        updateCardSets();
    }

    @FXML
    private void btnRight(Event e) {
        if (lvCardsets.getSelectionModel().getSelectedItem() != null) {
            Cardset set = (Cardset) lvCardsets.getSelectionModel().getSelectedItem();
            lobby.setToUsingSets(set);
            updateCardSets();
        }
    }

    @FXML
    private void btnLeft(Event e) {
        if (lvPickedCards.getSelectionModel().getSelectedItem() != null) {
            Cardset set = (Cardset) lvPickedCards.getSelectionModel().getSelectedItem();
            lobby.setToNotUsingSets(set);
            updateCardSets();
        }
    }

    @FXML
    private void btnSend(Event e) {
        if (tfChatBox.getText().equals("")) {
            return;
        }
        if (lobby.isHost()) {
            try {
                lobby.messageClients("<C>" + StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText() + "</C>");
            } catch (NotHostException e1) {
                e1.printStackTrace();
            }

            putChatMessage(StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText());
        } else {
            try {
                lobby.messageServer("<C>" + StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText() + "</C>");
            } catch (NotClientException e1) {
                e1.printStackTrace();
            }
        }

        tfChatBox.setText("");
    }

    @FXML
    private void btnChangeNickName(Event e) {
        //not implemented
    }

    @FXML
    private void btnLeaveGame(Event e) {
        if (lobby.isHost()) {
            //goes to different view
            //starts the game with current options
            Stage stage = (Stage) btnStartGame.getScene().getWindow();
            stage.close();

            mainServerManager.sendMessage("<L>quit</L>");

            try {
                lobby.close();
            } catch (NotHostException e1) {
                e1.printStackTrace();
            }

            previousStage.show();
        }
    }

    @FXML
    private void btnStartGame(Event e) throws Exception {
        //goes to different view
        //starts the game with current options
        try {
            if (lvPickedCards.getItems().size() < 1) {
                throw new Exception("Errormessage: Geen cardsets geslecteerd.");
            }
            Stage stage = (Stage) btnStartGame.getScene().getWindow();
            stage.close();
            
            lobby.startGame();
            lobby.messageClients("Start game");
            startGameScreen(lobby);
        } catch(Exception exception) {
            exception.printStackTrace();
            System.out.println(exception.getMessage());
        }
    }

    public void updateCardSets() {
        lvPickedCards.getItems().clear();
        lvCardsets.getItems().clear();

        for (Cardset C : lobby.getCardSetsNotUsing()) {
            lvCardsets.getItems().add(C);
        }

        for (Cardset C : lobby.getCardSetsUsing()) {
            lvPickedCards.getItems().add(C);
        }
    }

    @Override
    public void onClientMessage(Socket client, String message) {
        String clientDataString = getClientData(message);
        if (clientDataString != null) {
            Player player = new Player(clientDataString);
            lobby.getPlayers().put(client, player);

            try {
                for (Player p : lobby.getPlayers().values()) {
                    if (p == player) {
                        for (Player p2 : lobby.getPlayers().values()) {
                            if (p2 == p) continue;

                            lobby.messageClient(p2, "<D>" + p2.getName() + "</D>");
                        }
                    } else if (p != StaticPlayer.getPlayer()) {
                        lobby.messageClient(p, message);
                    }
                }
            } catch (NotHostException ex) {
                ex.printStackTrace();
            }

            updateScoreBoard();
        }

        String chatMessage = getChatMessage(message);
        if (chatMessage != null) {
            try {
                lobby.messageClients(message);
            } catch (NotHostException e) {
                e.printStackTrace();
            }

            putChatMessage(chatMessage);
        }
    }

    @Override
    public void onClientJoin(Socket client) {

    }

    @Override
    public void onClientLeave(Socket client) {
        lobby.getPlayers().remove(client);
    }

    @Override
    public void onHostMessage(String message) {
        String clientData = getClientData(message);
        if (clientData != null) {
            lobby.getPlayers().put(new Socket(), new Player(clientData));
            updateScoreBoard();
        }

        String chatMessage = getChatMessage(message);
        if (chatMessage != null) {
            putChatMessage(chatMessage);
        }

        //TODO start spel
        // Start van het spel
        if (message.equals("Start game")) {
            try {
                startGameScreen(lobby);
                //startGameScreen(lobby);
            } catch (IOException ex) {
                // k
            }
        }

        //TODO veranderen van cardsets
        if (message.equals("Change cardsets")) {

        }
        //TODO verlaten van spelers
        //TODO veranderen van gamesettings
    }

    @Override
    public void onJoin(SocketAddress address) {
        try {
            lobby.messageServer("<D>" + StaticPlayer.getPlayer().getName() + "</D>");
        } catch (NotClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServerClose() {

    }

    private String getClientData(String input) {
        return StringUtils.substringBetween(input, "<D>", "</D>");
    }

    private String getChatMessage(String input) {
        return StringUtils.substringBetween(input, "<C>", "</C>");
    }

    private void putChatMessage(String input) {
        Platform.runLater(() -> {
            lvChat.getItems().add(input);
        });
    }

    private void updateScoreBoard() {
        Platform.runLater(() -> {
            lvScore.getItems().clear();

            for (Player player : lobby.getPlayers().values()) {
                lvScore.getItems().add(player.getName() + ": " + player.getPoints());
            }
        });
    }

    private void startGameScreen(Lobby lobby) throws IOException {
        try {
            //starts the game with current options
            //Stage stage = (Stage) btnStartGame.getScene().getWindow();
            //stage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameView.fxml"));

            GameController gameController = new GameController();
            gameController.setLobby(lobby);

            fxmlLoader.setController(gameController);

            Parent root1 = fxmlLoader.load();
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            stage2.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}