package Controllers;

import entite.Reclamation;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import Service.Reclamationservice;

public class Ajoutercontroller {

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private TextField txtdes;


//    String type_rec = txttype.getText();
//    String description = txtdes.getText();
//    String statut = txt_stat.getText();
//    //int id_user = Integer.parseInt(txtuser.getText());
//    LocalDate date_rec = LocalDate.now();
//
//    Reclamation reclamation = new Reclamation(type_rec, description, statut, date_rec );//, id_user);
//    Reclamationservice reclamationservice = new Reclamationservice();
//        reclamationservice.insertPST(reclamation);
//
//    FXMLLoader loader = new FXMLLoader(getClass().getResource("../AfficherReclamation.fxml"));
//try {
//        Parent root = loader.load();
//    }
//catch (Exception e) {
//        e.printStackTrace();
//    }
//}







    @FXML
    void action_ajouter(ActionEvent event) {
        String type = comboType.getValue(); // Récupérer la valeur sélectionnée dans la ComboBox
        String description = txtdes.getText();
        String statut = "not treated"; //  valeur par défaut
        LocalDate date = LocalDate.now();

        Reclamation reclamation = new Reclamation(type, description, statut, date);
        Reclamationservice reclamationservice = new Reclamationservice();
        reclamationservice.insertPST(reclamation);

        try {
            // Ensure the correct path to the FXML file
            Parent parent = FXMLLoader.load(getClass().getResource("/Main/UpdateDelete.fxml"));
            Scene parentScene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(parentScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
