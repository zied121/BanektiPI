package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file for the admin interface
            Parent root = FXMLLoader.load(getClass().getResource("/Main/user.fxml"));

            // Set the scene
            Scene scene = new Scene(root);

            // Set the stage properties
            primaryStage.setTitle("Bank App - Admin");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
