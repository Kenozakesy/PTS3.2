package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient {
    private ClientHandler clientHandler;

    public GameClient() {
        clientHandler = new ClientHandler(this);
    }

    private class ClientHandler extends Thread {
        private GameClient client;
        private Socket socket;

        ClientHandler(GameClient client) {
            this.client = client;
            try {
                socket = new Socket(InetAddress.getLocalHost(), 1338);
                socket.
            } catch (IOException e) {
                e.printStackTrace();
                client.clientHandler = null;
            }
        }

        @Override
        public void run() {
            super.run();


        }
    }
}
