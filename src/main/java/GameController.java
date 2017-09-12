import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GameController {
    @FXML
    Button BtnChat;

    @FXML
    Button BtnChoose;

    @FXML
    TextField TbChat;

    public void click( Event e){
        System.out.println("Test");
    }
}
