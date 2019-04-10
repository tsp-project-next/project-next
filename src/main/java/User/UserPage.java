package User;

import client.LobbyUser;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import java.awt.*;

import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;

public class UserPage  {

//    private static Scene UserScene = null;
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static ObservableList<String> itemsPlayQueue;
    private static Text playing = new Text();
    private static Text songTitle = new Text();
    private static Text artist = new Text();
    private static Text album = new Text();
    private static LobbyUser user = null;


    public UserPage(String code) {
        UserInterface.getStage().getScene().setRoot(setup(code));
    }

    // Makes and Adds all elements of the UserPage to the UserPage
    @SuppressWarnings("Duplicates")
    private GridPane setup(String Code) {

        user = new LobbyUser(clientId, clientSecret);

        int fontSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 40;
        Font standard = Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, fontSize);

        // Makes a GridPane Called Root ----------------------------------
        GridPane root = new GridPane();
        root.getStylesheets().add("scene.css");
        root.setPadding(new Insets(5));
        root.setHgap(10);
        root.setVgap(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        root.setMinSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        root.setStyle("-fx-background-color: transparent");
        //----------------------------------------------------------------

        // Makes a VBox called stack -------------------------------------
        VBox stack = new VBox();
        //----------------------------------------------------------------

        // Sets col and row constraints to grow or not -------------------
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.NEVER);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.NEVER);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(col1, col1, col1, col1, col1 ,col1, col1);
        root.getRowConstraints().addAll(row1, row2, row1, row2, row2, row2, row1);
        //----------------------------------------------------------------

        //Changes the root of the stage's scene --------------------------
        UserInterface.getStage().getScene().setRoot(root);
        //----------------------------------------------------------------

        // Start text zones, font is for scaling font sizes --------------
        int font = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/40;
        //----------------------------------------------------------------

        // Makes the code displayer for top left of the screen -----------
        Text code = new Text();
        code.setText("Code:" + Code);
        code.setFill(Color.WHITE);
        code.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));
        root.add(code,0, 0);
        //----------------------------------------------------------------

        // Makes the header for the queue scroll panel -------------------
        Text queue = new Text();
        queue.setText("Play Queue");
        queue.setFill(Color.WHITE);
        queue.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));
        root.add(queue,5, 2, 2, 1);
        //----------------------------------------------------------------

        // Makes and Adds the search bar to the header -------------------
        TextField searchBar = new TextField();
        searchBar.setMaxSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        searchBar.setPromptText("Search....");
        root.add(searchBar, 0,2);
        //----------------------------------------------------------------

        // Makes a ScrollPane called searchResults -----------------------
        ScrollPane searchResults = new ScrollPane();
        ListView<String> listSearchResults = new ListView<>();
        ObservableList<String> itemsSearchResults = FXCollections.observableArrayList("");
        listSearchResults.setItems(itemsSearchResults);
        searchResults.setContent(listSearchResults);
        searchResults.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        searchResults.setFitToHeight(true);
        searchResults.setFitToWidth(true);
        searchResults.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        root.add(searchResults, 0,3,2,2);
        //----------------------------------------------------------------

        // Makes a ScrollPane called playQueue ---------------------------
        ScrollPane playQueue = new ScrollPane();
        ListView<String> listPlayQueue = new ListView<>();
        itemsPlayQueue = FXCollections.observableArrayList("");
        listPlayQueue.setItems(itemsPlayQueue);
        playQueue.setContent(listPlayQueue);
        playQueue.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playQueue.setFitToHeight(true);
        playQueue.setFitToWidth(true);
        playQueue.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        root.add(playQueue, 5,3,2,2);
        //----------------------------------------------------------------

        // Current Playing Display ---------------------------------------

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

        stack.setSpacing(40);

        stack.getChildren().addAll(playing, songTitle, artist, album);
        stack.setAlignment(Pos.TOP_LEFT);
        root.add(stack, 3, 3, 2,2);
        //----------------------------------------------------------------

        // Makes and Adds the 'Add' button to the screen -----------------
        Button add = new Button("Add");
        add.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        add.setOnAction(event -> {

            if(!(listSearchResults.getItems().toString().trim().equals("[]"))) {

                if (!(listSearchResults.getSelectionModel().getSelectedItems().toString().equals("[]"))) {

                    if (itemsPlayQueue.get(0).trim().isEmpty()) {

                        itemsPlayQueue.clear();
                    }

                    if (!(itemsPlayQueue.contains(listSearchResults.getSelectionModel().getSelectedItem().trim()))) {

                        Paging<Track> tracks = user.searchTracks(searchBar.getText().trim());

                        Track[] song = new Track[0];

                        for (int i = 0; i <= 9; i++) {

                            song = tracks.getItems();
                        }

                        String[] sName = new String [] {song[itemsSearchResults.indexOf(listSearchResults.getSelectionModel().getSelectedItem())].getUri()};

                        UserInterface.client.sendPacket(Utilities.generatePacketIdentifier(), 3, null, sName[0], Code);
                    }
                }
            }
        });
        root.add(add, 0,5, 2,1);
        //----------------------------------------------------------------

        // Makes and Adds the 'Search' button to the right of the search -
        Button search = new Button("Search");
        search.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/15, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        search.setOnAction(event -> {

            itemsSearchResults.clear();

            if (!(searchBar.getText().trim().isEmpty())) {

                if((user.searchTracks(searchBar.getText().trim()).getItems().length !=0)) {

                    Paging<Track> tracks = user.searchTracks(searchBar.getText().trim());

                    if (!(itemsSearchResults.contains(searchBar.getText().trim()))) {

                        for (int i = 0; i < tracks.getItems().length; i++) {
                            Track[] song = tracks.getItems();

                            itemsSearchResults.add(song[i].getName());

                        }
                    }
                } else {

                    itemsSearchResults.add("");
                }
            }else {

                itemsSearchResults.add("");
            }
        });
        root.add(search,1 ,2);
        //----------------------------------------------------------------

        // Makes and Adds the 'X' button ---------------------------------
        Button closeButton = new Button("X");
        closeButton.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/30, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        closeButton.setOnAction(event -> {
            if(UserInterface.isTimerRunning()) {
                UserInterface.timerUpdate(false);
            }
            Platform.exit();
        });
        root.add(closeButton, 6, 0);
        //----------------------------------------------------------------

        // Makes and Adds the 'Leave' button -----------------------------
        Button leave = new Button("Leave");
        leave.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        leave.setOnAction(event -> {
            UserInterface.client.sendPacket(Utilities.generatePacketIdentifier(), 4, null, null, null);
            if(UserInterface.isTimerRunning()) {
                UserInterface.timerUpdate(false);
            }
            UserInterface.loadLandingPage();
        });
        root.add(leave, 5,5, 2,1);
        //----------------------------------------------------------------

        // Sets the alignment of the elements inside the GridPane --------
        GridPane.setHalignment(code, HPos.LEFT);
        GridPane.setValignment(code, VPos.TOP);

        GridPane.setHalignment(queue, HPos.CENTER);
        GridPane.setValignment(queue, VPos.CENTER);

        GridPane.setHalignment(closeButton, HPos.RIGHT);
        GridPane.setValignment(closeButton, VPos.TOP);

        GridPane.setHalignment(leave, HPos.CENTER);
        GridPane.setValignment(leave, VPos.CENTER);

        GridPane.setHalignment(add, HPos.CENTER);
        GridPane.setValignment(add, VPos.CENTER);

        GridPane.setHalignment(searchBar, HPos.RIGHT);
        GridPane.setValignment(searchBar, VPos.TOP);

        GridPane.setHalignment(search, HPos.LEFT);
        GridPane.setValignment(search, VPos.TOP);

        GridPane.setHalignment(searchResults, HPos.CENTER);
        GridPane.setValignment(searchResults,VPos.CENTER);

        GridPane.setHalignment(listSearchResults, HPos.CENTER);
        GridPane.setValignment(listSearchResults, VPos.CENTER);

        GridPane.setHalignment(playQueue, HPos.CENTER);
        GridPane.setValignment(playQueue,VPos.CENTER);

        GridPane.setHalignment(listPlayQueue, HPos.CENTER);
        GridPane.setValignment(listPlayQueue, VPos.CENTER);

        GridPane.setVgrow(listPlayQueue, Priority.ALWAYS);

        GridPane.setHalignment(stack, HPos.CENTER);
        GridPane.setValignment(stack, VPos.CENTER);
        //----------------------------------------------------------------

        root.setGridLinesVisible(false);

        return root;
    }

    public static void updateQueue() {

        itemsPlayQueue.clear();

        Paging<PlaylistTrack> tracks = LobbyUser.getPlayListTracks();

        for (int i = 0; i < tracks.getItems().length; i++) {
            PlaylistTrack[] song = tracks.getItems();

            itemsPlayQueue.add(song[i].getTrack().getName());
        }

        UserInterface.timerUpdate(true);

    }

    public static void updateCurrentPlaying() {

        Paging<PlaylistTrack> tracks = LobbyUser.getPlayListTracks();

        PlaylistTrack[] current = tracks.getItems();

        if(current != null && current.length != 0) {
            songTitle.setText("Song: " + current[0].getTrack().getName());

            artist.setText("Artist: " + current[0].getTrack().getArtists()[0].getName());

            album.setText("Album: " + current[0].getTrack().getAlbum().getName());
        }
    }

    public static void sendToLandingPage() {

        UserInterface.loadLandingPage();
    }

    public static boolean isInitialized() {
        if(user == null) return false;
        return true;
    }
}
