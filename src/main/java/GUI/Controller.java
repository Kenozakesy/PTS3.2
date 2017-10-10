package GUI;

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

public class Controller {

    @FXML
    TextField textField;

    @FXML
    Button button;

    @FXML
    public void logIn(Event e){

        String name = textField.getText();
        Player player = new Player(name);

        StaticPlayer.setName(name);

        if (player.getName() == name) {
            System.out.println("Playername: " + name);
        }

        //goes to different view
        //starts the game with current options
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LobbyView.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root1));
        stage2.show();
    }
}
