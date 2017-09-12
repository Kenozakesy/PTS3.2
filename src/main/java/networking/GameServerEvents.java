package networking;

import java.net.Socket;

public interface GameServerEvents {

    void onClientMessage(Socket client, String message);
    void onClientJoin(Socket client);
    void onClientLeave(Socket client);
}
