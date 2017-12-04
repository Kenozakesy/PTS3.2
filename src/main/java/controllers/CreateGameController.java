package controllers;

import business.CardSet;
import business.Lobby;
import business.Player;
import business.exceptions.NotClientException;
import business.exceptions.NotHostException;
import business.statics.StaticPlayer;
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Gebruiker on 12-9-2017.
 */


public class CreateGameController implements Initializable, ChangeListener<String>, ServerHostEvents, ServerClientEvents {

    @FXML
    private Button btnStartGame;

    @FXML
    private Label lbPlayers;

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
            CardSet set = (CardSet) lvCardsets.getSelectionModel().getSelectedItem();
            lobby.setToUsingSets(set);

            try {
                lobby.messageClients(MessageType.UPDATE_CARDSETS, String.valueOf(set.getId()));
            } catch (NotHostException e1) {
                e1.printStackTrace();
            }

            updateCardSets();
        }
    }

    @FXML
    private void btnLeft(Event e) {
        if (lvPickedCards.getSelectionModel().getSelectedItem() != null) {
            CardSet set = (CardSet) lvPickedCards.getSelectionModel().getSelectedItem();
            lobby.setToNotUsingSets(set);

            try {
                lobby.messageClients(MessageType.UPDATE_CARDSETS, String.valueOf(set.getId()));
            } catch (NotHostException e1) {
                e1.printStackTrace();
            }

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
        lobby.disconnect();
        this.returnToPreviousScreen();
    }

    @FXML
    private void btnStartGame(Event e) {
        //goes to different view
        //starts the game with current options

        if (lobby.getPlayers().size() <= lobby.getMaxPlayers()) {
            try {
                if (lvPickedCards.getItems().size() < 1) {
                    lbError.setText("Please choose a cardpack.");
                } else {
                    startGameScreen(lobby);
                    lobby.messageClients(MessageType.START_GAME, "");
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            lbPlayers.setText("Too many players to start!");
        }
    }

    public void updateCardSets() {
        lvPickedCards.getItems().clear();
        lvCardsets.getItems().clear();

        for (CardSet C : lobby.getCardSetsNotUsing()) {
            lvCardsets.getItems().add(C);
        }

        for (CardSet C : lobby.getCardSetsUsing()) {
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
            case PLAYER_DATA:
                handleClientPlayerData(client, message);
                break;
        }
    }

    @Override
    public void onClientJoin(Socket client) {
        // Not used for now.
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
            case PLAYER_DATA:
                handleHostPlayerData(message);
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

            case UPDATE_CARDSETS:
                int id = Integer.parseInt(message);

                for (CardSet set : lobby.getCardSetsUsing()) {
                    if (set.getId() == id) {
                        lobby.getCardSetsUsing().remove(set);
                        lobby.getCardSetsNotUsing().add(set);
                        Platform.runLater(() -> updateCardSets());
                        return;
                    }

                }

                for (CardSet set : lobby.getCardSetsNotUsing()) {
                    if (set.getId() == id) {
                        lobby.getCardSetsNotUsing().remove(set);
                        lobby.getCardSetsUsing().add(set);
                        Platform.runLater(() -> updateCardSets());
                        return;
                    }
                }

                break;

            case INIT_PRE_CHOSEN_CARD_SETS:
                String[] setIds = message.split(",");

                List<CardSet> sets = new ArrayList<>();

                for (String setId : setIds) {
                    int parsedId = Integer.parseInt(setId);

                    for (CardSet set : lobby.getCardSetsNotUsing()) {
                        if (set.getId() == parsedId) {
                            sets.add(set);
                            break;
                        }
                    }
                }

                for (CardSet set : sets) {
                    lobby.getCardSetsNotUsing().remove(set);
                    lobby.getCardSetsUsing().add(set);
                }

                Platform.runLater(() -> updateCardSets());
                break;
        }
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
        this.returnToPreviousScreen();
    }

    private void putChatMessage(String input) {
        Platform.runLater(() -> lvChat.getItems().add(input));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../GameView.fxml"));

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
        this.sendChosenCardSets(client);

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
            String opacityCSS = "-fx-opacity: 1";
            ddScorelimit.setStyle(opacityCSS);
            ddPlayerLimit.setStyle(opacityCSS);
            ddSpectatorLimit.setStyle(opacityCSS);
            ddIdleTimer.setStyle(opacityCSS);
            ddBlankCards.setStyle(opacityCSS);
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // This only gets called when the user is a server host.
        lobby.setScoreLimit(ddScorelimit.getSelectionModel().getSelectedIndex());
        lobby.setMaxPlayers(ddPlayerLimit.getSelectionModel().getSelectedIndex());
        lobby.setMaxSpectators(ddSpectatorLimit.getSelectionModel().getSelectedIndex());
        lobby.setTimeLimit(ddIdleTimer.getSelectionModel().getSelectedIndex());
        lobby.setBlankCards(ddBlankCards.getSelectionModel().getSelectedIndex());

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

    private void sendChosenCardSets(Socket client) {
        // Geeft de cardsets mee aan de client
        StringBuilder builder = new StringBuilder();

        for (CardSet set : lobby.getCardSetsUsing()) {
            builder.append(set.getId() + ",");
        }

        try {
            lobby.messageClient(client, MessageType.INIT_PRE_CHOSEN_CARD_SETS, builder.toString());
        } catch (NotHostException e) {
            e.printStackTrace();
        }
    }


    private void returnToPreviousScreen() {
        Stage stage = (Stage) btnStartGame.getScene().getWindow();
        stage.close();

        previousStage.show();
    }
}