package Controllers;

import entite.Demande;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import Service.Demandeservice;

import java.time.LocalDate;

public class Ajoutercontroller {

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private TextField txtdes;

    @FXML
    void action_ajouter() {
        String type = comboType.getValue(); // Récupérer la valeur sélectionnée dans la ComboBox
        String description = txtdes.getText();
        String statut = "pas encore"; // Utilisation de la valeur par défaut
        LocalDate date = LocalDate.now();

        Demande reclamation = new Demande(type, statut, description, date);
        Demandeservice demandeservice = new Demandeservice();
        demandeservice.insert(reclamation);

        // Charger une autre vue ou effectuer d'autres actions si nécessaire
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fenetre2.fxml"));
        try {
            Parent root = loader.load();
            // Autres actions après chargement de la vue
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
