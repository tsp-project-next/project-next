package User;

import client.LobbyUser;
import com.wrapper.spotify.model_objects.specification.Paging;
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

    private static Scene UserScene = null;
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";

    // Makes and Adds all elements of the UserPage to the UserPage
    public UserPage(String Code) {

        LobbyUser user = new LobbyUser(clientId, clientSecret);

        // Makes a GridPane Called Root
        GridPane root = new GridPane();
        root.setPadding(new Insets(5));
        root.setHgap(10);
        root.setVgap(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        root.setMinSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        root.setStyle("-fx-background-color: transparent");

        // Makes a VBox called stack
        VBox stack = new VBox();

        // Sets col and row constraints to grow or not
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

        //Changes the root of the stage's scene
        UserInterface.getStage().getScene().setRoot(root);

        // Start text zones, font is for scaling font sizes
        int font = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/40;

        // Makes the code displayer for top left of the screen
        Text code = new Text();
        code.setText("Code:" + Code);
        code.setFill(Color.WHITE);
        code.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));
        root.add(code,0, 0);

        // Makes the header for the queue scroll panel
        Text queue = new Text();
        queue.setText("Play Queue");
        queue.setFill(Color.WHITE);
        queue.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));
        root.add(queue,5, 2, 2, 1);

        // Makes and Adds the search bar to the header of the search box and displays the prompt text 'Searching....'
        TextField searchBar = new TextField();
        searchBar.setMaxSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        searchBar.setPromptText("Search....");
        root.add(searchBar, 0,2);

        // Makes a ScrollPane called searchResults and adds it to the left of the screen with the list
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

        // Makes a ScrollPane called playQueue and adds it to the right of the screen with the list
        ScrollPane playQueue = new ScrollPane();
        ListView<String> listPlayQueue = new ListView<>();
        ObservableList<String> itemsPlayQueue = FXCollections.observableArrayList("");
        listPlayQueue.setItems(itemsPlayQueue);
        playQueue.setContent(listPlayQueue);
        playQueue.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playQueue.setFitToHeight(true);
        playQueue.setFitToWidth(true);
        playQueue.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        root.add(playQueue, 5,3,2,2);

        String Song = "Song Title: ";
        String Artist = "Artist: ";
        String Album = "Album: ";

        if (!(itemsPlayQueue.get(0).equals(""))) {

            Song += itemsPlayQueue.get(0);
            Artist += itemsPlayQueue.get(0);
            Album += itemsPlayQueue.get(0);
        }

        // Makes the header for the middle of the screen information for the current song
        Text playing = new Text();
        playing.setText("Playing ");
        playing.setFill(Color.WHITE);
        playing.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Makes the song title displayer
        Text songTitle = new Text();
        songTitle.setText(Song);
        songTitle.setFill(Color.WHITE);
        songTitle.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Makes the artist displayer
        Text artist = new Text();
        artist.setText(Artist);
        artist.setFill(Color.WHITE);
        artist.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Makes the album displayer
        Text album = new Text();
        album.setText(Album);
        album.setFill(Color.WHITE);
        album.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Sets the spacing between the displayer's
        stack.setSpacing(40);

        // Adds the diplayers to the middle of the screen
        stack.getChildren().addAll(playing, songTitle, artist, album);
        stack.setAlignment(Pos.TOP_LEFT);
        root.add(stack, 3, 3, 2,2);

        // Makes and Adds the 'Add' button centered and under the search displayer
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

                        // This is for test in local queue
                        // Change this to add to Spotify queue
                        // song[itemsSearchResults.indexOf(listSearchResults.getSelectionModel().getSelectedItem())].getUri()
                        itemsPlayQueue.add(listSearchResults.getSelectionModel().getSelectedItem().trim());

                    }
                }
            }
        });
        root.add(add, 0,5, 2,1);

        // Makes and Adds the 'Search' button to the right of the search text field in the header of the search display box and takes the searchText and
        // searched Spotify returning the results to the search
        Button search = new Button("Search");
        search.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/15, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        search.setOnAction(event -> {

            itemsSearchResults.clear();

            if (!(searchBar.getText().trim().isEmpty())) {

                if((user.searchTracks(searchBar.getText().trim()).getItems().length !=0)) {

                    Paging<Track> tracks = user.searchTracks(searchBar.getText().trim());

                    if (!(itemsSearchResults.contains(searchBar.getText().trim()))) {

                        for (int i = 0; i <= 9; i++) {
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

        // Makes and Adds the 'X' button to close the program to the top right of the screen
        Button closeButton = new Button("X");
        closeButton.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/30, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        closeButton.setOnAction(event -> Platform.exit());
        root.add(closeButton, 6, 0);

        // Makes and Adds the 'Leave' button centered and under the queue displayer
        Button leave = new Button("Leave");
        leave.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        leave.setOnAction(event -> UserInterface.loadLandingPage());
        root.add(leave, 5,5, 2,1);

        // Sets the alignment of the elements inside the GridPane
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

        root.setGridLinesVisible(false);
    }
}
