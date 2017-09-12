import networking.GameHost;
import networking.GameServerEventHandler;

import java.io.IOException;

public class Controller {

    public void btnHost_Clicked() {
        try {
            GameHost host = new GameHost(2, new GameServerEventHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnClient_Clicked() {
    }
}
