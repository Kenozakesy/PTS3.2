import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    @FXML
    Button btnChat;
    @FXML
    Button btnChoose;
    @FXML
    ListView lvPlayers;
    @FXML
    RadioButton rbtnCard1;
    @FXML
    RadioButton rbtnCard2;
    @FXML
    RadioButton rbtnCard3;
    @FXML
    RadioButton rbtnCard4;
    @FXML
    RadioButton rbtnCard5;
    @FXML
    RadioButton rbtnCard6;
    @FXML
    RadioButton rbtnCard7;
    @FXML
    RadioButton rbtnCard8;
    @FXML
    TextArea taCard1;
    @FXML
    TextArea taCard2;
    @FXML
    TextArea taCard3;
    @FXML
    TextArea taCard4;
    @FXML
    TextArea taCard5;
    @FXML
    TextArea taCard6;
    @FXML
    TextArea taCard7;
    @FXML
    TextArea taCard8;
    @FXML
    TextArea taBlackCard;
    @FXML
    TextArea taChatHistory;
    @FXML
    TextField tbChat;

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

    public void choose(Event e){
        System.out.println("Kaart gekozen");
    }


}
