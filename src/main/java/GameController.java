import Business.*;
import Business.Enums.Role;
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

public class GameController implements Initializable{
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

    private Lobby lobby;

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    private List<String> chatList = new ArrayList<String>();

    public void initialize(URL location, ResourceBundle resources) {
        loadPlayerHand();
    }

    @FXML
    public void btnSend( Event e){
        String chat;

        chat = tbChat.getText();
        chatList.add(chat);
    }

    @FXML   //Czar chooses a card and a new rounds starts
    public void btnChoose(Event e){

        boolean check = false;
        if(StaticPlayer.getPlayer().getRole().equals(Role.Pleb)) {

            //hier moet een betere manier voor zijn
            if (rbtnCard1.isSelected())
            {
                lobby.getGame().playerPicksCard(0, new Player(StaticPlayer.getPlayer().getName()));
            }
            else if (rbtnCard2.isSelected())
            {
                lobby.getGame().playerPicksCard(1, new Player(StaticPlayer.getPlayer().getName()));
            }
            else if (rbtnCard3.isSelected())
            {
                lobby.getGame().playerPicksCard(2, new Player(StaticPlayer.getPlayer().getName()));
            }
            else if (rbtnCard4.isSelected())
            {
                lobby.getGame().playerPicksCard(3, new Player(StaticPlayer.getPlayer().getName()));
            }
            else if (rbtnCard5.isSelected())
            {
                lobby.getGame().playerPicksCard(4, new Player(StaticPlayer.getPlayer().getName()));
            }
            else if (rbtnCard6.isSelected())
            {
                lobby.getGame().playerPicksCard(5, new Player(StaticPlayer.getPlayer().getName()));
            }
            else if (rbtnCard7.isSelected())
            {
                lobby.getGame().playerPicksCard(6, new Player(StaticPlayer.getPlayer().getName()));
            }
            else if (rbtnCard8.isSelected())
            {
                lobby.getGame().playerPicksCard(7, new Player(StaticPlayer.getPlayer().getName()));
            }
            //methode om UI te disabelen
        }
        else if(StaticPlayer.getPlayer().getRole().equals(Role.Czar))
        {
            if(rbCzarPick1.isSelected())
            {
                String cardtext = taCzar1.getText();
                lobby.getGame().czarPicksCards(cardtext);
            }
            else if(rbCzarPick2.isSelected())
            {
                String cardtext = taCzar2.getText();
                lobby.getGame().czarPicksCards(cardtext);
            }
            else if(rbCzarPick3.isSelected())
            {
                String cardtext = taCzar3.getText();
                lobby.getGame().czarPicksCards(cardtext);
            }
            else if(rbCzarPick4.isSelected())
            {
                String cardtext = taCzar4.getText();
                lobby.getGame().czarPicksCards(cardtext);
            }
        }
        else
        {
            //Do nothing
        }
    }

    public void btnLeaveGame()
    {
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
        stage2.setScene(new Scene(root1)); stage2.show();
    }

    //TODO Turn this test code into actual code
    public void loadPlayerHand()
    {
        List<PlayCard> list = StaticPlayer.getPlayer().getCardsInHand();
        CzarCard czarCard = lobby.getGame().pickBlackCard();

        taCard1.setText(list.get(0).getText());
        taCard2.setText(list.get(1).getText());
        taCard3.setText(list.get(2).getText());
        taCard4.setText(list.get(3).getText());
        taCard5.setText(list.get(4).getText());
        taCard6.setText(list.get(5).getText());
        taCard7.setText(list.get(6).getText());
        taCard8.setText(list.get(7).getText());
        taBlackCard.setText(czarCard.getText());
    }

    //moet aangeroepen worden wanneer het de turn is van de czar en wanneer een nieuwe beurt begint
    public void updateTurn()
    {
        Player player = new Player("test");
        if(player.getRole() == Role.Czar)
        {

        }
        else if(player.getRole() == Role.Pleb)
        {

        }
    }
}
