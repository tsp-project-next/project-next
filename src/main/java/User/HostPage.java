package User;

import client.LobbyHost;
import client.Packet;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("Duplicates")
public class HostPage {
    private static LobbyHost host = null;
    private static ObservableList<String> itemsPlayQueue;
    private static ObservableList<String> uLObs;
    private static Text playing = new Text();
    private static Text songTitle = new Text();
    private static Text artist = new Text();
    private static Text album = new Text();
    private static boolean triggered = false;
    private static String currentSong = "";

    private static Timer timer;
    private static boolean isTimerRunning = false;

    public HostPage(String code, LobbyHost host) {
        this.host = host;

        setup(code);
    }

    private static void setup(String code) {

        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        int fontSize = (int) screenHeight / 40;
        Font standard = Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, fontSize);

        // Setting up the formatting for the main grid root--------
        GridPane root = new GridPane();
        root.getStylesheets().add("scene.css");
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(5));
        //----------------------------------------------------------------

        // Sets col and row constraints to grow or not -------------------
        RowConstraints rowOne = new RowConstraints();
        rowOne.setVgrow(Priority.ALWAYS);

        ColumnConstraints colOne = new ColumnConstraints();
        colOne.setHgrow(Priority.ALWAYS);

        root.getRowConstraints().addAll(rowOne, rowOne, rowOne);
        root.getColumnConstraints().addAll(colOne, colOne, colOne);
        //----------------------------------------------------------------

        // Basic Features-------------------------------------------------
        Text joinCode = new Text("Code: " + code);
        joinCode.setFill(Color.WHITE);
        joinCode.setFont(standard);
        root.add(joinCode, 0, 0);
        //----------------------------------------------------------------

        // Makes and Adds the 'X' button ---------------------------------
        Button exitButton = new Button("X");
        exitButton.setOnAction(event -> {
            Packet senUsersToLandingPage = new Packet(Utilities.generatePacketIdentifier(), 6);
            senUsersToLandingPage.setLobby(code);
            UserInterface.client.sendPacket(senUsersToLandingPage);

            if(isTimerRunning()) {
                timerUpdate(false);
            }
            Platform.exit();
        });

        root.add(exitButton, 2, 0);
        //----------------------------------------------------------------

        //End Session ----------------------------------------------------
        Button endSession = new Button("End Session");
        endSession.setOnAction(event -> {
            Packet packet = new Packet(Utilities.generatePacketIdentifier(), 4);
            packet.setLobby(code);
            UserInterface.client.sendPacket(packet);

            if(isTimerRunning()) {
                timerUpdate(false);
            }
            UserInterface.loadLandingPage();
        });
        root.add(endSession, 1, 2);
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
        uLObs = FXCollections.observableArrayList("");
        uLList.setItems(uLObs);
        userList.setContent(uLList);
        userList.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        userList.setFitToHeight(true);
        userList.setFitToWidth(true);
        userList.setMaxWidth((1*screenWidth)/4);
        currentUsers.add(userList, 0, 1);
        currentUsers.setAlignment(Pos.CENTER);

        root.add(currentUsers, 0, 1);
        //----------------------------------------------------------------

        // Song Blacklist Search and Add ---------------------------------
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
        blacklistItems.setMaxWidth((1*screenWidth)/4);
        blacklist.add(blacklistItems, 0, 2);
        blacklist.setAlignment(Pos.CENTER);

        searchButton.setOnAction(event -> {

            bLObsList.clear();

            if (!(searchBar.getText().trim().isEmpty())) {


                if((host.searchTracks(searchBar.getText().trim()).getItems().length !=0)) {

                    Paging<Track> tracks = host.searchTracks(searchBar.getText().trim());

                    if (!(bLObsList.contains(searchBar.getText().trim()))) {

                        for (int i = 0; i < tracks.getItems().length; i++) {

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

        Button addBlack = new Button("Add To Blacklist");
        addBlack.setPrefSize(screenWidth/7.5, screenHeight /30);
        addBlack.setOnAction(event -> {

            Paging<Track> tracks = host.searchTracks(searchBar.getText().trim());

            Track[] song;

            song = tracks.getItems();

            String[] sName = new String [] {song[bLObsList.indexOf(bLList.getSelectionModel().getSelectedItem())].getUri()};

            host.addToBlackList(sName[0]);
        });

        blacklist.add(addBlack,0,3);

        root.add(blacklist, 0, 2);
        //----------------------------------------------------------------

        // Currently Playing----------------------------------------------
        VBox currentlyPlaying = new VBox();
        currentlyPlaying.setSpacing(40);

        playing.setText("Playing");
        playing.setFill(Color.WHITE);
        playing.setFont(standard);

        songTitle.textProperty().setValue("Song: No Song");
        songTitle.setFill(Color.WHITE);
        songTitle.setFont(standard);

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

                host.startPlaylist();
                play.setText("Pause");
                nowPlaying.set(true);
                firstSong.set(false);

                if (!triggered) {

                    triggered = true;

                    //currentSong = host.getCurrentPlaying().getUri();

                    if(!isTimerRunning()) {
                        timerUpdate(true);
                    }
                }
            }
            if (nowPlaying.get()) {

                host.resume();
                play.setText("Pause");
                nowPlaying.set(false);
            } else {

                host.pause();
                play.setText("Play");
                nowPlaying.set(true);
            }
        });

        // Button that switches the user's current song to the next song
        // in the playlist
        Button next = new Button(">>");
        next.setOnAction(event -> {
            String cSong = host.getCurrentPlaying().getUri();

            JsonArray arrayedTracks = new JsonParser().parse("[{\"uri\":\"" + cSong + "\"}]").getAsJsonArray();

            host.deleteSongFromPlaylist(arrayedTracks);

            host.startPlaylist();

            updateQueue();
        });

        // Adds back, play and next to a HBox in order to have the controls in a row
        HBox controls = new HBox();
        controls.getChildren().addAll(play, next);
        controls.setAlignment(Pos.CENTER);

        currentlyPlaying.getChildren().addAll(playing, songTitle, artist, album, controls);

        currentlyPlaying.setPadding(new Insets(40));
        currentlyPlaying.setAlignment(Pos.CENTER);

        root.add(currentlyPlaying, 1, 1);
        //----------------------------------------------------------------

        // Queue with add to queue ---------------------------------------
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
        queueItems.setMaxWidth((1*screenWidth)/4);
        queue.add(queueItems, 0, 1);
        queue.setAlignment(Pos.CENTER);

        root.add(queue, 2, 1);

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
        searchItems.setMaxWidth((1*screenWidth)/4);
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

        Button addSearch = new Button("Add To Playlist");
        addSearch.setPrefSize(screenWidth/7.5, screenHeight /30);
        addSearch.setOnAction(event -> {

            if(!(hostSearchList.getItems().toString().trim().equals("[]"))) {

                if (!(hostSearchList.getSelectionModel().getSelectedItems().toString().equals("[]"))) {

                    if (!(itemsPlayQueue.contains(hostSearchList.getSelectionModel().getSelectedItem().trim()))) {

                        Paging<Track> tracks = host.searchTracks(hostSearchText.getText().trim());

                        Track[] song;

                        song = tracks.getItems();

                        String[] sName = new String [] {song[OhostSearchList.indexOf(hostSearchList.getSelectionModel().getSelectedItem())].getUri()};

                        Packet packet = new Packet(Utilities.generatePacketIdentifier(), 3);
                        packet.setSongURI(sName[0]);
                        packet.setLobby(code);
                        UserInterface.client.sendPacket(packet);
                    }
                }
            }
        });

        searchPane.add(addSearch,0,3);

        root.add(searchPane, 2, 2);
        //----------------------------------------------------------------

        // Sets the alignment of the elements inside the GridPane --------
        GridPane.setHalignment(joinCode, HPos.LEFT);
        GridPane.setValignment(joinCode, VPos.TOP);

        GridPane.setHalignment(exitButton, HPos.RIGHT);
        GridPane.setValignment(exitButton, VPos.TOP);

        GridPane.setHalignment(endSession, HPos.CENTER);
        GridPane.setValignment(endSession, VPos.CENTER);
        //----------------------------------------------------------------

        UserInterface.getStage().getScene().setRoot(root);
    }

    public static void updateQueue() {

        itemsPlayQueue.clear();

        Paging<PlaylistTrack> tracks = host.getPlayListTracks();

        if (tracks != null) {

            for (int i = 0; i < tracks.getItems().length; i++) {
                PlaylistTrack[] song = tracks.getItems();

                itemsPlayQueue.add(song[i].getTrack().getName());
            }
        }
    }

    public static void updateCurrentPlaying() {

        Paging<PlaylistTrack> tracks = host.getPlayListTracks();

        String currentlyPlaying = "";

        try {
            currentlyPlaying = host.getCurrentPlaying().getUri();
        } catch (NullPointerException e) {
            //System.out.println("We caught this null pointer exception");
        }

        if (tracks.getItems().length != 0) {

            PlaylistTrack[] current = tracks.getItems();

            currentSong = current[0].getTrack().getUri();

            songTitle.setText("Song: "+ current[0].getTrack().getName());

            artist.setText("Artist: " + current[0].getTrack().getArtists()[0].getName());

            album.setText("Album: " + current[0].getTrack().getAlbum().getName());
        }


        if (!currentlyPlaying.equals(currentSong) && tracks != null && !currentlyPlaying.equals("")) {
            // If the host has naturally moved to another song, store the title of the
            // currenly playing song
            currentSong = currentlyPlaying;

            JsonArray arrayedTracks = new JsonParser().parse("[{\"uri\":\"" + tracks.getItems()[0].getTrack().getUri() + "\"}]").getAsJsonArray();

            host.deleteSongFromPlaylist(arrayedTracks);

            host.startPlaylist();

            updateQueue();
        }

        if (currentlyPlaying.equals("")) {
            String songName = "";

            songTitle.setText(songName);

            artist.setText("Artist: ");

            album.setText("Album: ");
        }
    }

    public static void updateUserId(ArrayList<String> array) {

        uLObs.clear();

        uLObs.addAll(array);
    }

    public static boolean isInitialized() {
        if(host == null)
            return false;
        
        return true;
    }

    public static void addedToBlackList() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setContentText("Alert");
        alert.setHeaderText("Added to blacklist");
        alert.showAndWait();
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

                    if(HostPage.isInitialized()) {
                        HostPage.updateCurrentPlaying();
                    }
                }
            }, 0, 1000);
        } else {
            timer.cancel();
            timer.purge();
            isTimerRunning = false;
        }
    }

    public static boolean isTimerRunning() {
        return isTimerRunning;
    }

    public static void checkEmpty() {

        if(itemsPlayQueue.isEmpty()) {
            itemsPlayQueue.add("");
        }
    }
}