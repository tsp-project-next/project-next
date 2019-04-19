package User;

import client.LobbyHost;
import com.wrapper.spotify.SpotifyHttpManager;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.Toolkit;
import java.net.URI;
import java.util.Optional;

@SuppressWarnings("Duplicates")
public class LandingPage {
    private static final String clientId = "ef5f89735e4649929f4e9eb8fac2db06";
    private static final String clientSecret = "f32ba2821de9409785f1abb637707170";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://tsp-project-next.github.io/");

    public LandingPage() {
        setup();
    }

    public static void setup() {

        double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        GridPane root = new GridPane();
        root.getStylesheets().add("scene.css");

        root.setMinSize(screenWidth, screenHeight);

        root.setPadding(new Insets(5));

        // Sizes rows adn columns to the size of the screen --------------
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(column1, column2, column1);
        root.getRowConstraints().addAll(row1, row1, row2);
        //----------------------------------------------------------------

        //Exit Button ----------------------------------------------------
        Button exit = new Button("X");
        exit.setOnAction(event -> {
            Platform.exit();
        });

        root.add(exit, 2, 0);
        //----------------------------------------------------------------

        // Title of the application---------------------------------------
        Text title = new Text("PROJECT NEXT");
        title.resize(screenWidth / 8, screenHeight / 12);
        title.setFont(Font.font(Font.getDefault().toString(), screenWidth / 20));
        title.setFill(Color.WHITE);

        root.add(title, 1, 0);
        //----------------------------------------------------------------

        // Host button the connects to the host page ---------------------
        Button host = new Button("Host");
        host.setPrefSize(screenWidth / 8, screenHeight / 12);
        host.setOnAction(event -> showInputTextDialog());

        root.add(host, 1, 1);
        //----------------------------------------------------------------

        //Join Button with Text Field, checks for validity of code -------
        Button join = new Button("Join");
        join.setPrefSize(screenWidth / 8, screenHeight / 12);
        join.setOnAction(event -> userPrompt());

        root.add(join, 1, 2);
        //----------------------------------------------------------------

        // Sets the alignment of the elements inside the GridPane --------
        GridPane.setHalignment(exit, HPos.RIGHT);
        GridPane.setValignment(exit,VPos.TOP);

        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title,VPos.CENTER);

        GridPane.setHalignment(host, HPos.CENTER);
        GridPane.setValignment(host, VPos.CENTER);

        GridPane.setHalignment(join, HPos.CENTER);
        GridPane.setValignment(join, VPos.TOP);
        //----------------------------------------------------------------

        UserInterface.getStage().getScene().setRoot(root);
    }

    private static void showInputTextDialog() {
        LobbyHost host = new LobbyHost(clientId, clientSecret, redirectUri);

        try {
            host.visitURI();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("URL Input");
        dialog.setHeaderText("Input");
        dialog.setContentText("Please enter the URL:");

        Optional<String> result = dialog.showAndWait();

        if (!(result.toString().trim().equals("Optional[]"))) {
            if (result.isPresent()) {
                UserInterface.loadHostPage(result.toString(), host);
            }
        }
    }

    private static void userPrompt () {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Join Code");
        dialog.setHeaderText("Input");
        dialog.setContentText("Please enter the Code:");

        Optional<String> result = dialog.showAndWait();

        Alert invalidCode = new Alert(Alert.AlertType.INFORMATION);
        invalidCode.initOwner(UserInterface.getStage());
        invalidCode.setContentText("Invalid Code");
        invalidCode.setHeaderText("Alert");
        invalidCode.setTitle("Alert");

        Alert empty = new Alert(Alert.AlertType.INFORMATION);
        empty.initOwner(UserInterface.getStage());
        empty.setContentText("No Input");
        empty.setHeaderText("Alert");
        empty.setTitle("Alert");

        Alert shortCode = new Alert(Alert.AlertType.INFORMATION);
        shortCode.initOwner(UserInterface.getStage());
        shortCode.setContentText("Code is too short");
        shortCode.setHeaderText("Alert");
        shortCode.setTitle("Alert");

        if(result.isPresent()) {
            if (result.get().trim().length() == 4) {
                UserInterface.loadUserPage(result.get().trim());
            } else {
                shortCode.show();
            }
        } else {
            empty.show();
        }
    }
}
