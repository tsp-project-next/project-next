package User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.Toolkit;


public class LandingPage {
    private static Scene landingScene = null;
    public LandingPage() {
        BorderPane root = new BorderPane();
        VBox options = new VBox();

        root.setStyle("-fx-background-color: #1A532E");
        options.setStyle("-fx-background-color: #1A532E");

        //Text title = new Text("PROJECT NEXT");
        //title.resize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 8, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 12);

        Button host = new Button("Host");
        host.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 8, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 12);

        Button join = new Button("Join");
        join.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 8, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 12);

        TextField joinCode = new TextField();
        joinCode.setPrefWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 8);

        VBox joinOpt = new VBox();
        joinOpt.setAlignment(Pos.CENTER);
        joinOpt.getChildren().add(join);
        joinOpt.getChildren().add(joinCode);

        options.getChildren().add(host);
        options.getChildren().add(joinOpt);
        options.setAlignment(Pos.CENTER);

        options.setSpacing(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 20);

        root.setCenter(options);

        landingScene = new Scene(root, 300, 200, Color.rgb(26, 83, 46));
    }

    //returns the scene to be used on the current window
    public Scene getScene() {
        return landingScene;
    }
}
