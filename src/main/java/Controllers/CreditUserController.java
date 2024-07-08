package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import Service.CreditService;
import javafx.stage.FileChooser;
import util.UserSession;

import java.io.*;


public class CreditUserController {

    @FXML
    private ComboBox<String> typeCreditCombo;
    @FXML
    private TextField idCompteField;

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

    public CreditUserController() {
        this.creditService = new CreditService();
    }

    public void initialize() {
        this.userId = String.valueOf(UserSession.getInstance().getUserId());
        showAlert("Error", this.userId);
        //String accountId = getAccountIdByUserId(userId);
//        if (accountId != null) {
//            idCompteField.setText(accountId);
//        } else {
//            showAlert("Error", "No account found for the provided User ID.");
//        }
    }
//    private String getAccountIdByUserId(String userId) {
//        return creditService.getAccountIdByUserId(userId);
//    }
        @FXML
        public void handleValidate(ActionEvent actionEvent) {
            String typeCredit = typeCreditCombo.getValue();
            String idCompte = idCompteField.getText();
            String status = statusCombo.getValue();
            double montant = Double.parseDouble(montantField.getText());
            String echeancier = echeancierField.getText();
            String document = documentField.getText();

            boolean isUpdated = creditService.updateCreditDetailsuser(idCompte, typeCredit, String.valueOf(montant), status,echeancier, document,this.userId);

            Alert alert = new Alert(isUpdated ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(isUpdated ? "Success" : "Error");
            alert.setHeaderText(isUpdated ? "Credit details updated successfully" : "Failed to update credit details");
            alert.showAndWait();
        }

        @FXML
        public void handleUploadDocument(ActionEvent actionEvent) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    String documentUrl = creditService.uploadPdfToCloudinary(file);
                    documentField.setText(documentUrl);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upload Success");
                    alert.setHeaderText("Document uploaded successfully");
                    alert.setContentText("Document URL: " + documentUrl);
                    alert.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Upload Failed");
                    alert.setHeaderText("Failed to upload document");
                    alert.showAndWait();
                }
            }
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

    public void setUserId(String userId) {
    }
}