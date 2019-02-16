package client;


import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

public class Client {


    // Can be moved later depending on how we want to fully implement this
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://connormulcahy.com");
    // End move

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();

    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .state("x4xkmn9pu3j6ukrs8n")
            .scope("user-read-birthdate,user-read-email")
            .show_dialog(true)
            .build();

    public static void main(String[] args)
    {
        // Entry point
        System.out.println("This is a successful run after gradle build...");

        authorizationCodeUri_Sync();
    }

    public String fakeTest()
    {
        return "test";
    }

    // Can be moved later depending on how we want to implement this.
    public static void authorizationCodeUri_Sync() {
        final URI uri = authorizationCodeUriRequest.execute();

        System.out.println("URI: " + uri.toString());
    }
    // End move
}
