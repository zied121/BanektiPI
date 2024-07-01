package Controllers;

import entite.Credit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Service.CreditService;

import java.util.List;

public class adminController {

    @FXML
    private TextField idCompteField;
    @FXML
    private ComboBox<String> typeCreditCombo;
    @FXML
    private TextField montantField;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private TextField echeancierField;
    @FXML
    private TextField documentField;

    private CreditService creditService;

    public adminController() {
        this.creditService = new CreditService();
    }

    @FXML
    public void initialize() {
        statusCombo.getItems().addAll("Approved", "Pending", "Rejected");
    }

    @FXML
    public void handleValidate(ActionEvent actionEvent) {
        String idCompte = idCompteField.getText();
        String status = statusCombo.getValue();
        String typeCredit = typeCreditCombo.getValue();

        boolean isUpdated = creditService.updateCreditDetails(idCompte,typeCredit, 0,status,  null, null);

        Alert alert = new Alert(isUpdated ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(isUpdated ? "Success" : "Error");
        alert.setHeaderText(isUpdated ? "Credit status updated successfully" : "Failed to update credit status");
        alert.showAndWait();
    }

    @FXML
    public void handleDownloadDocument(ActionEvent actionEvent) {
        String documentUrl = documentField.getText();
        if (documentUrl != null && !documentUrl.isEmpty()) {
            // Logic to download the file using the URL
            // This is a placeholder to show the functionality
            System.out.println("Download from URL: " + documentUrl);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            alert.setHeaderText("No document URL available");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCheckAccount(ActionEvent actionEvent) {
        // Validate input
        String idCompte = idCompteField.getText();

        if (idCompte.isEmpty()) {
            showAlert("Validation Error", "Please enter an Account ID.");
            return;
        }

        // Fetch credit details from the database
        Credit credit = creditService.getCreditByAccountId(idCompte);

        if (credit != null) {
            // Populate fields with data from the database
            idCompteField.setText(credit.getIdCompte());
            typeCreditCombo.setValue(credit.getTypeCredit());
            montantField.setText(String.valueOf(credit.getMontant()));
            statusCombo.setValue(credit.getStatus());
            documentField.setText(credit.getDocument());
            echeancierField.setText(credit.getEcheancier());
        } else {
            showAlert("Error", "No credit found with the provided Account ID.");
        }
    }

    @FXML
    public void handleUpdateStatus(ActionEvent actionEvent) {
        // Validate inputs
        String idCompte = idCompteField.getText();
        String statut = statusCombo.getValue();

        if (idCompte.isEmpty() || statut == null) {
            showAlert("Validation Error", "Please fill in the Account ID and Status.");
            return;
        }

        // Update the status of the credit in the database
        boolean isUpdated = creditService.updateCreditStatus(idCompte, statut);

        if (isUpdated) {
            showAlert("Success", "Credit status updated successfully!");
        } else {
            showAlert("Error", "Failed to update credit status.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
