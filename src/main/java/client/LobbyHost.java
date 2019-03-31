package client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import com.wrapper.spotify.model_objects.specification.User;
import User.Utilities;
import User.UserInterface;

import java.awt.*;
import java.io.IOException;
import java.net.URI;


public class LobbyHost {

    private static SpotifyApi spotifyApi = null;
    private static AuthorizationCodeUriRequest authorizationCodeUriRequest = null;

    // We might not need this?
    private static String code = "";
    private static String userId = "";
    private static String playlistUri = "";
    private static String playlistId = "";
    private static String deviceId = "";
    private static AuthorizationCodeRequest authorizationCodeRequest = null;

    private static int currentSongProgress = 0;

    // Constructor to build our initial API requests so we can begin to make requests like generating URIs
    // and initializing our scope for authorization
    public LobbyHost(String clientId, String clientSecret, URI redirectURI) {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURI)
                .build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("thisisouruniqueidentifier")
                .scope("playlist-modify-public,playlist-modify-private,playlist-read-private,playlist-read-collaborative,app-remote-control,user-read-currently-playing,streaming,user-read-playback-state,user-modify-playback-state")
                .show_dialog(true)
                .build();

        try {
            generateURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // We use this to build a desktop authorization URI to gain spotify authorization privileges for
    // a specific user
    public static String generateURI() throws Exception {
        final URI uri = authorizationCodeUriRequest.execute();

        // This is where we'd prompt the user with the URI to allow us to query specific account info
        System.out.println("URI: " + uri.toString());

        Desktop desktop = Desktop.getDesktop();

        desktop.browse(uri);

        return uri.toString();
    }

    public static String getPlaylistURI() {
        return playlistUri;
    }

    // After a user navigates the URI and authorizes us manually for account access,
    // we call this method to build our API to make requests on behalf of the user account
    public static boolean authorizationCode_Sync() {
        authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        //System.out.println("Current Code: " + code + "\n");

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());

            return true;
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in authorizationCode_Sync() Lobby Host: " + e.getMessage());

            return false;
        }
    }

    // Parse the authorization code from given OAuth URL so we can successfully create our request methods
    public static void setAuthCode(String authCode) {
        //System.out.println(authCode);

        int start = 0, finish = 0;

        for (int i = 0; i < authCode.length(); i++)
        {
            if (authCode.charAt(i) == '?') {
                if (authCode.charAt(i + 1) == 'c') {
                    if (authCode.charAt(i + 2) == 'o') {
                        if (authCode.charAt(i + 3) == 'd') {
                            if (authCode.charAt(i + 4) == 'e')
                                start = i + 6;
                        }
                    }
                }
            }
        }

        for (int i = start; i < authCode.length(); i++)
        {
            if (authCode.charAt(i) == '&')
                finish = i;
        }

        authCode = authCode.substring(start, finish);

        //System.out.println(authCode);

        code = authCode;
    }

    // Create a playlist and instantly unfollow it so that we can keep direct progress hidden from the user
    // This allows us to do as much as we might need to without creating a noticeable footprint on the
    // user's spotify account
    public static void createPlaylist() {
        GetCurrentUsersProfileRequest profileRequest = spotifyApi.getCurrentUsersProfile().build();

        try {
            final User user = profileRequest.execute();

            userId = user.getId();

            CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, "queue")
                    .collaborative(false)
                    .public_(false)
                    .description("Bot queue")
                    .build();

            final Playlist playlist = createPlaylistRequest.execute();

            System.out.println("Name: " + playlist.getName() + "\n");
            System.out.println("URI: " + playlist.getUri() + "\n");

            playlistUri = playlist.getUri();
            playlistId = playlist.getId();

            playlistCleanup();

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in createPlaylist: " + e.getMessage());
        }
    }

    // Unfollow the playlist, we can't delete playlists so this helps us keep the user's account clear
    public static void playlistCleanup() {
        try {
            spotifyApi.unfollowPlaylist(userId, playlistId).build().execute();
            System.out.println("Unfollowed playlist");
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in playlistCleanup: " + e.getMessage());
        }
    }

    // Return the stored playlist URI so that the user can save it manually
    public static String playlistExport() {
        return playlistUri;
    }

    // List available playback devices for the authorized user account, determine which are active
    public static Device[] getDevices() {
        try {
            final Device[] devices = spotifyApi.getUsersAvailableDevices().build().execute();

            for (Device d : devices)
            {
                System.out.println("Device name: " + d.getName());
                System.out.println("Is active: " + d.getIs_active() + "\n");

                if (d.getIs_active())
                    deviceId = d.getId();
            }

            return devices;
        } catch (IOException | SpotifyWebApiException e) {
            System.out.print("Error in getDevices: " + e.getMessage());

            return null;
        }
    }

    // Add songs to the stored playlist
    public static void addSong(String[] songUri) {
        try {
            spotifyApi.addTracksToPlaylist(playlistId, songUri).position(spotifyApi.getPlaylistsTracks(playlistId).build().execute().getTotal()).build().execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in addSong: " + e.getMessage());
        }
    }

    // Begin playback from the beginning of the stored playlist
    public static void startPlaylist() {
        try {
            spotifyApi.startResumeUsersPlayback().context_uri(playlistUri).device_id(deviceId)
                    .position_ms(0).build().execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in startPlaylist: " + e.getMessage());
        }
    }

    public static void resume() {
        try {
            spotifyApi.startResumeUsersPlayback().device_id(deviceId).build().execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in resume: " + e.getMessage());
        }
    }

    public static void pause() {
        try {
            spotifyApi.pauseUsersPlayback().build().execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in pause: " + e.getMessage());
        }
    }

    public static void nextSong() {
        try {
            spotifyApi.skipUsersPlaybackToNextTrack().device_id(deviceId).build().execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in nextSong: " + e.getMessage());
        }
    }

    public static void previousSong() {
        try {
            spotifyApi.skipUsersPlaybackToPreviousTrack().device_id(deviceId).build().execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in nextSong: " + e.getMessage());
        }
    }
}
