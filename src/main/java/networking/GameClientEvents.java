package networking;

import java.net.SocketAddress;

public interface GameClientEvents {
    void onHostMessage(String message);

    void onJoin(SocketAddress address);

    void onServerClose();
}
