package client;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {

    //Host name or ip address
    private String host = "localhost";

    //Socket for establishing connection
    private Socket socket;
    //Object output stream to the server
    private ObjectOutputStream toServer;
    //Object input stream from the server
    private ObjectInputStream fromServer;

    // Can be moved later depending on how we want to fully implement this
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");
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

        Client client = new Client();
        client.closeStreams();
    }

    public Client() {
        System.out.println("client created...");

        //Establish connection with server
        if(establishConnection(host) == false) {
            return;
        }

        //establish object data streams with server
        if(establishObjectStreams(socket) == false) {
            return;
        }

        //Send a test packet to the server
        Packet received = sendPacket("Testing", "host");
    }

    private boolean establishConnection(String host) {
        try {
            socket = new Socket(host, 9000);
            System.out.println("Socket connected");
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

    private boolean establishObjectStreams(Socket socket) {
        try {
            //Output stream has to be created before input stream.
            //Create and object output stream to the server
            toServer = new ObjectOutputStream(socket.getOutputStream());
            //Create and object input stream to the server
            fromServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("Streams established");
            return true;
        } catch(IOException ex) {
            ex.printStackTrace();
            System.out.println("failed to establish object streams");
            return false;
        }
    }

    private Packet sendPacket(String data, String cid) {
        try {
            //Create a packet to send
            Packet packet = new Packet(data, cid);
            toServer.writeObject(packet);
            System.out.println("Object sent to server...");

            try {
                //wait for the object response from the server
                Object obj = fromServer.readObject();
                Packet received = (Packet) obj;
                System.out.println("Successfully read object: " + received.getData());
                return received;
            } catch(ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch(SocketException ex) {
                System.out.println("Connection to server reset/lost");
                ex.printStackTrace();
            }
            return null;
        } catch(IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void closeStreams() {
        try {
            System.out.println("Attempting to close streams...");
            if(fromServer != null) fromServer.close();
            if(toServer != null) toServer.close();
            if(socket != null) socket.close();
            System.out.println("Streams closed.");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
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
