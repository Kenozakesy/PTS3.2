import Business.App;
import Business.Lobby;
import org.junit.Test;

/**
 * Created by Gebruiker on 10-10-2017.
 */
public class TestRefrences {

    @Test
    public void Test()
    {
        App app = new App();

        Lobby lobby = app.getLobbyFromId(id);
        lobby.getCardSetsDatabase();

    }
}
