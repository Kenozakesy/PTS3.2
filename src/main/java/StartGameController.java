import Business.Cardset;
import Business.staticClasses.StaticLobby;
import Business.staticClasses.StaticPlayer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import networking.GameClient;
import networking.GameHost;
import networking.GameServerEvents;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Gebruiker on 12-9-2017.
 */


public class StartGameController implements Initializable, GameServerEvents {

    @FXML
    public Button btnStartGame;

    @FXML
    private ListView lvPickedCards;

    @FXML
    private ListView lvCardsets;

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

    private GameHost host;
    private GameClient client;

    private Stage previousStage;

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    private ArrayList<Cardset> Cardsets = null;
    private ArrayList<Cardset> CardsetsPicked = null;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.host = new GameHost(6, this);
            host.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.sendMessage("<L>" + StaticPlayer.getName() + ";" + InetAddress.getLocalHost().getHostAddress() + "</L>");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Cardsets = new ArrayList<Cardset>();
        CardsetsPicked = new ArrayList<Cardset>();

        //for loop is for testing
        for (int x = 0; x < 10; x++) {
            Cardsets.add(new Cardset(x, "Test" + x));
        }
        Update();
    }

    @FXML
    private void btnRight(Event e) {
        if (lvCardsets.getSelectionModel().getSelectedItem() != null) {
            Cardset set = (Cardset) lvCardsets.getSelectionModel().getSelectedItem();
            Cardsets.remove(set);
            CardsetsPicked.add(set);
            Update();
        }
    }

    @FXML
    private void btnLeft(Event e) {
        if (lvPickedCards.getSelectionModel().getSelectedItem() != null) {
            Cardset set = (Cardset) lvPickedCards.getSelectionModel().getSelectedItem();
            CardsetsPicked.remove(set);
            Cardsets.add(set);
            Update();
        }
    }

    @FXML
    private void btnSend(Event e) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();

        String Text = tfChatBox.getText();
        taChat.appendText(dateFormat.format(date) + " " + StaticPlayer.getName() + ": " + Text + "\n");
        tfChatBox.setText("");
    }

    @FXML
    private void btnChangeNickName(Event e) {
        //not implemented
    }

    @FXML
    private void btnLeaveGame(Event e) //not correct as of now
    {
        try {
            //goes to different view
            //starts the game with current options
            Stage stage = (Stage) btnStartGame.getScene().getWindow();
            stage.close();

            client.sendMessage("<L>quit</L>");
            host.close();

            previousStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    private void btnStartGame(Event e) throws Exception {
        //goes to different view
        //starts the game with current options
        Stage stage = (Stage) btnStartGame.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1));
        stage2.show();
    }

    @FXML
    private void UpdateGameOptions(Event e) {
        StaticLobby.setScorelimit(Integer.parseInt((String) ddScorelimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setMaxplayers(Integer.parseInt((String) ddPlayerLimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setMaxspectators(Integer.parseInt((String) ddSpectatorLimit.getSelectionModel().getSelectedItem()));
        StaticLobby.setTimelimit((String) ddIdleTimer.getSelectionModel().getSelectedItem());
        StaticLobby.setBlankcards(Integer.parseInt((String) ddBlankCards.getSelectionModel().getSelectedItem()));

        // only used for testing
//        taChat.setText(String.valueOf(StaticLobby.getScorelimit()) + "\n" +
//                        String.valueOf(StaticLobby.getMaxplayers()) + "\n" +
//                        String.valueOf(StaticLobby.getMaxspectators()) + "\n" +
//                        String.valueOf(StaticLobby.getTimelimit()) + "\n" +
//                        String.valueOf(StaticLobby.getBlankcards()));
    }

    public void Update() {
        lvPickedCards.getItems().clear();
        lvCardsets.getItems().clear();

        for (Cardset C : Cardsets) {
            lvCardsets.getItems().add(C);
        }
        for (Cardset C : CardsetsPicked) {
            lvPickedCards.getItems().add(C);
        }
    }


    @Override
    public void onClientMessage(Socket client, String message) {

    }

    @Override
    public void onClientJoin(Socket client) {

    }

    @Override
    public void onClientLeave(Socket client) {

    }
}
