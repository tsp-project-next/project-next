package User;

import client.Client;
import client.LobbyHost;
import client.LobbyUser;
import com.wrapper.spotify.SpotifyHttpManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.URI;

public class UserInterface extends Application{


    // We need these initialized for lobby user or host. Do not alter
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");
    // End do not alter

    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("client"))
        {
            LobbyUser user = new LobbyUser(clientId, clientSecret);
        }
        else if (args[0].equalsIgnoreCase("host"))
        {
            LobbyHost host = new LobbyHost(clientId, clientSecret, redirectUri);
            //host.generateURI();
        }

        System.out.println(args[0]);

        new Client();

        Client client = new Client();

        Application.launch(args);

        client.closeStreams();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Project Next"); //application is currently called Project Next
        primaryStage.show();
        LandingPage landingPage = new LandingPage(); //creates the landing page
        primaryStage.setScene(landingPage.getScene()); //sets the window to contain the landing page
        primaryStage.setFullScreen(true); //sets the window to fullscreen
    }
}
