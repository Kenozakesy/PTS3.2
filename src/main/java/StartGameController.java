import Business.staticClasses.StaticLobby;
import Business.staticClasses.StaticPlayer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gebruiker on 12-9-2017.
 */


public class StartGameController {

    @FXML
    public Button btnStartGame;

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
    private TextArea taChat;

    @FXML
    private TextField tfChatBox;

    @FXML
    private void btnSend(Event e)
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();

        String Text = tfChatBox.getText();
        taChat.appendText(dateFormat.format(date) + " " + StaticPlayer.getName() +": "+ Text + "\n");
        tfChatBox.setText("");
    }

    @FXML
    private void btnChangeNickName(Event e)
    {
        //not implemented
    }

    @FXML
    private void btnLeaveGame(Event e)
    {
        //you leave the game. if you are a host it will delete the game
    }

    @FXML
    private void btnStartGame(Event e) throws Exception
    {
        //goes to different view
        //starts the game with current options
        Stage stage = (Stage) btnStartGame.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1)); stage2.show();
    }

    @FXML
    private void UpdateGameOptions(Event e)
    {
        StaticLobby.setScorelimit(Integer.parseInt((String)ddScorelimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setMaxplayers(Integer.parseInt((String)ddPlayerLimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setMaxspectators(Integer.parseInt((String)ddSpectatorLimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setTimelimit((String)ddIdleTimer.getSelectionModel().getSelectedItem());
        StaticLobby.setBlankcards(Integer.parseInt((String)ddBlankCards.getSelectionModel().getSelectedItem()));

       // only used for testing
//        taChat.setText(String.valueOf(StaticLobby.getScorelimit()) + "\n" +
//                        String.valueOf(StaticLobby.getMaxplayers()) + "\n" +
//                        String.valueOf(StaticLobby.getMaxspectators()) + "\n" +
//                        String.valueOf(StaticLobby.getTimelimit()) + "\n" +
//                        String.valueOf(StaticLobby.getBlankcards()));
    }
}
