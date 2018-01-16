package ExceptionTest;

import business.exceptions.AlreadyHostingException;
import business.exceptions.NotClientException;
import business.exceptions.NotHostException;
import org.junit.Test;

/**
 * Created by Gebruiker on 16-1-2018.
 */
public class Exceptions {


    @Test (expected = AlreadyHostingException.class)
    public void AlreadyHostingExceptionTest() throws AlreadyHostingException {
        throw new AlreadyHostingException();
    }

    @Test (expected = NotClientException.class)
    public void NotClientExceptionTestTest() throws NotClientException {
        throw new NotClientException();
    }

    @Test (expected = NotHostException.class)
    public void NotHostExceptionTest() throws NotHostException {
        throw new NotHostException();
    }
}
