import Business.*;
import Business.staticClasses.StaticPlayer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static Business.staticClasses.StaticPlayer.getPlayer;

public class GameController implements Initializable{
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

    public void initialize(URL location, ResourceBundle resources) {
        loadPlayerHand();
    }

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
        List<PlayCard> list = StaticPlayer.getPlayer().getCardsInHand();
        CzarCard czarCard = lobby.getGame().pickBlackCard();

        Cardset cardset = new Cardset("Saas");
        Cards card = new PlayCard( "Hello I am a card", cardset, false );
        Cards card2 = new PlayCard( "Hello I am a card 2", cardset, false );
        Cards card3 = new PlayCard( "Hello I am a card 3", cardset, false );
        Cards card4 = new PlayCard( "Hello I am a card 4", cardset, false );
        Cards card5 = new PlayCard( "Hello I am a card 5", cardset, false );
        Cards card6 = new PlayCard( "Hello I am a card 6", cardset, false );
        Cards black = new CzarCard("I am black and addicted to __", cardset , 1);

        taCard1.setText(list.get(0).getText());
        taCard2.setText(list.get(1).getText());
        taCard3.setText(list.get(2).getText());
        taCard4.setText(list.get(3).getText());
        taCard5.setText(list.get(4).getText());
        taCard6.setText(list.get(5).getText());
        taBlackCard.setText(czarCard.getText());


    }




}
