package User;

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

    // Makes and Adds all elements of the UserPage to the UserPage
    public UserPage() {


        String Code = "";
        String Song = "";
        String Artist = "";
        String Album = "";


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

        // Makes a Userscence set to fit to screen size with a background of darker green
        UserScene = new Scene(root, Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight(),  Color.rgb(26,83,46));

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

        // Makes the header for the middle of the screen information for the current song
        Text playing = new Text();
        playing.setText("Playing ");
        playing.setFill(Color.WHITE);
        playing.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Makes the song title displayer
        Text songTitle = new Text();
        songTitle.setText("Song Title: " + Song);
        songTitle.setFill(Color.WHITE);
        songTitle.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Makes the artist displayer
        Text artist = new Text();
        artist.setText("Artist: " + Artist);
        artist.setFill(Color.WHITE);
        artist.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Makes the album displayer
        Text album = new Text();
        album.setText("Album: " + Album);
        album.setFill(Color.WHITE);
        album.setFont(Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, font));

        // Sets the spacing between the displayer's
        stack.setSpacing(40);

        // Adds the diplayers to the middle of the screen
        stack.getChildren().addAll(playing, songTitle, artist, album);
        stack.setAlignment(Pos.TOP_LEFT);
        root.add(stack, 3, 3, 2,2);

        // Makes and Adds the search bar to the header of the search box and displays the prompt text 'Searching....'
        TextField searchBar = new TextField();
        searchBar.setMaxSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        searchBar.setPromptText("Search....");
        root.add(searchBar, 0,2);

        // Makes a ScrollPane called searchResults and adds it to the left of the screen with the list
        ScrollPane searchResults = new ScrollPane();
        ListView<String> listSearchResults = new ListView<>();
        ObservableList<String> itemsSearchResults = FXCollections.observableArrayList(
                "Single Ladies", "Halo", "Take a Bow", "Diamonds");
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
        ObservableList<String> itemsPlayQueue = FXCollections.observableArrayList(
                "first", "second", "third", "fourth");
        listPlayQueue.setItems(itemsPlayQueue);
        playQueue.setContent(listPlayQueue);
        playQueue.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        playQueue.setFitToHeight(true);
        playQueue.setFitToWidth(true);
        playQueue.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        root.add(playQueue, 5,3,2,2);

        // Makes and Adds the 'X' button to close the program to the top right of the screen
        Button closeButton = new Button("X");
        closeButton.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/30, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        closeButton.setOnAction(event -> Platform.exit());
        root.add(closeButton, 6, 0);

        // Makes and Adds the 'Add' button centered and under the search displayer
        Button add = new Button("Add");
        add.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/10, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        add.setOnAction(event -> {

            if(!(itemsPlayQueue.contains(listSearchResults.getSelectionModel().getSelectedItem()))) {

                itemsPlayQueue.add(listSearchResults.getSelectionModel().getSelectedItem());
            }
        });
        root.add(add, 0,5, 2,1);

        // Makes and Adds the 'Search' button to the right of the search text field in the header of the search display box and takes the searchText and
        // searched Spotify returning the results to the search
        Button search = new Button("Search");
        search.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/15, Toolkit.getDefaultToolkit().getScreenSize().getHeight() /30);
        search.setOnAction(event -> {

            if(!(itemsSearchResults.contains(searchBar.getText()))) {

                itemsSearchResults.add(searchBar.getText());
            }
        });
        root.add(search,1 ,2);

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

    //returns the scene to be used on the current window
    public Scene getScene() {
        return UserScene;
    }
}
