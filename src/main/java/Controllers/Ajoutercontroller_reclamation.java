package Controllers;

import entite.Reclamation;
import entite.User;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import Service.Reclamationservice;
import java.io.IOException;
import java.time.LocalDate;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import util.UserSession;

public class Ajoutercontroller_reclamation {

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private TextField txtdes;
    User user = UserSession.getInstance().getUser();

    @FXML
    void action_ajouter(ActionEvent event) {
        String type = comboType.getValue(); // Récupérer la valeur sélectionnée dans la ComboBox
        String description = txtdes.getText();
        String statut = "pas encore"; //  valeur par défaut
        LocalDate date = LocalDate.now();
        int id_user =user.getId();

        Reclamation Reclamation = new Reclamation(type, statut, description, date,id_user);
        Reclamationservice Reclamationservice = new Reclamationservice();
        Reclamationservice.insert(Reclamation);

        // Afficher un popup pour informer l'utilisateur de l'ajout réussi
        showPopup("Reclamation ajoutée", "La Reclamation a été ajoutée avec succès.");

        try {
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/fenetre1_reclamation.fxml"));
            Scene adminLoginScene = new Scene(adminLoginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminLoginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPopup(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
