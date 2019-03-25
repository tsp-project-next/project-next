package User;
import client.LobbyHost;
import client.LobbyUser;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URI;


public class HostPage {

    private static Scene UserScene = null;
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");

    private static LobbyHost host = null;

    private static Scene hostPage = null;
    private String code;
    private String authorizationCode;
    private int fontSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 40;
    private Font standard = Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, fontSize);

    public HostPage(String code, String authorizationCode, LobbyHost host) {
        this.code = code;
        this.host = host;
        UserInterface.getStage().getScene().setRoot(setup(code, authorizationCode));
    }

    private GridPane setup(String code, String authorizationCode) {

        LobbyUser user = new LobbyUser(clientId, clientSecret);

        host.setAuthCode(authorizationCode);

        host.authorizationCode_Sync();

        host.createPlaylist();

        host.playlistExport();

        host.getDevices();

        String[] songs = new String[]{"spotify:track:7GhIk7Il098yCjg4BQjzvb"};

        host.addSong(songs);

        host.startPlaylist();


        // Setting up the formatting for the main grid controller--------
        GridPane controller = new GridPane();
        controller.setStyle("-fx-background-color: rgb(26,83,46); -fx-background-radius: 10;");
        RowConstraints rowOne = new RowConstraints();
        rowOne.setVgrow(Priority.ALWAYS);
        ColumnConstraints colOne = new ColumnConstraints();
        colOne.setHgrow(Priority.ALWAYS);
        controller.setAlignment(Pos.CENTER);
        controller.setHgap(10);
        controller.setVgap(10);
        controller.setPadding(new Insets(25, 25, 25, 25));
        //----------------------------------------------------------------

        // Basic Features-------------------------------------------------
        Text joinCode = new Text("Code: " + code);
        joinCode.setFill(Color.WHITE);
        joinCode.setFont(standard);
        controller.add(joinCode, 0, 0);

        VBox exitHolder = new VBox();
        Button exitButton = new Button("X");
        exitButton.setOnAction(event -> Platform.exit());
        exitHolder.getChildren().add(exitButton);
        exitHolder.setAlignment(Pos.TOP_RIGHT);
        controller.add(exitHolder, 2, 0);

        VBox endHolder = new VBox();
        Button endSession = new Button("End Session");
        endSession.setOnAction(event -> UserInterface.loadLandingPage());
        endHolder.getChildren().add(endSession);
        endHolder.setAlignment(Pos.CENTER);
        controller.add(endHolder, 2, 2);
        //----------------------------------------------------------------

        // Standard Row Constraints---------------------------------------
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(10);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(90);
        //----------------------------------------------------------------

        // Current Users--------------------------------------------------
        GridPane currentUsers = new GridPane();
        currentUsers.getRowConstraints().addAll(row1, row2);

        Text users = new Text("Users:");
        users.setFill(Color.WHITE);
        users.setFont(standard);
        currentUsers.add(users, 0, 0);

        ScrollPane userList = new ScrollPane();
        ListView<String> uLList = new ListView<>();
        ObservableList<String> uLObs = FXCollections.observableArrayList(
                "Alec", "Dakoda", "Scott", "Connor", "Logan");
        uLList.setItems(uLObs);
        userList.setContent(uLList);
        userList.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        userList.setFitToHeight(true);
        userList.setFitToWidth(true);
        userList.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        currentUsers.add(userList, 0, 1);
        currentUsers.setAlignment(Pos.CENTER);

        controller.add(currentUsers, 0, 1);
        //----------------------------------------------------------------

        // Song Blacklist-------------------------------------------------
        GridPane blacklist = new GridPane();

        Text blacklistText = new Text("Blacklist:");
        blacklistText.setFill(Color.WHITE);
        blacklistText.setFont(standard);
        blacklist.add(blacklistText, 0, 0);

        GridPane search = new GridPane();
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        Button searchButton = new Button("Search");
        search.add(searchBar, 0, 0);
        search.add(searchButton, 1, 0);
        blacklist.add(search, 0, 1);

        ScrollPane blacklistItems = new ScrollPane();
        ListView<String> bLList = new ListView<>();
        ObservableList<String> bLObsList = FXCollections.observableArrayList("");
        bLList.setItems(bLObsList);
        blacklistItems.setContent(bLList);
        blacklistItems.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        blacklistItems.setFitToHeight(true);
        blacklistItems.setFitToWidth(true);
        blacklistItems.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        blacklist.add(blacklistItems, 0, 2);
        blacklist.setAlignment(Pos.CENTER);

        searchButton.setOnAction(event -> {

            bLObsList.clear();

            if (!(searchBar.getText().trim().isEmpty())) {


                if((user.searchTracks(searchBar.getText().trim()).getItems().length !=0)) {
                    Paging<Track> tracks = user.searchTracks(searchBar.getText().trim());

                    if (!(bLObsList.contains(searchBar.getText().trim()))) {

                        for (int i = 0; i <= 9; i++) {
                            Track[] song = tracks.getItems();

                            bLObsList.add(song[i].getName());
                        }
                    }
                } else {

                    bLObsList.add("");
                }
            } else {

                bLObsList.add("");
            }

        });

        controller.add(blacklist, 0, 2);
        //----------------------------------------------------------------

        // Queue----------------------------------------------------------
        GridPane queue = new GridPane();
        queue.getRowConstraints().addAll(row1, row2);

        Text queueText = new Text("Queue:");
        queueText.setFill(Color.WHITE);
        queueText.setFont(standard);
        queue.add(queueText, 0, 0);

        ScrollPane queueItems = new ScrollPane();
        ListView<String> listPlayQueue = new ListView<>();
        ObservableList<String> itemsPlayQueue = FXCollections.observableArrayList(
                "first", "second", "third", "fourth");
        listPlayQueue.setItems(itemsPlayQueue);
        queueItems.setContent(listPlayQueue);
        queueItems.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        queueItems.setFitToHeight(true);
        queueItems.setFitToWidth(true);
        queueItems.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        queue.add(queueItems, 0, 1);
        queue.setAlignment(Pos.CENTER);

        controller.add(queue, 2, 1);
        //----------------------------------------------------------------


        // Currently Playing----------------------------------------------
        VBox currentlyPlaying = new VBox();

        Text playing = new Text("Playing");
        playing.setFill(Color.WHITE);
        playing.setFont(standard);

        Text songTitle = new Text("Song Title");
        songTitle.setFill(Color.WHITE);
        songTitle.setFont(standard);

        Text artist = new Text("Artist");
        artist.setFill(Color.WHITE);
        artist.setFont(standard);

        Text album = new Text("Album");
        album.setFill(Color.WHITE);
        album.setFont(standard);

        currentlyPlaying.getChildren().addAll(playing, songTitle, artist, album);
        currentlyPlaying.setPadding(new Insets(40));
        currentlyPlaying.setAlignment(Pos.CENTER);

        controller.add(currentlyPlaying, 1, 1);
        //----------------------------------------------------------------

        controller.getRowConstraints().addAll(rowOne, rowOne, rowOne);
        controller.getColumnConstraints().addAll(colOne, colOne, colOne);

        return controller;
    }
}
