package networking;

import java.net.Socket;

public class GameServerEventHandler implements GameServerEvents {
    @Override
    public void onClientMessage(Socket client, String message) {
        System.out.println(client.getRemoteSocketAddress() + " : " + message);
    }

    @Override
    public void onClientJoin(Socket client) {
        System.out.println(client.getRemoteSocketAddress() + " just joined!");
    }

    @Override
    public void onClientLeave(Socket client) {
        System.out.println(client.getRemoteSocketAddress() + " just left!");
    }
}
