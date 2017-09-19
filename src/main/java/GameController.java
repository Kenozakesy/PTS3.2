import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    @FXML
    Button BtnChat;
    @FXML
    Button BtnChoose;
    @FXML
    ListView LvPlayers;
    @FXML
    RadioButton RbtnCard1;
    @FXML
    RadioButton RbtnCard2;
    @FXML
    RadioButton RbtnCard3;
    @FXML
    RadioButton RbtnCard4;
    @FXML
    RadioButton RbtnCard5;
    @FXML
    RadioButton RbtnCard6;
    @FXML
    RadioButton RbtnCard7;
    @FXML
    RadioButton RbtnCard8;
    @FXML
    TextArea TaCard1;
    @FXML
    TextArea TaCard2;
    @FXML
    TextArea TaCard3;
    @FXML
    TextArea TaCard4;
    @FXML
    TextArea TaCard5;
    @FXML
    TextArea TaCard6;
    @FXML
    TextArea TaCard7;
    @FXML
    TextArea TaCard8;
    @FXML
    TextArea TaBlackCard;
    @FXML
    TextArea TaChatHistory;
    @FXML
    TextField TbChat;

    List<String> chatList = new ArrayList<String>();

    public void click( Event e){
        String chat;

        chat = TbChat.getText();
        chatList.add(chat);

//        for (String line : chatList){
//
//            TaChatHistory.setText(TaChatHistory.getText() + line);
//        }
    }

    public void Choose(Event e){
        System.out.println("Kaart gekozen");
    }


}
