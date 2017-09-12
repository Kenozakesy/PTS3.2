import Business.Player;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    TextField textField;

    @FXML
    Button button;

    @FXML
    public void logIn(Event e){

        String name = textField.getText();
        Player player = new Player(name);

        if (player.getName() == name) {
            System.out.println("Playername: " + name);
        }


    }








    







}
