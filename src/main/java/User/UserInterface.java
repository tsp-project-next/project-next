package User;

import client.Client;
import client.LobbyHost;
import client.LobbyUser;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class UserInterface extends Application{

    // We need these initialized for lobby user or host. Do not alter ----
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");
    //--------------------------------------------------------------------

    private static Stage mainStage = null;
    private static Scene mainScene = null;

    public static Client client;

    public static Thread t;

    private static Timer timer;
    private static boolean isTimerRunning = false;

    // Main method -------------------------------------------------------
    public static void main(String[] args) {
        //create a new client object
        client = new Client();
        if(!client.isConnectionEstablished()) {
            System.out.println("Connection to server not found.");
            System.exit(0);
        }

        //need to save the thread.
        t = Thread.currentThread();

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
    }
    //--------------------------------------------------------------------

    //displays host page on the window -----------------------------------
    public static void loadHostPage(String authorizationCode, LobbyHost host) {

        host.setAuthCode(authorizationCode);

        if(host.authorizationCode_Sync()) {

            host.createPlaylist();

            host.getDevices();

            String codeResponse = client.sendPacketWaitResponse(Utilities.generatePacketIdentifier(), 0, host.getPlaylistURI(), null, null);

            if (codeResponse == null) {
                System.out.println("lobby creation code from server returned null");
            }

            host.setCode(codeResponse);

            HostPage hostPage = new HostPage(codeResponse, host);
        }

    }
    //--------------------------------------------------------------------

    //displays user page on the window -----------------------------------
    public static void loadUserPage(String code) {
        String identifier = Utilities.generatePacketIdentifier();
        String responseURI = client.sendPacketWaitResponse(identifier,1, null, null, code);
        if(responseURI == " ") {
            //need to actually do something here
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Lobby Code");

            // alert.setHeaderText("Results:");
            alert.setContentText("Invalid Lobby Code");
            alert.setHeaderText("Please check case sensitivity or make sure the lobby is still being hosted.");

            alert.showAndWait();
            System.out.println("lobby join uri from server returned null");
            return;
        } else {
            //System.out.println("Joined lobby and returned uri: " + responseURI);
        }

        LobbyUser.setPLaylistUri(responseURI);
        LobbyUser.getPlaylistId();

        UserPage userPage = new UserPage(code);
        //mainStage.setScene(userPage.getScene());
    }
    //--------------------------------------------------------------------

    public static Stage getStage() {
        return mainStage;
    }

    public static void timerUpdate(Boolean start) {

        if (start) {
            isTimerRunning = true;
            timer = new Timer();

            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(isTimerRunning == false) {
                        return;
                    }
                    //System.out.println("running");
                    if(HostPage.isInitialized()) {
                        HostPage.updateCurrentPlaying();
                    }
                    if(UserPage.isInitialized()) {
                        UserPage.updateCurrentPlaying();
                    }
                }
            }, 0, 5000);
        } else {
            timer.cancel();
            timer.purge();
            isTimerRunning = false;
        }
    }

    public static boolean isTimerRunning() {
        return isTimerRunning;
    }
}
