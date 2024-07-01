package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import entite.Credit;
import Service.CreditService;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;

public class userController  {

    @FXML
    private ComboBox<String> typeCreditCombo;
    @FXML
    private TextField idCompteField;

    @FXML
    private TextField montantField;
//    @FXML
//    private ComboBox<String> statusCombo;
    @FXML
    private TextField echeancierField;
    @FXML
    private TextField documentField;

    private CreditService creditService;

    public userController() {
        this.creditService = new CreditService();
    }

//    @FXML
//    public void initialize() {
//        statusCombo.getItems().addAll("Approved", "Pending", "Rejected");
//    }

    @FXML
    public void handleValidate(ActionEvent actionEvent) {
        String typeCredit = typeCreditCombo.getValue();
        String idCompte = idCompteField.getText();
        double montant = Double.parseDouble(montantField.getText());
//        String status = statusCombo.getValue();
        String echeancier = echeancierField.getText();
        String document = documentField.getText();

        boolean isUpdated = creditService.updateCreditDetailsuser(idCompte, typeCredit, montant, echeancier, document);

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
                // Upload the file to Cloudinary and get the URL
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

    @FXML
    public void handleGetAllCredits(ActionEvent actionEvent) {
        List<Credit> credits = creditService.getAllCredits();
        // Handle displaying credits, e.g., populate a TableView
        // This is just a placeholder to show the functionality
        credits.forEach(credit -> System.out.println(credit.toString()));
    }
}