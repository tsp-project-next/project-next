import client.Client;
import org.junit.Test;

import static org.junit.Assert.*;


public class ClientTest {

    Client client;

    @Test
    public void fakeTest() {
        client = new Client();

        assertEquals("test", client.fakeTest());
        //fail("This should fail");
    }
}