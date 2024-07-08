package Controllers;

import entite.Credit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import Service.CreditService;

public class CreditadminController {

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
    private String userId;

    public CreditadminController() {
        this.creditService = new CreditService();
    }

    @FXML
    public void initialize() {
        statusCombo.getItems().addAll("Approved", "Pending", "Rejected");
    }
    private String getAccountIdByUserId(String userId) {
        return creditService.getAccountIdByUserId(userId);
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
        // Get the user ID from the logged-in user (assume it's stored in a variable `userId`)
        String userId = getUserId(); // You need to implement this method to get the logged-in user's ID

        // Get the account ID using the user ID
        String idCompte = creditService.getAccountIdByUserId(userId);

        if (idCompte != null) {
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
        } else {
            showAlert("Error", "No account found for the provided User ID.");
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
            if ("Welcome to your agency".equals(statut)) {
                // Get other credit details
                String typeCredit = typeCreditCombo.getValue();
                double montant = Double.parseDouble(montantField.getText());
                String echeancier = echeancierField.getText();
                String document = documentField.getText();

                // Create a new Credit object
                Credit credit = new Credit(userId, typeCredit, montant, statut, echeancier, document);

                // Insert the new credit record into the credit table
                boolean isInserted = creditService.insertCredit(credit);

                if (isInserted) {
                    showAlert("Success", "Credit status updated and new credit inserted successfully!");
                } else {
                    showAlert("Error", "Failed to insert new credit.");
                }
            } else if ("Refused".equals(statut)) {
                // Delete the credit request from the demande table
                boolean isDeleted = creditService.deleteCreditRequest(idCompte);

                if (isDeleted) {
                    showAlert("Success", "Credit status updated and credit request deleted successfully!");
                } else {
                    showAlert("Error", "Failed to delete credit request.");
                }
            } else {
                showAlert("Success", "Credit status updated successfully!");
            }
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

    // Implement this method to get the logged-in user's ID
    private String getUserId() {
        // Assuming you have a session or some way to get the logged-in user's ID
        // This is just a placeholder
        return "logged_in_user_id";
    }


}
