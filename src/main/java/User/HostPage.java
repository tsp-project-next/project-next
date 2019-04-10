package User;

import client.LobbyHost;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class HostPage {

//    private static Scene hostPage = null;
    private static LobbyHost host = null;
    private static ObservableList<String> itemsPlayQueue;
    private static Text playing = new Text();
    private static Text songTitle = new Text();
    private static Text artist = new Text();
    private static Text album = new Text();
    private static Text scrollingText = new Text();
    private static boolean triggered = false;
    private static String code;

    public HostPage(String code, LobbyHost host) {
        this.code = code;
        this.host = host;
        UserInterface.getStage().getScene().setRoot(setup(code));
    }


    @SuppressWarnings("Duplicates")
    private GridPane setup(String code) {

        int fontSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 40;
        Font standard = Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, fontSize);

        //host.startPlaylist();

        // Setting up the formatting for the main grid controller--------
        GridPane controller = new GridPane();
        controller.getStylesheets().add("scene.css");
        controller.setStyle("-fx-background-color: rgb(26,83,46); -fx-background-radius: 10;");
        RowConstraints rowOne = new RowConstraints();
        rowOne.setVgrow(Priority.ALWAYS);
        ColumnConstraints colOne = new ColumnConstraints();
        colOne.setHgrow(Priority.ALWAYS);
        controller.setAlignment(Pos.CENTER);
        controller.setHgap(10);
        controller.setVgap(10);
        controller.setPadding(new Insets(5));
        //----------------------------------------------------------------

        // Basic Features-------------------------------------------------
        Text joinCode = new Text("Code: " + code);
        joinCode.setFill(Color.WHITE);
        joinCode.setFont(standard);
        controller.add(joinCode, 0, 0);

        VBox exitHolder = new VBox();
        Button exitButton = new Button("X");
        exitButton.setOnAction(event -> {
            if(UserInterface.isTimerRunning()) {
                UserInterface.timerUpdate(false);
            }
            Platform.exit();
        });

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


                if((host.searchTracks(searchBar.getText().trim()).getItems().length !=0)) {
                    Paging<Track> tracks = host.searchTracks(searchBar.getText().trim());

                    if (!(bLObsList.contains(searchBar.getText().trim()))) {

                        for (int i = 0; i <= tracks.getTotal(); i++) {
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


                if((host.searchTracks(hostSearchText.getText().trim()).getItems().length !=0)) {
                    Paging<Track> tracks = host.searchTracks(hostSearchText.getText().trim());

                    if (!(OhostSearchList.contains(hostSearchText.getText().trim()))) {

                        for (int i = 0; i < tracks.getItems().length; i++) {
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
        // Blacklist functionality here

        Button addSearch = new Button("Add To Playlist");
        addSearch.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/7.5, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        addSearch.setOnAction(event -> {

            if(!(hostSearchList.getItems().toString().trim().equals("[]"))) {

                if (!(hostSearchList.getSelectionModel().getSelectedItems().toString().equals("[]"))) {

                    if (!(itemsPlayQueue.contains(hostSearchList.getSelectionModel().getSelectedItem().trim()))) {

                        Paging<Track> tracks = host.searchTracks(hostSearchText.getText().trim());


                        Track[] song = new Track[0];

                        for (int i = 0; i <= tracks.getItems().length; i++) {

                            song = tracks.getItems();
                        }

                        String[] sName = new String [] {song[OhostSearchList.indexOf(hostSearchList.getSelectionModel().getSelectedItem())].getUri()};

                        host.addSong(sName);
                    }
                }
            }
        });


        Button endSession = new Button("End Session");
        endSession.setOnAction(event -> {
            UserInterface.client.sendPacket(Utilities.generatePacketIdentifier(), 4, null, null, null);
            if(UserInterface.isTimerRunning()) {
                UserInterface.timerUpdate(false);
            }
            UserInterface.loadLandingPage();
        });
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

        playing.setText("Playing");
        playing.setFill(Color.WHITE);
        playing.setFont(standard);


        HBox SONG = new HBox();
        SONG.setMaxWidth(100);

        songTitle.textProperty().setValue("Song: ");
        songTitle.setFill(Color.WHITE);
        songTitle.setFont(standard);

        scrollingText.setText("No Song");
        scrollingText.setLayoutX(songTitle.getLayoutX() + 10);
        scrollingText.setLayoutY(songTitle.getLayoutY());
        scrollingText.setFill(Color.WHITE);
        scrollingText.setFont(standard);
//        scrollingText.setWrappingWidth(100);

        SONG.getChildren().addAll(songTitle, scrollingText);


        artist.setText("Artist: No Song");
        artist.setFill(Color.WHITE);
        artist.setFont(standard);

        album.setText("Album: No Song");
        album.setFill(Color.WHITE);
        album.setFont(standard);

        // Used to switch the play button to pause and vice versa
        AtomicBoolean nowPlaying = new AtomicBoolean(false);
        AtomicBoolean firstSong = new AtomicBoolean(true);
        // Button that will resume or pause the user's playlist
        Button play = new Button("Play");
        play.setOnAction(event -> {
            if (!nowPlaying.get() && firstSong.get()) {
                LobbyHost.startPlaylist();
                play.setText("Pause");
                nowPlaying.set(true);
                firstSong.set(false);

                if (!triggered) {

                    triggered = true;

                    UserInterface.timerUpdate(true);
                }
            }
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
        controls.getChildren().addAll(play, next);
        controls.setAlignment(Pos.CENTER);

        currentlyPlaying.getChildren().addAll(playing, SONG, artist, album, controls);

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

        if (tracks != null) {

            for (int i = 0; i < tracks.getItems().length; i++) {
                PlaylistTrack[] song = tracks.getItems();

                itemsPlayQueue.add(song[i].getTrack().getName());
            }
        }
    }

    public static void updateCurrentPlaying() {

        Paging<PlaylistTrack> tracks = LobbyHost.getPlayListTracks();

        if (tracks != null) {

            PlaylistTrack[] current = tracks.getItems();

            String songName = current[0].getTrack().getName();

            if (songName.length() >= 20 ) {

                TranslateTransition tt = new TranslateTransition(Duration.millis(3000), scrollingText);
                tt.setFromX(songTitle.getLayoutX() + 10); // setFromX sets the starting position, coming from the left and going to the right.
                tt.setToX(scrollingText.getWrappingWidth() + songTitle.getLayoutX() + 100); // setToX sets to target position, go beyond the right side of the screen.
                tt.setCycleCount(Timeline.INDEFINITE); // repeats for ever
                tt.setAutoReverse(false); //Always start over
                tt.play();
            }

            scrollingText.setText(songName);

            artist.setText("Artist: " + current[0].getTrack().getArtists()[0].getName());

            album.setText("Album: " + current[0].getTrack().getAlbum().getName());
        }
    }

    public static boolean isInitialized() {
        if(host == null) return false;
        return true;
    }
}