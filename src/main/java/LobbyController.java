import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import networking.GameClient;
import networking.GameClientEvents;

import java.net.SocketAddress;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Gebruiker on 26-9-2017.
 */
public class LobbyController implements Initializable, GameClientEvents{

    private GameClient client;

    @FXML
    private Button btnSend;

    @FXML
    private TextField tfSend;

    @FXML
    private ListView lvChat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new GameClient("145.93.133.180", 1336, this);
        client.start();
    }

    @FXML
    public void btnSend()
    {
        String text = tfSend.getText();
        //lvChat.getItems().add(text);
        client.sendMessage(text);
    }

     
    @Override
    public void onHostMessage(String message) {
        Platform.runLater(() -> {
        lvChat.getItems().add(message);
        });
    }

    @Override
    public void onJoin(SocketAddress address) {

    }

    @Override
    public void onServerClose() {

    }
}
