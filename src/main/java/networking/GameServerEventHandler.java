package networking;

import java.net.Socket;

public class GameServerEventHandler implements GameServerEvents {
    @Override
    public void onClientMessage(Socket client, String message) {
        System.out.println(client.getRemoteSocketAddress() + " : " + message);
    }

    @Override
    public void onClientJoin(Socket client) {

    }

    @Override
    public void onClientLeave(Socket client) {

    }
}
