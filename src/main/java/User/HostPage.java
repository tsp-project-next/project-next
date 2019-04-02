package User;

import client.LobbyHost;
import client.LobbyUser;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
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
import java.util.concurrent.atomic.AtomicBoolean;


public class HostPage {

    private static Scene UserScene = null;
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");
    public static ObservableList<String> itemsPlayQueue;

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

    @SuppressWarnings("Duplicates")
    private GridPane setup(String code, String authorizationCode) {

        LobbyUser user = new LobbyUser(clientId, clientSecret);

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
        ObservableList<String> uLObs = FXCollections.observableArrayList("");
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
        //----------------------------------------------------------------

        // Search --------------------------------------------------------
        GridPane searchPane = new GridPane();

        Text searchText = new Text("Search:");
        searchText.setFill(Color.WHITE);
        searchText.setFont(standard);
        searchPane.add(searchText, 0, 0);

        GridPane hostSearch = new GridPane();
        TextField hostSearchText= new TextField();
        hostSearchText.setPromptText("Search...");
        Button searchButtonHost = new Button("Search");
        hostSearch.add(hostSearchText, 0, 0);
        hostSearch.add(searchButtonHost, 1, 0);
        searchPane.add(hostSearch, 0, 1);

        ScrollPane searchItems = new ScrollPane();
        ListView<String> hostSearchList = new ListView<>();
        ObservableList<String> OhostSearchList = FXCollections.observableArrayList("");
        hostSearchList.setItems(OhostSearchList);
        searchItems.setContent(hostSearchList);
        searchItems.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        searchItems.setFitToHeight(true);
        searchItems.setFitToWidth(true);
        searchItems.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        searchPane.add(searchItems, 0, 2);
        searchPane.setAlignment(Pos.CENTER);

        searchButtonHost.setOnAction(event -> {

            OhostSearchList.clear();

            if (!(hostSearchText.getText().trim().isEmpty())) {


                if((user.searchTracks(hostSearchText.getText().trim()).getItems().length !=0)) {
                    Paging<Track> tracks = user.searchTracks(hostSearchText.getText().trim());

                    if (!(OhostSearchList.contains(hostSearchText.getText().trim()))) {

                        for (int i = 0; i <= 9; i++) {
                            Track[] song = tracks.getItems();

                            OhostSearchList.add(song[i].getName());
                        }
                    }
                } else {

                    OhostSearchList.add("");
                }
            } else {

                OhostSearchList.add("");
            }

        });
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
        itemsPlayQueue = FXCollections.observableArrayList("");
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

        // Makes and Adds the 'Add' button to the screen -----------------
        HBox endHolder = new HBox();
        endHolder.setSpacing(40);

        Button addBlack = new Button("Add To Blacklist");
        addBlack.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/7.5, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
//        addBlack.setOnAction(event -> {
//
//            if(!(hostSearchList.getItems().toString().trim().equals("[]"))) {
//
//                if (!(hostSearchList.getSelectionModel().getSelectedItems().toString().equals("[]"))) {
//
//                    if (!(itemsPlayQueue.contains(hostSearchList.getSelectionModel().getSelectedItem().trim()))) {
//
//                        Paging<Track> tracks = user.searchTracks(hostSearchText.getText().trim());
//
//                        Track[] song = new Track[0];
//
//                        for (int i = 0; i <= 9; i++) {
//
//                            song = tracks.getItems();
//                        }
//
//                        // This is for test in local queue
//                        // Change this to add to Spotify queue
//                        // song[itemsSearchResults.indexOf(listSearchResults.getSelectionModel().getSelectedItem())].getUri()
//
//                        String[] sName = new String [] {song[OhostSearchList.indexOf(hostSearchList.getSelectionModel().getSelectedItem())].getUri()};
//
//                        host.addSong(sName);
//
////                        host.startPlaylist();
//
////                        itemsPlayQueue.add(hostSearchList.getSelectionModel().getSelectedItem().trim());
//
//                    }
//                }
//            }
//        });

        Button addSearch = new Button("Add To Playlist");
        addSearch.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/7.5, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        addSearch.setOnAction(event -> {

            if(!(hostSearchList.getItems().toString().trim().equals("[]"))) {

                if (!(hostSearchList.getSelectionModel().getSelectedItems().toString().equals("[]"))) {

                    if (!(itemsPlayQueue.contains(hostSearchList.getSelectionModel().getSelectedItem().trim()))) {

                        Paging<Track> tracks = user.searchTracks(hostSearchText.getText().trim());


                        Track[] song = new Track[0];

                        for (int i = 0; i <= 9; i++) {

                            song = tracks.getItems();
                        }

                        String[] sName = new String [] {song[OhostSearchList.indexOf(hostSearchList.getSelectionModel().getSelectedItem())].getUri()};

                        host.addSong(sName);

                    }
                }
            }
        });


        Button endSession = new Button("End Session");
        endSession.setOnAction(event -> UserInterface.loadLandingPage());
        endHolder.getChildren().add(endSession);
        endHolder.setAlignment(Pos.CENTER);

        controller.add(endHolder, 1, 2);
        //----------------------------------------------------------------


        blacklist.add(addBlack,0,3);

        controller.add(blacklist, 0, 2);

        searchPane.add(addSearch,0,3);

        controller.add(searchPane, 2, 2);



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

        // Button that switches the user's current song to the previous song
        // in the playlist
        Button back = new Button("<<");
        back.setOnAction(event -> {
            LobbyHost.previousSong();
        });

        // Used to switch the play button to pause and vice versa
        AtomicBoolean nowPlaying = new AtomicBoolean(false);
        // Button that will resume or pause the user's playlist
        Button play = new Button("Play");
        play.setOnAction(event -> {
            if (nowPlaying.get()) {
                LobbyHost.resume();
                play.setText("Pause");
                nowPlaying.set(false);
            } else {
                LobbyHost.pause();
                play.setText("Play");
                nowPlaying.set(true);
            }
        });

        // Button that switches the user's current song to the next song
        // in the playlist
        Button next = new Button(">>");
        next.setOnAction(event -> {
            LobbyHost.nextSong();
        });

        // Adds back, play and next to a HBox in order to have the controls in a row
        HBox controls = new HBox();
        controls.getChildren().addAll(back, play, next);
        controls.setAlignment(Pos.CENTER);

        currentlyPlaying.getChildren().addAll(playing, songTitle, artist, album, controls);

        currentlyPlaying.setPadding(new Insets(40));
        currentlyPlaying.setAlignment(Pos.CENTER);

        controller.add(currentlyPlaying, 1, 1);
        //----------------------------------------------------------------

        controller.getRowConstraints().addAll(rowOne, rowOne, rowOne);
        controller.getColumnConstraints().addAll(colOne, colOne, colOne);

        return controller;
    }

    public static void updateQueue() {

        itemsPlayQueue.clear();

        Paging<PlaylistTrack> tracks = LobbyHost.getPlayListTracks();

        for (int i = 0; i < tracks.getTotal(); i++) {
            PlaylistTrack[] song = tracks.getItems();

            itemsPlayQueue.add(song[i].getTrack().getName());
        }


    }
}