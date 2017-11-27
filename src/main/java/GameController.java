import Business.CzarCard;
import Business.Enums.Role;
import Business.Lobby;
import Business.PlayCard;
import Business.Player;
import Business.exceptions.NotClientException;
import Business.staticClasses.StaticPlayer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
import java.util.ResourceBundle;

public class GameController implements Initializable, ServerHostEvents, ServerClientEvents {
    @FXML
    private Button btnSend;
    @FXML
    private Button btnChoose;
    @FXML
    private ListView lvPlayers;
    @FXML
    private RadioButton rbtnCard1;
    @FXML
    private RadioButton rbtnCard2;
    @FXML
    private RadioButton rbtnCard3;
    @FXML
    private RadioButton rbtnCard4;
    @FXML
    private RadioButton rbtnCard5;
    @FXML
    private RadioButton rbtnCard6;
    @FXML
    private RadioButton rbtnCard7;
    @FXML
    private RadioButton rbtnCard8;

    @FXML
    private RadioButton rbCzarPick1;
    @FXML
    private RadioButton rbCzarPick2;
    @FXML
    private RadioButton rbCzarPick3;
    @FXML
    private RadioButton rbCzarPick4;

    @FXML
    private TextArea taCzar1;
    @FXML
    private TextArea taCzar2;
    @FXML
    private TextArea taCzar3;
    @FXML
    private TextArea taCzar4;

    @FXML
    private TextArea taCard1;
    @FXML
    private TextArea taCard2;
    @FXML
    private TextArea taCard3;
    @FXML
    private TextArea taCard4;
    @FXML
    private TextArea taCard5;
    @FXML
    private TextArea taCard6;
    @FXML
    private TextArea taCard7;
    @FXML
    private TextArea taCard8;
    @FXML
    private TextArea taBlackCard;
    @FXML
    private TextArea taChatHistory;
    @FXML
    private TextField tbChat;
    @FXML
    private Button btnLeaveGame;
    @FXML
    private HBox hboxPlayerSelect;

    private Lobby lobby;
    int playerGottenCard = 0;

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    private List<String> chatList = new ArrayList<String>();

    public void initialize(URL location, ResourceBundle resources) {
        if (!lobby.isHost()) {
            try {
                lobby.messageServer(MessageType.RECEIVE_CARD, "!");
            } catch (NotClientException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void btnSend(Event e) {
        String chat;

        chat = tbChat.getText();
        chatList.add(chat);
    }

    @FXML   //Czar chooses a card and a new rounds starts
    public void btnChoose(Event e) {
        Player player = StaticPlayer.getPlayer();

        boolean check = false;
        if (player.getRole().equals(Role.Pleb)) {
            //hier moet een betere manier voor zijn

            if (rbtnCard1.isSelected()) {
                lobby.getGame().playerPicksCard(0, player);
            } else if (rbtnCard2.isSelected()) {
                lobby.getGame().playerPicksCard(1, player);
            } else if (rbtnCard3.isSelected()) {
                lobby.getGame().playerPicksCard(2, player);
            } else if (rbtnCard4.isSelected()) {
                lobby.getGame().playerPicksCard(3, player);
            } else if (rbtnCard5.isSelected()) {
                lobby.getGame().playerPicksCard(4, player);
            } else if (rbtnCard6.isSelected()) {
                lobby.getGame().playerPicksCard(5, player);
            } else if (rbtnCard7.isSelected()) {
                lobby.getGame().playerPicksCard(6, player);
            } else if (rbtnCard8.isSelected()) {
                lobby.getGame().playerPicksCard(7, player);
            }
            if (lobby.getGame().playedCard(player)) {
                hboxPlayerSelect.setVisible(false);
            }
        } else if (player.getRole().equals(Role.Czar)) {
            if (rbCzarPick1.isSelected()) {
                String cardtext = taCzar1.getText();
                lobby.getGame().czarPicksCards(cardtext);
            } else if (rbCzarPick2.isSelected()) {
                String cardtext = taCzar2.getText();
                lobby.getGame().czarPicksCards(cardtext);
            } else if (rbCzarPick3.isSelected()) {
                String cardtext = taCzar3.getText();
                lobby.getGame().czarPicksCards(cardtext);
            } else if (rbCzarPick4.isSelected()) {
                String cardtext = taCzar4.getText();
                lobby.getGame().czarPicksCards(cardtext);
            }
        } else {
            //Do nothing
        }
    }

    public void btnLeaveGame() {
        Stage stage = (Stage) btnLeaveGame.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LobbyView.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1));
        stage2.show();
    }

    //laad aan het begin van een niewe ronde de kaarten voor een client in
    public void loadPlayerHand() {
        Player player = StaticPlayer.getPlayer();
        List<PlayCard> cardsInHand = player.getCardsInHand();

        CzarCard CzarCard = lobby.getGame().getCurrentCzarCard();

        taCard1.setText(cardsInHand.get(0).getText());
        taCard2.setText(cardsInHand.get(1).getText());
        taCard3.setText(cardsInHand.get(2).getText());
        taCard4.setText(cardsInHand.get(3).getText());
        taCard5.setText(cardsInHand.get(4).getText());
        taCard6.setText(cardsInHand.get(5).getText());
        taCard7.setText(cardsInHand.get(6).getText());
        taCard8.setText(cardsInHand.get(7).getText());
        taBlackCard.setText(CzarCard.getText());
    }

    //moet aangeroepen worden wanneer het de turn is van de czar en wanneer een nieuwe beurt begint
    public void updateTurn() {
        this.changeControlVisibility();
    }

    private void changeControlVisibility() {
        switch (StaticPlayer.getPlayer().getRole()) {
            case Czar:
                this.setCzarRadioButtonsVisibility(true);
                this.setPlebRadioButtonsVisibility(false);
                break;

            case Pleb:
                this.setCzarRadioButtonsVisibility(false);
                this.setPlebRadioButtonsVisibility(true);
                break;

            case Spectator:
                this.setCzarRadioButtonsVisibility(false);
                this.setPlebRadioButtonsVisibility(false);
                break;
        }
    }

    private void setCzarRadioButtonsVisibility(boolean value) {
        rbCzarPick1.setVisible(value);
        rbCzarPick2.setVisible(value);
        rbCzarPick3.setVisible(value);
        rbCzarPick4.setVisible(value);
    }

    private void setPlebRadioButtonsVisibility(boolean value) {
        rbtnCard1.setVisible(value);
        rbtnCard2.setVisible(value);
        rbtnCard3.setVisible(value);
        rbtnCard4.setVisible(value);
        rbtnCard5.setVisible(value);
        rbtnCard6.setVisible(value);
        rbtnCard7.setVisible(value);
        rbtnCard8.setVisible(value);
    }

    @Override
    public void onClientMessage(Socket client, MessageType messageType, String message) {
        switch (messageType) {
            case CHAT_MESSAGE:
                break;
            case LOBBY_DATA:
                break;
            case LOBBY_QUIT:
                break;
            case PLAYER_DATA:
                break;
            case LOBBY_LIST_SYNC_REQUEST:
                break;
            case START_GAME:
                break;
            case UPDATE_LOBBY_SETTINGS:
                break;
            case RECEIVE_CARD:
                playerGottenCard++;
                if (playerGottenCard == lobby.getPlayers().size() - 1) {
                    lobby.getGame().newTurn();
                    playerGottenCard = 0;
                    loadPlayerHand();
                }
                break;

            case PLAY_CARD:

                int id = Integer.parseInt(message);

                PlayCard card = null;
                for (PlayCard playCard : lobby.getGame().getAllCards()) {
                    if (playCard.getId() == id) {
                        card = playCard;
                        break;
                    }
                }

                if (lobby.getGame().addToChosenCards(lobby.getPlayers().get(client), card) ){
                    ArrayList<PlayCard> list = new ArrayList<>();
                    try {
                        list.addAll(lobby.getGame().getChosenCards().values());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        break;
                    }
                    taCzar1.setText(list.get(0).getText());
                    taCzar2.setText(list.get(1).getText());
                    taCzar3.setText(list.get(2).getText());
                    taCzar4.setText(list.get(3).getText());
                }

                break;

            case CHOSEN_CARDS:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClientJoin(Socket client) {

    }

    @Override
    public void onClientLeave(Socket client) {

    }

    @Override
    public void onHostMessage(MessageType messageType, String message) {
        System.out.println("on host");
        switch (messageType) {
            case CHAT_MESSAGE:
                break;
            case LOBBY_DATA:
                break;
            case LOBBY_QUIT:
                break;
            case PLAYER_DATA:
                break;
            case LOBBY_LIST_SYNC_REQUEST:
                break;
            case START_GAME:
                break;
            case UPDATE_LOBBY_SETTINGS:
                break;
            case RECEIVE_CARD:
                Player player = StaticPlayer.getPlayer();
                player.getCardsInHand().clear();

                String[] array = message.split(",");

                for (CzarCard card : lobby.getGame().getCzarCards()) {
                    if (array[0].equals(String.valueOf(card.getId()))) {
                        lobby.getGame().setCurrentCzar(card);
                        break;
                    }
                }

                for (int i = 1; i < array.length; i++) {
                    for (PlayCard P : lobby.getGame().getPlayCards()) {

                        if (array[i].equals(String.valueOf(P.getId()))) {
                            player.addToHand(P);
                        }
                    }
                }

                loadPlayerHand();
                break;

            case PLAY_CARD:
                break;
            case CHOSEN_CARDS:
                ArrayList<PlayCard> list = new ArrayList<>();
                try {
                    list.addAll(lobby.getGame().getChosenCards().values());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    break;
                }
                taCzar1.setText(list.get(0).getText());
                taCzar2.setText(list.get(1).getText());
                taCzar3.setText(list.get(2).getText());
                taCzar4.setText(list.get(3).getText());
                break;
            case GET_ROLE:
                Role role = Role.values()[Integer.valueOf(message)];
                StaticPlayer.getPlayer().setRole(role);
                updateTurn();
                break;
            default:
                break;
        }
    }

    
    @Override
    public void onJoin(SocketAddress address) {

    }

    @Override
    public void onServerClose() {

    }
}
