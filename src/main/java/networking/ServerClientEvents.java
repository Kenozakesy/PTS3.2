package networking;

import java.net.SocketAddress;

public interface ServerClientEvents {
    void onHostMessage(MessageType messageType, String message);

    void onJoin(SocketAddress address);

    void onServerClose();
}
