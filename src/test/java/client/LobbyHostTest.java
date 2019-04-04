package client;

import com.wrapper.spotify.SpotifyHttpManager;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * @author: Connor Mulcahy
 * Class: CS1131
 * Date: 4/4/19
 * Time: 4:09 PM
 * Project Name: projectnext-gradle
 * File Name: LobbyHostTest
 */
public class LobbyHostTest {

    LobbyHost host;

    @Before
    public void setup() {
        String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
        String clientSecret = "f32ba2821de9409785f1abb637707170";
        URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");

        host = new LobbyHost(clientId, clientSecret, redirectUri);
    }

    @Test
    public void setAuthCode() {
        // This is a valid string that we would be testing against, pulled directly from the OAuth response flow
        String code = "https://tsp-project-next.github.io/?code=AQCFrqjLWVd5K2XP5t2rG7OvRWgZ4mZcwLSRj63Gve-7ELMtHho5VEhdaqOz19dVyrq89X41SUpRWYd6gNhs8ZyVutRguCkiJeJniFIy63GnjpHAto-SGO4zZ-geAa0VgvHsp6B3tBFCHrmUn0zDIbkPoO53aXC9YWEfnwujVsuWpD-BBJaVgRzKfYVSk7qn51V6GcSe5nY9zwmRODrr2Glmk8ccSx0VEXsnDYC7JHOwJfWwFf1aFmsssAmQ201hoypjwmOVMmIjLh7IPVi80Gi9BgNt05Ba4UCQXyay_QP5EQCK17qvlNmC29zC2Ml_SVC9xWO8D3oIhVXsQFMJ1d8fMz9LIor9VsEQhg4A1CYm6Z-587696NP6OQpdR6uGQlqQU8OjJPyB353LXvImByRrcHH8lP-JuWP1g1P0rUX6Xhb77m8BCv2p35aQEoh9IYeHrD0b9_hAbxuC_8vi3NwGZshDzAP_-SheSP8rVDYXpA&state=thisisouruniqueidentifier";
        String codeParsed = "AQCFrqjLWVd5K2XP5t2rG7OvRWgZ4mZcwLSRj63Gve-7ELMtHho5VEhdaqOz19dVyrq89X41SUpRWYd6gNhs8ZyVutRguCkiJeJniFIy63GnjpHAto-SGO4zZ-geAa0VgvHsp6B3tBFCHrmUn0zDIbkPoO53aXC9YWEfnwujVsuWpD-BBJaVgRzKfYVSk7qn51V6GcSe5nY9zwmRODrr2Glmk8ccSx0VEXsnDYC7JHOwJfWwFf1aFmsssAmQ201hoypjwmOVMmIjLh7IPVi80Gi9BgNt05Ba4UCQXyay_QP5EQCK17qvlNmC29zC2Ml_SVC9xWO8D3oIhVXsQFMJ1d8fMz9LIor9VsEQhg4A1CYm6Z-587696NP6OQpdR6uGQlqQU8OjJPyB353LXvImByRrcHH8lP-JuWP1g1P0rUX6Xhb77m8BCv2p35aQEoh9IYeHrD0b9_hAbxuC_8vi3NwGZshDzAP_-SheSP8rVDYXpA";

        String response = host.setAuthCode(code);

        if (!response.equals(codeParsed))
            fail("Authorization code was not parsed correctly");
    }
}