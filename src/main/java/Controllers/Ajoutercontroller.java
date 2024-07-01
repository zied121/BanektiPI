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

        // je veut passer a la fenetre suivante fenetre2.fxml qui a le controller modifier_supprimer_controller
        try {
            // Ensure the correct path to the FXML file
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/fenetre2.fxml"));
            Scene adminLoginScene = new Scene(adminLoginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminLoginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
