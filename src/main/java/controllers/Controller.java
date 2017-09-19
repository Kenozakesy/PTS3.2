package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import networking.GameClient;
import networking.GameClientEventHandler;
import networking.GameHost;
import networking.GameServerEventHandler;

import java.io.IOException;

public class Controller  {

    private GameHost host;
    private GameClient client;

    @FXML
    private TextField textInput;

    public void btnHost_Clicked() {
        try {
            host = new GameHost(2, new GameServerEventHandler());
            host.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnClient_Clicked() {
        client = new GameClient("145.93.133.77", new GameClientEventHandler());
        client.start();
    }

    public void btnSend_Clicked() {
        if (host != null) {
            host.messageAll(textInput.getText());
        } else if (client != null) {
            client.sendMessage(textInput.getText());
        }
    }

    public void btnClose_Clicked() {
        if (host != null) {
            host.close();
        } else if (client != null) {
            client.close();
        }
    }
}
