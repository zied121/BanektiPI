package Controllers;

import entite.Demande;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import Service.Demandeservice;
import java.io.IOException;
import java.time.LocalDate;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Ajoutercontroller {

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private TextField txtdes;

    @FXML
    void action_ajouter(ActionEvent event) {
        String type = comboType.getValue(); // Récupérer la valeur sélectionnée dans la ComboBox
        String description = txtdes.getText();
        String statut = "pas encore"; //  valeur par défaut
        LocalDate date = LocalDate.now();

        Demande demande = new Demande(type, statut, description, date);
        Demandeservice demandeservice = new Demandeservice();
        demandeservice.insert(demande);

        // Afficher un popup pour informer l'utilisateur de l'ajout réussi
        showPopup("Demande ajoutée", "La demande a été ajoutée avec succès.");

        // Passer à la fenêtre suivante fenetre2.fxml qui a le controller modifier_supprimer_controller
        try {
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/fenetre2.fxml"));
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
