package tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReclamation.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400); // Correspondre aux dimensions spécifiées dans FXML
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Réclamation");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Utiliser printStackTrace pour des détails complets
        }
    }
}
