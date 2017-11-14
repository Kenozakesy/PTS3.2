package networking;

import Business.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class ServerClient {
    private ClientHandler clientHandler;
    private ServerClientEvents eventHandler;
    private Player player;

    public ServerClientEvents getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(ServerClientEvents eventHandler) {
        this.eventHandler = eventHandler;
    }

    public Player getPlayer() {
        return player;
    }

    public ServerClient(String hostIP, int port, ServerClientEvents eventHandler, Player player) {
        clientHandler = new ClientHandler(this, hostIP, port);
        this.eventHandler = eventHandler;
        this.player = player;
    }

    public void start() {
        clientHandler.start();
    }

    public void sendMessage(MessageType messageType, String message) {
        clientHandler.sendMessage(messageType, message);
    }

    public void close() {
        clientHandler.close();
    }

    private class ClientHandler extends Thread {
        private ServerClient client;
        private Socket socket;

        private InputStream in;
        private OutputStream out;

        private final String hostIP;
        private final int port;

        private boolean receiveMessages = true;

        ClientHandler(ServerClient client, String hostIP, int port) {
            this.client = client;
            this.hostIP = hostIP;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                this.socket = new Socket();
                this.socket.connect(new InetSocketAddress(hostIP, port));

                this.in = socket.getInputStream();
                this.out = socket.getOutputStream();

                this.client.eventHandler.onJoin(socket.getRemoteSocketAddress());

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
                        byte[] resizedBuffer = Arrays.copyOfRange(buffer, 1, len);

                        MessageType messageType = MessageType.values()[buffer[0]];
                        String message = new String(resizedBuffer, "UTF-8");

                        client.eventHandler.onHostMessage(messageType, message);
                        buffer = new byte[512];
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    this.close();
                }
            }
        }

        private void sendMessage(MessageType messageType, String message) {
            try {
                message = (char) messageType.ordinal() + message;
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
