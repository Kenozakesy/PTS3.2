package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GameClient {
    private ClientHandler clientHandler;
    private GameClientEvents eventHandler;

    public GameClient(String hostIP, GameClientEvents eventHandler) {
        clientHandler = new ClientHandler(this, hostIP);
        this.eventHandler = eventHandler;
    }

    public void start() {
        clientHandler.start();
    }

    public void sendMessage(String message) {
        clientHandler.sendMessage(message);
    }

    public void close() {
        clientHandler.close();
    }

    private class ClientHandler extends Thread {
        private GameClient client;
        private Socket socket;

        private InputStream in;
        private OutputStream out;

        private final String hostIP;

        private boolean receiveMessages = true;

        ClientHandler(GameClient client, String hostIP) {
            this.client = client;
            this.hostIP = hostIP;
        }

        @Override
        public void run() {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(hostIP, 1337));

                client.eventHandler.onJoin(socket.getRemoteSocketAddress());

                in = socket.getInputStream();
                out = socket.getOutputStream();

                this.readInput();
            } catch (IOException e) {
                e.printStackTrace();
                this.close();
            }
        }

        private void readInput() {
            byte[] buffer = new byte[512];

            while (receiveMessages && !socket.isClosed()) {
                try {
                    int len = in.read(buffer);

                    if (len == -1) this.close();

                    if (len != 0) {
                        client.eventHandler.onHostMessage(new String(buffer, "UTF-8"));
                        buffer = new byte[512];
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    this.close();
                }
            }
        }

        private void sendMessage(String message) {
            try {
                out.write(message.getBytes("UTF-8"));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void close() {
            try {
                receiveMessages = false;
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not close client.");
            }
        }
    }
}
