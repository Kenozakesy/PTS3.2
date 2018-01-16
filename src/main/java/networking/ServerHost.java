package networking;

import business.statics.IntConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServerHost {
    private final ServerSocket server;
    private final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private final ClientAcceptor acceptor;
    private final int maxPlayers;
    private ServerHostEvents eventHandler;
    private final Object playerLockObj = new Object();

    public ServerHostEvents getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(ServerHostEvents eventHandler) {
        this.eventHandler = eventHandler;
    }

    private synchronized void removeClient(ClientHandler handler) {
        clients.remove(handler);
    }

    public ServerHost(int maxPlayers, ServerHostEvents eventHandler) throws IOException {
        this.eventHandler = eventHandler;
        this.maxPlayers = maxPlayers;
        server = new ServerSocket(1337);
        acceptor = new ClientAcceptor(this);
    }

    public void start() {
        acceptor.start();
    }

    public void stopAccepting() {
        acceptor.acceptClients = false;
    }

    public void close() throws IOException {
        this.stopAccepting();
        this.server.close();

        for (ClientHandler client : clients) {
            client.close();
        }
    }

    public void messageAll(MessageType messageType, String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(messageType, message);
        }
    }

    public void messageClient(Socket client, MessageType messageType, String message) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.client.equals(client)) {
                clientHandler.sendMessage(messageType, message);
            }
        }
    }

    /**
     * Runs concurrently to the main thread so it can accept incoming clients.
     */
    private class ClientAcceptor extends Thread {
        private final ServerHost host;
        private boolean acceptClients = true;

        ClientAcceptor(ServerHost host) {
            this.host = host;
        }

        @Override
        public void run() {
            synchronized (playerLockObj) {
                while (acceptClients) {
                    try {
                        if (clients.size() == maxPlayers) playerLockObj.wait();

                        Socket client = host.server.accept();
                        host.eventHandler.onClientJoin(client);

                        ClientHandler handler = new ClientHandler(host, client);
                        host.clients.add(handler);
                        handler.start();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }

    /**
     * Every client runs concurrently to the main thread allowing them to communicate with the host.
     */
    private class ClientHandler extends Thread {
        private final ServerHost host;
        private final Socket client;

        private final InputStream in;
        private final OutputStream out;

        private static final String UTF_8 = "UTF-8";
        private boolean receiveMessages = true;

        ClientHandler(ServerHost host, Socket client) throws IOException {
            this.host = host;
            this.client = client;

            in = client.getInputStream();
            out = client.getOutputStream();
        }

        @Override
        public void run() {
            byte[] buffer = new byte[512];

            while (receiveMessages && !client.isClosed()) {
                try {
                    int len = in.read(buffer);

                    if (len == -1) this.close();

                    if (len != 0) {
                        byte[] lengthBytes = Arrays.copyOfRange(buffer, 0, 4);
                        int messageLength = IntConverter.byteArrayToInt(lengthBytes) + 4;

                        byte[] messageBuffer = Arrays.copyOfRange(buffer, 5, messageLength);

                        MessageType messageType = MessageType.values()[buffer[4]];
                        String message = new String(messageBuffer, UTF_8);

                        host.eventHandler.onClientMessage(client, messageType, message);

                        int index = messageLength;

                        while (index != len) {
                            byte[] resizedArray = Arrays.copyOfRange(buffer, index, len);

                            lengthBytes = Arrays.copyOfRange(resizedArray, 0, 4);
                            messageLength = IntConverter.byteArrayToInt(lengthBytes) + 4;

                            messageBuffer = Arrays.copyOfRange(resizedArray, 5, messageLength);

                            messageType = MessageType.values()[resizedArray[4]];
                            message = new String(messageBuffer, UTF_8);

                            host.eventHandler.onClientMessage(client, messageType, message);

                            index += messageLength;
                        }

                        buffer = new byte[512];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.close();
                }
            }
        }

        void sendMessage(MessageType messageType, String message) {
            try {
                message = (char) messageType.ordinal() + message;
                message = new String(IntConverter.intToByteArray(message.length()), UTF_8) + message;
                out.write(message.getBytes("UTF-8"));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                this.close();
            }
        }

        private void close() {
            try {
                host.removeClient(this);
                host.eventHandler.onClientLeave(client);
                this.receiveMessages = false;
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not close client: " + client.getRemoteSocketAddress());
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ClientHandler that = (ClientHandler) o;

            return client.equals(that.client);
        }

        @Override
        public int hashCode() {
            return client.hashCode();
        }
    }
}
