package User;

import com.wrapper.spotify.model_objects.specification.User;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.Toolkit;


public class LandingPage {
    private static Scene landingScene = null;
    public LandingPage() {
        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        GridPane root = new GridPane();
        root.setGridLinesVisible(false);
        root.setStyle("-fx-background-color: transparent");

        // Sizes columns to the size of the screen
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setMinWidth(screenWidth / 3);
        column2.setMinWidth(screenWidth / 3);
        column3.setMinWidth(screenWidth / 3);
        column2.setHalignment(HPos.CENTER);

        // Sizes columns to the size of the screen
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row1.setMinHeight(screenHeight / 3);
        row2.setMinHeight(screenHeight / 3);
        row3.setMinHeight(screenHeight / 3);
        row1.setValignment(VPos.CENTER);
        row2.setValignment(VPos.CENTER);
        row3.setValignment(VPos.TOP);

        // Adds column and row contraints to the GridPane
        root.getColumnConstraints().addAll(column1, column2, column3);
        root.getRowConstraints().addAll(row1, row2, row3);

        // Title of the application
        Text title = new Text("PROJECT NEXT");
        title.resize(screenWidth / 8, screenHeight / 12);
        title.setFont(Font.font(Font.getDefault().toString(), screenWidth / 20));
        title.setFill(Color.WHITE);

        // Host button the connects to the host page
        Button host = new Button("Host");
        host.setPrefSize(screenWidth / 8, screenHeight / 12);
        host.setOnAction(event -> UserInterface.loadHostPage("AAAA"));

        // Join button that connects to the join page using the given code in the text field
        Button join = new Button("Join");
        join.setPrefSize(screenWidth / 8, screenHeight / 12);

        TextField joinCode = new TextField();
        joinCode.resize(screenWidth / 8, screenHeight / 12);
        VBox joinElements = new VBox();
        joinElements.setAlignment(Pos.TOP_CENTER);
        joinElements.getChildren().addAll(join, joinCode);

        join.setOnAction(event -> UserInterface.loadUserPage(joinCode.getText()));

        Button exit = new Button("X");
        exit.setOnAction(event -> Platform.exit());

        VBox exitFormat = new VBox();
        exitFormat.getChildren().add(exit);
        exitFormat.setAlignment(Pos.TOP_RIGHT);

        root.add(title, 1, 0);
        root.add(host, 1, 1);
        root.add(joinElements, 1, 2);
        root.add(exitFormat, 2, 0);


        landingScene = new Scene(root, 300, 200, Color.rgb(26, 83, 46));
    }

    //returns the scene to be used on the current window
    public Scene getScene() {
        return landingScene;
    }
}
