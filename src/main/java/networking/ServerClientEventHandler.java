package networking;

import java.net.SocketAddress;

public class ServerClientEventHandler implements ServerClientEvents {
    @Override
    public void onHostMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void onJoin(SocketAddress address) {
        System.out.println("Joined server at " + address);
    }

    @Override
    public void onServerClose() {
        System.out.println("The server has closed!");
    }
}
