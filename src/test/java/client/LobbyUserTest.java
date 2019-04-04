package client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author: Connor Mulcahy
 * Class: CS1131
 * Date: 4/4/19
 * Time: 4:07 PM
 * Project Name: projectnext-gradle
 * File Name: LobbyUserTest
 */
public class LobbyUserTest {

    LobbyUser user;

    @Before
    public void setup() {
        String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
        String clientSecret = "f32ba2821de9409785f1abb637707170";

        user = new LobbyUser(clientId, clientSecret);
    }

    @Test
    public void setPLaylistUri() {
        String playlistUri = "spotify:album:6XhjNHCyCDyyGJRM5mg40G";
        String playlistId = "6XhjNHCyCDyyGJRM5mg40G";

        String idResponse = user.setPLaylistUri(playlistUri);

        if (!idResponse.equals(playlistId))
            fail("Parsing playlist id from URI in user was incorrect");
    }
}