<<<<<<< HEAD:src/main/java/GUI/GameController.java
package GUI;

import business.Lobby;
=======
import Business.Cards;
import Business.Cardset;
import Business.Lobby;
import Business.PlayCard;
>>>>>>> 9768eeb3fbee56974f90db06fb682e0f4735f724:src/main/java/GameController.java
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.smartcardio.Card;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    @FXML
    private Button btnChat;
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

    private Lobby lobby;

    private List<String> chatList = new ArrayList<String>();

    public void click( Event e){
        String chat;

        chat = tbChat.getText();
        chatList.add(chat);

//        for (String line : chatList){
//
//            taChatHistory.setText(taChatHistory.getText() + line);
//        }
    }

    //Zchar chooses a card and a new rounds starts
    public void btnChoose(Event e){
        System.out.println("Kaart gekozen");
    }

    public void btnLeaveGame()
    {
        //goes to different view
        //starts the game with current options
        Stage stage = (Stage) btnLeaveGame.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LobbyView.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1)); stage2.show();
    }

    //whenever a new round start a the cards need to be shuffled again
    public void AddCardsToHand(Event e)
    {
        
    }

    //TODO Turn this test code into actual code
    public void loadPlayerHand()
    {
        Cardset cardset = new Cardset("Saas");
        Cards card = new PlayCard( "Hello I am a card", cardset, false );
        Cards card2 = new PlayCard( "Hello I am a card 2", cardset, false );
        Cards card3 = new PlayCard( "Hello I am a card 3", cardset, false );
        Cards card4 = new PlayCard( "Hello I am a card 4", cardset, false );
        Cards card5 = new PlayCard( "Hello I am a card 5", cardset, false );
        Cards card6 = new PlayCard( "Hello I am a card 6", cardset, false );

        taCard1.setText(card.getText());
        taCard2.setText(card2.getText());
        taCard3.setText(card3.getText());
        taCard4.setText(card4.getText());
        taCard5.setText(card5.getText());
        taCard6.setText(card6.getText());


    }




}
