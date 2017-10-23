import Business.*;
import Business.staticClasses.StaticLobby;
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
import networking.ServerClient;
import networking.ServerClientEvents;
import networking.ServerHost;
import networking.ServerHostEvents;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.*;
import java.util.*;

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

    private MainServerManager mainServerManager;

    private ServerHost host;
    private ServerClient lobbyClient;

    private Map<Socket, Player> players = new HashMap<>();

    private Stage previousStage;

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    private List<Cardset> Cardsets = null;
    private List<Cardset> CardsetsPicked = null;

    public void initialize(URL location, ResourceBundle resources) {
        Cardsets = new ArrayList<>();
        CardsetsPicked = new ArrayList<>();

//        Lobby lobby = app.getLobbyFromId(lobbyId);
//        lobby.getCardSetsDatabase();

        Update();
    }

    public void setHost() {
        try {
            this.host = new ServerHost(6, this);
            host.start();

            this.mainServerManager = MainServerManager.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.mainServerManager.sendMessage("<L>" + StaticPlayer.getName() + ";" + InetAddress.getLocalHost().getHostAddress() + "</L>");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setClient(ServerClient client) {
        this.lobbyClient = client;
        lobbyClient.start();
    }

    @FXML
    private void btnRight(Event e) {
//        Lobby lobby = app.getLobbyFromId(lobbyId);
//
//        if (lvCardsets.getSelectionModel().getSelectedItem() != null) {
//            Cardset set = (Cardset) lvCardsets.getSelectionModel().getSelectedItem();
//            lobby.setToUsingSets(set);
//            Update();
//        }
    }

    @FXML
    private void btnLeft(Event e) {
//        Lobby lobby = app.getLobbyFromId(lobbyId);
//
//        if (lvPickedCards.getSelectionModel().getSelectedItem() != null) {
//            Cardset set = (Cardset) lvPickedCards.getSelectionModel().getSelectedItem();
//            lobby.setToNotUsingSets(set);
//            Update();
//        }
    }

    @FXML
    private void btnSend(Event e) {
        if (tfChatBox.getText().equals("")) {
            return;
        }
        if (host != null) {
            host.messageAll("<C>" + StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText() + "</C>");
            putChatMessage(StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText());
        } else {
            lobbyClient.sendMessage("<C>" + StaticPlayer.getPlayer().getName() + ": " + tfChatBox.getText() + "</C>");
        }

        tfChatBox.setText("");
    }

    @FXML
    private void btnChangeNickName(Event e) {
        //not implemented
    }

    @FXML
    private void btnLeaveGame(Event e) {
        if (host != null) {
            try {
                //goes to different view
                //starts the game with current options
                Stage stage = (Stage) btnStartGame.getScene().getWindow();
                stage.close();

                mainServerManager.sendMessage("<L>quit</L>");
                host.close();

                previousStage.show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @FXML
    private void btnStartGame(Event e) throws Exception {
        //goes to different view
        //starts the game with current options
        Stage stage = (Stage) btnStartGame.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1));
        stage2.show();
    }

    @FXML
    private void UpdateGameOptions(Event e) {
        StaticLobby.setScorelimit(Integer.parseInt((String) ddScorelimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setMaxplayers(Integer.parseInt((String) ddPlayerLimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setMaxspectators(Integer.parseInt((String) ddSpectatorLimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setTimelimit((String) ddIdleTimer.getSelectionModel().getSelectedItem());
        StaticLobby.setBlankcards(Integer.parseInt((String) ddBlankCards.getSelectionModel().getSelectedItem()));
    }

    public void Update() {
//        Lobby lobby = app.getLobbyFromId(lobbyId);
//
//        lvPickedCards.getItems().clear();
//        lvCardsets.getItems().clear();
//
//        for (Cardset C : lobby.getCardSetsNotUsing()) {
//            lvCardsets.getItems().add(C);
//        }
//
//        for (Cardset C : lobby.getCardSetsUsing()) {
//            lvPickedCards.getItems().add(C);
//        }
    }


    @Override
    public void onClientMessage(Socket client, String message) {
        String clientDataString = getClientData(message);
        if (clientDataString != null) {
            players.put(client, new Player(clientDataString));
            updateScoreBoard();
            return;
        }

        String chatMessage = getChatMessage(message);
        if (!chatMessage.equals("")) {
            host.messageAll(message);
            putChatMessage(chatMessage);
        }
    }

    @Override
    public void onClientJoin(Socket client) {

    }

    @Override
    public void onClientLeave(Socket client) {
        players.remove(client);
    }

    @Override
    public void onHostMessage(String message) {
        String chatMessage = getChatMessage(message);
        if (!chatMessage.equals("")) {
            putChatMessage(chatMessage);
        }
    }

    @Override
    public void onJoin(SocketAddress address) {
        lobbyClient.sendMessage("<D>" + lobbyClient.getPlayer().getName() + "</D>");
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

            for (Player player : players.values()) {
                lvScore.getItems().add(player.getName());
            }
        });
    }
}
