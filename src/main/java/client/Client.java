package client;


import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    //Host name or ip address
    String host = "localhost";

    //Socket for establishing connection
    Socket socket;

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
        new Client();
    }

    public Client() {
        try {
            System.out.println("client created...");

            //Establish connection with server
            if(establishConnection(host) == false) {
                return;
            }
            System.out.println("Is socket connected: " + socket.isConnected());

            //Output stream has to be created before input stream.

            //Create and object output stream to the server
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());

            //Create and object input stream to the server
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

            System.out.println("Streams established");

            //Create a packet to send
            Packet packet = new Packet("Testing");
            toServer.writeObject(packet);
            System.out.println("wrote object...");

            try {
                Object obj = fromServer.readObject();
                Packet received = (Packet) obj;
                System.out.println("Successfully read object: " + received.getData());
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean establishConnection(String host) {
        try {
            socket = new Socket(host, 9000);
            return true;
        } catch (ConnectException ex) {
            System.out.println("No server connection found.");
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
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
