package User;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LandingPage {
    private static Scene landingScene = null;
    public LandingPage() {
        //filler code at the moment
        StackPane root = new StackPane();
        landingScene = new Scene(root, 300, 200, Color.rgb(		26, 83, 46));
        BorderPane root = new BorderPane();
        VBox options = new VBox();

        Text title = new Text("PROJECT NEXT");
        title.setFont(Font.getDefault());

        Button host = new Button("Host");
        host.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 8, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 12);

        Button join = new Button("Join");
        join.setPrefSize(Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 8, Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 12);

        options.getChildren().add(title);
        options.getChildren().add(host);
        options.getChildren().add(join);
        options.setAlignment(Pos.CENTER);

        root.setCenter(options);
        landingScene = new Scene(root, 300, 200, Color.FORESTGREEN);
    }

    //returns the scene to be used on the current window
    public Scene getScene() {
        return landingScene;
    }
}
