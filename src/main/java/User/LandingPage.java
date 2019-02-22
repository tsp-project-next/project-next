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
        landingScene = new Scene(root, 300, 200, Color.FORESTGREEN);
    }

    //returns the scene to be used on the current window
    public Scene getScene() {
        return landingScene;
    }
}
