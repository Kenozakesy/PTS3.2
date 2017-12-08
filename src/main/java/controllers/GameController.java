package controllers;

import business.*;
import business.exceptions.NotClientException;
import business.statics.StaticPlayer;
import javafx.application.Platform;
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
    private ListView lvScore;
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
    @FXML
    private HBox hboxCzarSelect;

    private Lobby lobby;
    int cardRequestCount = 0;

    private synchronized void incrementCardRequestCount() {
        cardRequestCount++;
    }

    private synchronized boolean checkCardRequestCount() {
        if (cardRequestCount == lobby.getPlayers().size() - 1) {
            cardRequestCount = 0;
            return true;
        }

        return false;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    private List<String> chatList = new ArrayList<>();

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

    @FXML   //CZAR chooses a card and a new rounds starts
    public void btnChoose(Event e) {
        Player player = StaticPlayer.getPlayer();
        boolean hostListisFull = false;

        if (player.getRole() == Role.PLEB) {
            //hier moet een betere manier voor zijn

            if (rbtnCard1.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(0, player);
            } else if (rbtnCard2.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(1, player);
            } else if (rbtnCard3.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(2, player);
            } else if (rbtnCard4.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(3, player);
            } else if (rbtnCard5.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(4, player);
            } else if (rbtnCard6.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(5, player);
            } else if (rbtnCard7.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(6, player);
            } else if (rbtnCard8.isSelected()) {
                hostListisFull = lobby.getGame().playerPicksCard(7, player);
            }
            if (lobby.getGame().playedCard(player)) {
                hboxPlayerSelect.setVisible(false);
            }
            if (hostListisFull){
                getCardsForHost();
            }
        } else if (player.getRole() == Role.CZAR) {
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
            if(lobby.isHost())
            {
                updateTurn();
            }
        }
        loadPlayerHand();
    }

    public void btnLeaveGame() {
        // leave game method for later
        throw new UnsupportedOperationException();
    }

     void returnToMainScreen() {
        Platform.runLater(() -> {
            Stage stage = (Stage) btnLeaveGame.getScene().getWindow();
            stage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../LobbyView.fxml"));
            Parent root1 = null;
            try {
                root1 = fxmlLoader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            stage2.show();
        });
    }

    //laad aan het begin van een niewe ronde de kaarten voor een client in
    private void loadPlayerHand() {
        List<PlayCard> cardsInHand = StaticPlayer.getPlayer().getCardsInHand();
        CzarCard czarCard = lobby.getGame().getCurrentCzarCard();


        taCard1.clear();
        taCard2.clear();
        taCard3.clear();
        taCard4.clear();
        taCard5.clear();
        taCard6.clear();
        taCard7.clear();
        taCard8.clear();

        try
        {
            taCard1.setText(cardsInHand.get(0).getText());
            taCard2.setText(cardsInHand.get(1).getText());
            taCard3.setText(cardsInHand.get(2).getText());
            taCard4.setText(cardsInHand.get(3).getText());
            taCard5.setText(cardsInHand.get(4).getText());
            taCard6.setText(cardsInHand.get(5).getText());
            taCard7.setText(cardsInHand.get(6).getText());
            taCard8.setText(cardsInHand.get(7).getText());
            taBlackCard.setText(czarCard.getText());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //moet aangeroepen worden wanneer het de turn is van de czar en wanneer een nieuwe beurt begint
    public void updateTurn() {
        Platform.runLater( ()-> {
            this.changeControlVisibility();
            updateScoreBoard();
            deleteChosenCardsUI();
            loadPlayerHand();
        });
    }

    private void changeControlVisibility() {
        switch (StaticPlayer.getPlayer().getRole()) {
            case CZAR:
                this.setCzarRadioButtonsVisibility(true);
                this.setPlebRadioButtonsVisibility(false);
                break;

            case PLEB:
                this.setCzarRadioButtonsVisibility(false);
                this.setPlebRadioButtonsVisibility(true);
                break;

            case SPECTATOR:
                this.setCzarRadioButtonsVisibility(false);
                this.setPlebRadioButtonsVisibility(false);
                break;
                default: break;
        }
    }

    private void setCzarRadioButtonsVisibility(boolean value) {
        hboxCzarSelect.setVisible(value);
    }

    private void setPlebRadioButtonsVisibility(boolean value) {
        hboxPlayerSelect.setVisible(value);
    }

    @Override
    public void onClientMessage(Socket client, MessageType messageType, String message) {
        switch (messageType) {
            case RECEIVE_CARD:
                this.incrementCardRequestCount();

                if (checkCardRequestCount()) {
                    lobby.getGame().newTurn();
                    loadPlayerHand();
                    updateTurn();
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

                //doesn't delete card
                lobby.getPlayers().get(client).removeFromHand(card);

                if (lobby.getGame().addToChosenCards(lobby.getPlayers().get(client), card)) {

                    ArrayList<PlayCard> list = new ArrayList<>();

                    try {
                        list.addAll(lobby.getGame().getChosenCards().values());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        break;
                    }

                    showPlayedCards(list);
                }


                break;
            case INCREASE_POINTS:
                lobby.getGame().czarPicksCards(message);
                updateTurn();
                break;
                default: break;
        }
        updateScoreBoard();
    }

    @Override
    public void onClientJoin(Socket client) {
        // interface method. Currently unused.

    }

    @Override
    public void onClientLeave(Socket client) {
        // interface method. Currently unused.

    }

    @Override
    public void onHostMessage(MessageType messageType, String message) {
        System.out.println("on host");
        switch (messageType) {
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

                break;

            case CHOSEN_CARDS:
                ArrayList<PlayCard> list = new ArrayList<>();
                String[] cards = message.split(",");

                try {
                    for (String string : cards) {
                        int id = Integer.parseInt(string);
                        for (PlayCard card : lobby.getGame().getAllCards()) {
                            if (card.getId() == id) {
                                list.add(card);
                            }
                        }
                    }
                    list.addAll(lobby.getGame().getChosenCards().values());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    break;
                }
                showPlayedCards(list);
                break;

            case GET_ROLE:

                String[] players = message.split(",");
                int i;

                for (Player P : lobby.getPlayers().values()) {
                    for (i = 0; i < players.length; i++) {
                        if (P.getName().equals(players[i])) {
                            i++;
                            Role role = Role.values()[Integer.valueOf(players[i])];
                            P.setRole(role);
                            break;
                        } else {
                            i += 1;
                        }
                    }
                }

                updateTurn();
                break;
            case INCREASE_POINTS: //new piece of code still to be tested

                for (Player p : lobby.getPlayers().values()) {
                    if(p.getName().equals(message))
                    {
                        p.increasePoints();
                    }
                }
                break;
            default: break;
        }
    }

    @Override
    public void onJoin(SocketAddress address) {
        // interface methode. Ongebruikt momenteel

    }

    @Override
    public void onServerClose() {
        // interface methode. Ongebruikt momenteel

    }

    private void updateScoreBoard() {
        Platform.runLater(() -> {
            lvScore.getItems().clear();

            for (Player player : lobby.getPlayers().values()) {
                String role = player.getRole() == Role.CZAR ? "Czar" : "";
                lvScore.getItems().add(player.getName() + ": " + player.getPoints() + "  " + role);
            }
        });
    }
    private void getCardsForHost() {
        List<PlayCard> list = new ArrayList<>();
        list.addAll(lobby.getGame().getChosenCards().values());

        showPlayedCards(list);
    }

    //Laat alle gespeelde kaarten zien op de GUI.
    private void showPlayedCards(List<PlayCard> list){

        try {
            taCzar1.setText(list.get(0).getText());
            taCzar2.setText(list.get(1).getText());
            taCzar3.setText(list.get(2).getText());
            taCzar4.setText(list.get(3).getText());
        }
        catch (IndexOutOfBoundsException ex)
        {
            // do nothing
            // Kunt proberen om een stuk of 4 if's te maken ipv deze try-catch
            // Als je ogen dat aankunnen
            deleteChosenCardsUI();
        }

    }

    private void deleteChosenCardsUI()
    {
        taCzar1.clear();
        taCzar2.clear();
        taCzar3.clear();
        taCzar4.clear();
    }


}
