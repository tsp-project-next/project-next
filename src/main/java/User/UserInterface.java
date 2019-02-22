package User;

import javafx.application.Application;
import javafx.stage.Stage;

public class UserInterface extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Project Next"); //application is currently called Project Next
        primaryStage.show();
        LandingPage landingPage = new LandingPage(); //creates the landing page
        primaryStage.setScene(landingPage.getScene()); //sets the window to contain the landing page
        primaryStage.setFullScreen(true); //sets the window to fullscreen
    }
}
