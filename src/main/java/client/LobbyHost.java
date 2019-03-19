package client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.awt.*;
import java.io.IOException;
import java.net.URI;


public class LobbyHost {

    private static SpotifyApi spotifyApi = null;
    private static AuthorizationCodeUriRequest authorizationCodeUriRequest = null;

    // We might not need this?
    private static String code = "";
    private static AuthorizationCodeRequest authorizationCodeRequest = null;

    public LobbyHost(String clientId, String clientSecret, URI redirectURI) {

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURI)
                .build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("thisisouruniqueidentifier")
                .scope("user-read-birthdate,user-read-email")
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

    public static void authorizationCode_Sync() {
        authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error in authorizationCode_Sync() Lobby Host: " + e.getMessage());
        }
    }

    public static void setAuthCode(String authCode) {
        code = authCode;
    }
}
