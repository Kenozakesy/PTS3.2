import Business.Player;
import Business.staticClasses.StaticPlayer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField textField;

    @FXML
    Button button;

    @FXML
    public void logIn(Event e) {

        String name = textField.getText();

        StaticPlayer.initializePlayer();
        StaticPlayer.getPlayer().setName(name);

        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LobbyView.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (root1 != null) {
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root1));
            stage2.show();
        }
    }
}
