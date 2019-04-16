package client;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;

@SuppressWarnings("Duplicates")
public class LobbyUser {

    // Initialize all this stuff to null because we'll pass it in the constructor
    private static SpotifyApi spotifyApi = null;
    private static ClientCredentialsRequest clientCredentialsRequest = null;
    private static String playlistId = null;
    private static String playlistUri = null;

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
            //System.out.println("Expires in: " + clientCredentials.getExpiresIn());
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
            Paging<Track> pagingFuture = searchTracksRequest.execute();

            return pagingFuture;
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("searchTracks error: " + e.getCause().getMessage());
        }

        return null;
    }

    public static Paging<PlaylistTrack> getPlayListTracks() {

        final GetPlaylistsTracksRequest getPlaylistsTracksRequest = spotifyApi.getPlaylistsTracks(playlistId).build();

        try {
            Paging<PlaylistTrack> pagingFuture = getPlaylistsTracksRequest.execute();

            return pagingFuture;

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in getPlayListTracks: " + e.getMessage());
        }

        return null;
    }

    public static String setPLaylistUri(String uri) {

        playlistUri = uri;

        int idLength = 0;

        for (int i = playlistUri.length() - 1; i >= 0; i--) {
            if (playlistUri.charAt(i) == ':') {
                idLength = playlistUri.length() - i - 1;
                break;
            }
        }

        char[] array = new char[idLength];

        for (int i = 0; i < idLength; i++) {
            array[i] = playlistUri.charAt(playlistUri.length() - idLength + i);
        }

        playlistId = String.copyValueOf(array);

        return playlistId;
    }

    public static String getPlaylistId() {
        return playlistId;
    }

}
