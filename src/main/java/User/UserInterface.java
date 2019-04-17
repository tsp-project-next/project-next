package User;

import client.Client;
import client.LobbyHost;
import client.LobbyUser;
import client.Packet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class UserInterface extends Application{

    private static Stage mainStage = null;

    public static Client client;

    public static Thread t;

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
        new LandingPage();
    }
    //--------------------------------------------------------------------

    //displays host page on the window -----------------------------------
    public static void loadHostPage(String authorizationCode, LobbyHost host) {

        host.setAuthCode(authorizationCode);

        if(host.authorizationCode_Sync()) {

            host.createPlaylist();

            host.getDevices();

            Packet packet = new Packet(Utilities.generatePacketIdentifier(), 0);
            packet.setPlaylistURI(host.getPlaylistURI());
            String codeResponse = client.sendPacketWaitResponse(packet);

            if (codeResponse == null) {
                System.out.println("lobby creation code from server returned null");
            }

            host.setCode(codeResponse);
            new HostPage(codeResponse, host);
        }

    }
    //--------------------------------------------------------------------

    //displays user page on the window -----------------------------------
    public static void loadUserPage(String code) {
        Packet packet = new Packet( Utilities.generatePacketIdentifier(), 1);
        packet.setLobby(code);
        String responseURI = client.sendPacketWaitResponse(packet);
        if(responseURI == " ") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Lobby Code");

            alert.setContentText("Invalid Lobby Code");
            alert.setHeaderText("Please check case sensitivity or make sure the lobby is still being hosted.");

            alert.showAndWait();
            System.out.println("lobby join uri from server returned null");
            return;
        } else {

            LobbyUser.setPLaylistUri(responseURI);
            LobbyUser.getPlaylistId();

            new UserPage(code);
        }
    }
    //--------------------------------------------------------------------

    public static Stage getStage() {
        return mainStage;
    }

    public static void inBlackList() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setContentText("Alert");
        alert.setHeaderText("Song is in blacklist");
        alert.showAndWait();

        if(HostPage.isInitialized()) {
            HostPage.checkEmpty();
        } else if (UserPage.isInitialized()) {
            UserPage.checkEmpty();
        }

    }
}
