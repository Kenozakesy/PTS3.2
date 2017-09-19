package networking;

import java.net.SocketAddress;

public class GameClientEventHandler implements GameClientEvents {
    @Override
    public void onHostMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onJoin(SocketAddress address) {
        System.out.println("Joined server at " + address);
    }
}
