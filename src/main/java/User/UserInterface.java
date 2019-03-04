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

    private static Stage mainStage = null;

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

        System.out.println("Input argument: " + args[0]);

        //create a new client object
        Client client = new Client();

        Application.launch(args);

        //close streams after the application is closed.
        client.closeStreams();
    }

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        //application is currently called Project Next
        mainStage.setTitle("Project Next");
        mainStage.show();
        mainStage.setFullScreen(true);

        //first page on loading is always landing page
        loadLandingPage();
    }

    //displays landing page on the window
    public static void loadLandingPage() {
        LandingPage landingPage = new LandingPage();
        mainStage.setScene(landingPage.getScene());
        mainStage.setFullScreen(true);
    }

    //displays host page on the window
    public static void loadHostPage() {
        HostPage hostPage = new HostPage(Utilities.codeGenerator());
        mainStage.setScene(hostPage.getScene());
        mainStage.setFullScreen(true);
    }

    //displays user page on the window
    public static void loadUserPage(String code) {
        UserPage userPage = new UserPage(code);
        mainStage.setScene(userPage.getScene());
        mainStage.setFullScreen(true);
    }
}
