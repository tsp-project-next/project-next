package client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
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
    private static AuthorizationCodeRequest authorizationCodeRequest = null;

    public LobbyHost(String clientId, String clientSecret, URI redirectURI) {

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURI)
                .build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("thisisouruniqueidentifier")
                .scope("playlist-modify-public,playlist-modify-private,playlist-read-private,playlist-read-collaborative,app-remote-control,user-read-currently-playing,streaming")
                .show_dialog(true)
                .build();

        try {
            generateURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateURI() throws Exception {
        final URI uri = authorizationCodeUriRequest.execute();

        // This is where we'd prompt the user with the URI to allow us to query specific account info
        System.out.println("URI: " + uri.toString());

        Desktop desktop = Desktop.getDesktop();

        desktop.browse(uri);

        return uri.toString();
    }

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

    public static void createPlaylist() {
        GetCurrentUsersProfileRequest profileRequest = spotifyApi.getCurrentUsersProfile().build();

        try {
            final User user = profileRequest.execute();

            userId = user.getDisplayName();

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

            UserInterface.client.sendPacket(Utilities.generatePacketIdentifier(),0, playlistUri, null, null);

        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in createPlaylist: " + e.getMessage());
        }
    }

    public static void playlistCleanup() {
        try {
            spotifyApi.unfollowPlaylist(userId, playlistId).build().execute();
            System.out.println("Unfollowed playlist");
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in playlistCleanup: " + e.getMessage());
        }
    }
}
