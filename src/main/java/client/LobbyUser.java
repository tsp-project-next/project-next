package client;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    public void refreshAccessToken() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            // Debug
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in LobbyUser Constructor: " + e.getMessage());
        }
    }

    // Returns the wrapper data structure for holding URI data with top 10 results
    public Paging<Track> searchTracks(String toSearch)
    {
        final SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(toSearch)
                .market(CountryCode.US)
                .limit(10)
                .build();

        try {
            final Future<Paging<Track>> pagingFuture = searchTracksRequest.executeAsync();

            final Paging<Track> trackPaging = pagingFuture.get();

            return trackPaging;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("searchTracks error: " + e.getCause().getMessage());
        }

        return null;
    }
}
