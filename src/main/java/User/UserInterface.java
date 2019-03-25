package User;

import client.Client;
import client.LobbyHost;
import client.LobbyUser;
import com.wrapper.spotify.SpotifyHttpManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;

public class UserInterface extends Application{

    // We need these initialized for lobby user or host. Do not alter ----
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");
    //--------------------------------------------------------------------

    private static Stage mainStage = null;
    private static Scene mainScene = null;

    public static Client client;

    // Main method -------------------------------------------------------
    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("client"))
        {
            LobbyUser user = new LobbyUser(clientId, clientSecret);
        }
        else if (args[0].equalsIgnoreCase("host"))
        {
            //LobbyHost host = new LobbyHost(clientId, clientSecret, redirectUri);
            //host.generateURI();
        }

        System.out.println("Input argument: " + args[0]);

        //create a new client object
        client = new Client();

        Application.launch(args);

        //close streams after the application is closed.
        client.closeStreams();
    }
    //--------------------------------------------------------------------

    // Starts the landing page -------------------------------------------
    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        //application is currently called Project Next
        mainStage.setTitle("Project Next");
        mainStage.show();
        GridPane root = new GridPane();
        mainStage.setScene(new Scene(root, Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                Toolkit.getDefaultToolkit().getScreenSize().getHeight(), Color.rgb(26,83,46)));
        mainStage.setFullScreen(false);
        //first page on loading is always landing page
        loadLandingPage();
    }
    //--------------------------------------------------------------------

    //displays landing page on the window --------------------------------
    public static void loadLandingPage() {
        getStage().setFullScreen(false);
        LandingPage landingPage = new LandingPage();

        //getStage().setScene(landingPage.getScene());
    }
    //--------------------------------------------------------------------

    //displays host page on the window -----------------------------------
    public static void loadHostPage(String authorizationCode, LobbyHost host) {
        HostPage hostPage = new HostPage(Utilities.codeGenerator(), authorizationCode, host);
        //getStage().setScene(hostPage.getScene());
    }
    //--------------------------------------------------------------------

    //displays user page on the window -----------------------------------
    public static void loadUserPage(String code) {
        UserPage userPage = new UserPage(code);
        //mainStage.setScene(userPage.getScene());
    }
    //--------------------------------------------------------------------

    public static Stage getStage() {
        return mainStage;
    }
}
