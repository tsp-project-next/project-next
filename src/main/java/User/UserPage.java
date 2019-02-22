package User;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class UserPage {

    private static Scene UserScene = null;
    public UserPage() {
        //filler code at the moment
        StackPane root = new StackPane();
        UserScene = new Scene(root, 300, 200, Color.FORESTGREEN);
    }

    //returns the scene to be used on the current window
    public Scene getScene() {
        return UserScene;
    }
}
