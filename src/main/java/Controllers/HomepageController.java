package Controllers;
import Service.CreditService;
import entite.Credit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HomepageController {
    @FXML
    private ListView<String> creditListView;

    private CreditService creditService;

    public HomepageController() {
        creditService = new CreditService();
    }

    @FXML
    public void initialize() {
        List<Credit> credits = creditService.getAllCredits();
        for (Credit credit : credits) {
            creditListView.getItems().add("ID Compte: " + credit.getIdCompte() +
                    ", Type Credit: " + credit.getTypeCredit() +
                    ", Montant: " + credit.getMontant() +
                    ", Status: " + credit.getStatus());
        }
    }
    private void handleAssuranceButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/Assurance.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Assurance Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
