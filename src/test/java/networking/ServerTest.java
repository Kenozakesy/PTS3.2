package networking;

import Business.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class ServerTest implements ServerHostEvents, ServerClientEvents {

    private ServerHost host;

    @Before
    public void initHost() {
        try {
            if (host != null) host.close();
            host = new ServerHost(5, this);
            host.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void clientConnectTest() {
        ServerClient client = new ServerClient("localhost", 1337, this, new Player("Jan"));
        client.start();

        Assert.assertNotNull(client);
        Assert.assertEquals("Jan", client.getPlayer().getName());
    }

    @Override
    public void onClientMessage(Socket client, MessageType messageType, String message) {

    }

    @Override
    public void onClientJoin(Socket client) {

    }

    @Override
    public void onClientLeave(Socket client) {

    }

    @Override
    public void onHostMessage(MessageType messageType, String message) {

    }

    @Override
    public void onJoin(SocketAddress address) {

    }

    @Override
    public void onServerClose() {

    }
}
