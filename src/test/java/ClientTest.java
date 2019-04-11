import User.Utilities;
import client.Client;
import client.Packet;
import org.junit.Test;

import static org.junit.Assert.*;


public class ClientTest {

    Client client = new Client();

    @Test
    public void clientCreatedTest() {
        assertTrue(client != null);
    }

    @Test
    public void streamConnectionTest() {
        org.junit.Assume.assumeTrue(client.isConnectionEstablished());
        assertTrue(client.isStreamEstablished());
    }

    @Test
    public void sendPacketTest() {
        org.junit.Assume.assumeTrue(client.isConnectionEstablished());
        Packet packet = new Packet(Utilities.generatePacketIdentifier(), 0);
        assertTrue(client.sendPacket(packet));
    }
}