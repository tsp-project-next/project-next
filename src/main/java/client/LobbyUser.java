package client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;

public class LobbyUser {

    // Initialize all this stuff to null because we'll pass it in the constructor
    private static SpotifyApi spotifyApi = null;
    private static ClientCredentialsRequest clientCredentialsRequest = null;

    public LobbyUser(String clientId, String clientSecret)
    {
        // We initialize in the constructor so we only need to change things in our main class for
        // change reflection
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();

        clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();

        refreshAccessToken();
    }

    // Initialize as well as refresh our client access token in case we need to refresh.
    private void refreshAccessToken() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            // Debug
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in LobbyUser Constructor: " + e.getMessage());
        }
    }
}
