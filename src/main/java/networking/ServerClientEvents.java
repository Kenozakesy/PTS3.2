package networking;

import java.net.SocketAddress;

public interface ServerClientEvents {
    void onHostMessage(String message);

    void onJoin(SocketAddress address);

    void onServerClose();
}
