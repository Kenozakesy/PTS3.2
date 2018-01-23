package networking;

import business.Player;
import business.statics.IntConverter;

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
        private static final String UTF_8 = "UTF-8";

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
                this.close();
            }
        }

        private void readInput() {
            byte[] buffer = new byte[2048];

            while (receiveMessages && !socket.isClosed()) {
                try {
                    int len = in.read(buffer);

                    if (len == -1) this.close();

                    if (len != 0) {
                        byte[] lengthBytes = Arrays.copyOfRange(buffer, 0, 4);
                        int messageLength = IntConverter.byteArrayToInt(lengthBytes) + 4;
                        byte[] messageBuffer = Arrays.copyOfRange(buffer, 5, messageLength);

                        MessageType messageType = MessageType.values()[buffer[4]];
                        String message = new String(messageBuffer, UTF_8);

                        client.eventHandler.onHostMessage(messageType, message);

                        int index = messageLength;

                        while (index != len) {
                            byte[] resizedArray = Arrays.copyOfRange(buffer, index, len);

                            lengthBytes = Arrays.copyOfRange(resizedArray, 0, 4);
                            messageLength = IntConverter.byteArrayToInt(lengthBytes) + 4;

                            messageBuffer = Arrays.copyOfRange(resizedArray, 5, messageLength);

                            messageType = MessageType.values()[resizedArray[4]];
                            message = new String(messageBuffer, UTF_8);

                            client.eventHandler.onHostMessage(messageType, message);

                            index += messageLength;
                        }

                        buffer = new byte[2048];
                    }
                } catch (IOException ex) {
                    this.close();
                }
            }
        }

        private void sendMessage(MessageType messageType, String message) {
            try {
                message = (char) messageType.ordinal() + message;
                message = new String(IntConverter.intToByteArray(message.length()), UTF_8) + message;
                out.write(message.getBytes(UTF_8));
                out.flush();
            } catch (IOException e) {
                //Do nothing
            }
        }

        private void close() {
            try {
                receiveMessages = false;
                this.client.eventHandler.onServerClose();
                this.socket.close();
            } catch (IOException e) {
                //Do nothing
            }
        }
    }
}
