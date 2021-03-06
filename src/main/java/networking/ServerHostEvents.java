package networking;

import java.net.Socket;

public interface ServerHostEvents {

    void onClientMessage(Socket client, MessageType messageType, String message);
    void onClientJoin(Socket client);
    void onClientLeave(Socket client);
}
