import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by Gebruiker on 12-9-2017.
 */


public class StartGameController {

    @FXML
    private ComboBox ddScoreLimit;

    @FXML
    private ComboBox ddPlayerLimit;

    @FXML
    private ComboBox ddSpectatorLimit;

    @FXML
    private ComboBox ddIdleTimer;

    @FXML
    private ComboBox ddBlankCards;

    @FXML
    private TextArea taChat;

    @FXML
    private TextField tfChatBox;

    @FXML
    private void btnSend(Event e)
    {
        //send chat messages
    }

    @FXML
    private void btnChangeNickName(Event e)
    {
        //changes your current nickname
    }

    @FXML
    private void btnLeaveGame(Event e)
    {
        //you leave the game. if you are a host it will delete the game
    }

    @FXML
    private void btnStartGame(Event e)
    {
        //starts the game with current options
    }
}
