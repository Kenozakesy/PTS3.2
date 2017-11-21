import Business.Cardset;
import Business.Lobby;
import Business.MainServerManager;
import Business.Player;
import Business.exceptions.NotClientException;
import Business.exceptions.NotHostException;
import Business.staticClasses.StaticPlayer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import networking.MessageType;
import networking.ServerClientEvents;
import networking.ServerHostEvents;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Gebruiker on 12-9-2017.
 */


public class CreateGameController implements Initializable, ChangeListener<String>, ServerHostEvents, ServerClientEvents {

    @FXML
    private Button btnStartGame;

    @FXML
    private Label lbError;

    @FXML
    private Button btnLeft;

    @FXML
    private Button btnRight;

    @FXML
    private ListView lvPickedCards;

    @FXML
    private ListView lvCardsets;

    @FXML
    private ComboBox<String> ddScorelimit;

    @FXML
    private ComboBox<String> ddPlayerLimit;

    @FXML
    private ComboBox<String> ddSpectatorLimit;

    @FXML
    private ComboBox<String> ddIdleTimer;

    @FXML
    private ComboBox<String> ddBlankCards;

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

        disableControls();

        if (lobby.isHost()) {
            ddBlankCards.valueProperty().addListener(this);
            ddIdleTimer.valueProperty().addListener(this);
            ddPlayerLimit.valueProperty().addListener(this);
            ddScorelimit.valueProperty().addListener(this);
            ddSpectatorLimit.valueProperty().addListener(this);
        }

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
                lobby.messageClients(MessageType.CHAT_MESSAGE, StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText());
            } catch (NotHostException e1) {
                e1.printStackTrace();
            }

            putChatMessage(StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText());
        } else {
            try {
                lobby.messageServer(MessageType.CHAT_MESSAGE, StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText());
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
            mainServerManager.sendMessage(MessageType.LOBBY_QUIT, "!");

            try {
                lobby.close();
            } catch (NotHostException e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                lobby.disconnect();
            } catch (NotClientException e1) {
                e1.printStackTrace();
            }
        }

        Stage stage = (Stage) btnStartGame.getScene().getWindow();
        stage.close();

        previousStage.show();
    }

    @FXML
    private void btnStartGame(Event e) throws Exception {
        //goes to different view
        //starts the game with current options
        try {
            if (lvPickedCards.getItems().size() < 1) {
                lbError.setText("Please choose a cardpack.");
                //throw new Exception("Errormessage: Geen cardsets geselecteerd.");
            }
            else {
                // Geeft de cardsets mee aan de clients en start de schermen bij de spelers
                StringBuilder builder = new StringBuilder();

                for (Object o : lvPickedCards.getItems()) {
                    Cardset cardset = (Cardset) o;
                    builder.append(cardset.getId() + ",");
                }

                lobby.messageClients(MessageType.START_GAME, builder.toString());
                startGameScreen(lobby);
            }

        } catch (Exception exception) {
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
    public void onClientMessage(Socket client, MessageType messageType, String message) {
        switch (messageType) {

            case CHAT_MESSAGE:
                try {
                    lobby.messageClients(MessageType.CHAT_MESSAGE, message);
                } catch (NotHostException e) {
                    e.printStackTrace();
                }

                putChatMessage(message);
                break;
            case LOBBY_DATA:
                break;
            case PLAYER_DATA:
                handleClientPlayerData(client, message);
                break;
            case LOBBY_LIST_SYNC_REQUEST:
                break;
            case START_GAME:
                break;
        }
    }

    @Override
    public void onClientJoin(Socket client) {

    }

    @Override
    public void onClientLeave(Socket client) {
        System.out.println(lobby.getPlayers().get(client).getName() + "has left!");
        lobby.getPlayers().remove(client);
        updateScoreBoard();
    }

    @Override
    public void onHostMessage(MessageType messageType, String message) {
        switch (messageType) {
            case CHAT_MESSAGE:
                putChatMessage(message);
                break;
            case LOBBY_DATA:
                break;
            case LOBBY_QUIT:
                break;
            case PLAYER_DATA:
                handleHostPlayerData(message);
                break;
            case LOBBY_LIST_SYNC_REQUEST:
                break;
            case START_GAME:
                handleStartGame(message);
                break;
            case UPDATE_LOBBY_SETTINGS:
                String[] splitMessage = message.split(",");

                Platform.runLater(() -> {
                    ddScorelimit.getSelectionModel().select(Integer.valueOf(splitMessage[0]));
                    ddPlayerLimit.getSelectionModel().select(Integer.valueOf(splitMessage[1]));
                    ddSpectatorLimit.getSelectionModel().select(Integer.valueOf(splitMessage[2]));
                    ddIdleTimer.getSelectionModel().select(Integer.valueOf(splitMessage[3]));
                    ddBlankCards.getSelectionModel().select(Integer.valueOf(splitMessage[4]));
                });
                break;
        }

        //TODO verlaten van spelers
        //TODO veranderen van gamesettings
    }

    @Override
    public void onJoin(SocketAddress address) {
        try {
            lobby.messageServer(MessageType.PLAYER_DATA, StaticPlayer.getPlayer().getName());
        } catch (NotClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServerClose() {

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
            Stage stage = (Stage) btnStartGame.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameView.fxml"));

            GameController gameController = new GameController();
            gameController.setLobby(lobby);

            lobby.startGame();

            //kaarten verdelen
            if (lobby.isHost()) {
                lobby.setHostEventHandler(gameController);
            } else {
                System.out.println("Setting client eventhandler");
                lobby.setClientEventHandler(gameController);
            }

            fxmlLoader.setController(gameController);

            System.out.println("Loading");
            Parent root1 = fxmlLoader.load();
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            System.out.println("Showing");
            stage2.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleClientPlayerData(Socket client, String message) {
        try {
            //Send all current players to the connecting client.
            for (Map.Entry<Socket, Player> entry : lobby.getPlayers().entrySet()) {
                lobby.messageClient(client, MessageType.PLAYER_DATA, entry.getValue().getName());
            }

            //Send current players the new player
            for (Map.Entry<Socket, Player> entry : lobby.getPlayers().entrySet()) {
                if (entry.getValue() == StaticPlayer.getPlayer()) {
                    continue;
                }

                lobby.messageClient(entry.getKey(), MessageType.PLAYER_DATA, message);
            }
        } catch (NotHostException e) {
            e.printStackTrace();
        }

        Player player = new Player(message);
        lobby.getPlayers().put(client, player);

        updateScoreBoard();
    }

    private void handleHostPlayerData(String message) {
        lobby.getPlayers().put(new Socket(), new Player(message));
        updateScoreBoard();
    }

    private void handleStartGame(String message) {
        String[] splitMessage = message.split(",");

        ArrayList<Cardset> sets = new ArrayList<>();

        for (int i = 0; i < splitMessage.length; i++) {
            sets.add(lobby.getCardSetsNotUsing().get(Integer.valueOf(splitMessage[i])));
        }

        lobby.setCardSetsUsing(sets);

        System.out.println("Starting");

        Platform.runLater(() -> {
            try {
                startGameScreen(lobby);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void disableControls() {
        if (lobby != null && !lobby.isHost()) {
            //Disable/hide controls which should not be used by a client
            ddSpectatorLimit.setDisable(true);
            ddScorelimit.setDisable(true);
            ddPlayerLimit.setDisable(true);
            ddIdleTimer.setDisable(true);
            ddBlankCards.setDisable(true);

            btnStartGame.setVisible(false);
            btnLeft.setDisable(true);
            btnRight.setDisable(true);

            //Set the opacity to 1 so that the value is more readable
            ddScorelimit.setStyle("-fx-opacity: 1");
            ddPlayerLimit.setStyle("-fx-opacity: 1");
            ddSpectatorLimit.setStyle("-fx-opacity: 1");
            ddIdleTimer.setStyle("-fx-opacity: 1");
            ddBlankCards.setStyle("-fx-opacity: 1");
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // This only gets called when the user is a server host.

        String message =
                ddScorelimit.getSelectionModel().getSelectedIndex() + "," +
                        ddPlayerLimit.getSelectionModel().getSelectedIndex() + "," +
                        ddSpectatorLimit.getSelectionModel().getSelectedIndex() + "," +
                        ddIdleTimer.getSelectionModel().getSelectedIndex() + "," +
                        ddBlankCards.getSelectionModel().getSelectedIndex();

        try {
            lobby.messageClients(MessageType.UPDATE_LOBBY_SETTINGS, message);
        } catch (NotHostException e) {
            e.printStackTrace();
        }
    }
}