package User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.awt.*;


public class HostPage {

    private static Scene hostPage = null;
    private String code;
    private int fontSize = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 40;
    private Font standard = Font.font("times new roman", FontWeight.LIGHT, FontPosture.REGULAR, fontSize);

    public HostPage(String code) {
        this.code = code;
        hostPage = new Scene(setup(code), 300, 200, Color.rgb(26, 83, 46));
    }
    public Scene getScene() { return hostPage; }

    private GridPane setup(String code) {

        // Setting up the formatting for the main grid controller--------
        GridPane controller = new GridPane();
        controller.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
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
        endSession.setOnAction(event -> Platform.exit());
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
        blacklist.getRowConstraints().addAll(row1, row2);

        Text blacklistText = new Text("Blacklist:");
        blacklistText.setFill(Color.WHITE);
        blacklistText.setFont(standard);
        blacklist.add(blacklistText, 0, 0);

        ScrollPane blacklistItems = new ScrollPane();
        ListView<String> bLList = new ListView<>();
        ObservableList<String> bLObsList = FXCollections.observableArrayList(
                "Test One", "Test Two", "Test Three", "Test Four");
        bLList.setItems(bLObsList);
        blacklistItems.setContent(bLList);
        blacklistItems.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        blacklistItems.setFitToHeight(true);
        blacklistItems.setFitToWidth(true);
        blacklistItems.setMaxWidth((1*Toolkit.getDefaultToolkit().getScreenSize().getWidth())/4);
        blacklist.add(blacklistItems, 0, 1);
        blacklist.setAlignment(Pos.CENTER);

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
