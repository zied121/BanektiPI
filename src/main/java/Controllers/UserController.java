package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import entite.Credit;
import Service.CreditService;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.DatabaseConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userController {

    @FXML
    private TextField idCompteField;

    @FXML
    private ComboBox<String> typeCreditCombo;

    @FXML
    private TextField montantField;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private TextField documentField;

    @FXML
    private TextField echeancierField;

    private CreditService creditService = new CreditService();

    @FXML
    public void handleValidate(ActionEvent actionEvent) {
        // Validate inputs
        String idCompte = idCompteField.getText();
        String typeCredit = typeCreditCombo.getValue();
        String montant = montantField.getText();
//        String statut = statusCombo.getValue();
        String document = documentField.getText();
        String echeancier = echeancierField.getText();

        if (idCompte.isEmpty() || typeCredit == null || montant.isEmpty()  || document.isEmpty() || echeancier.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(montant);
            // Create a new Credit object
            Credit credit = new Credit();
            credit.setIdCompte(Integer.parseInt(idCompte));
            credit.setTypeCredit(typeCredit);
            credit.setMontant(amount);
//            credit.setStatus(statut);
            credit.setDocument(document);
            credit.setEcheancier(echeancier);

            // Save credit using CreditService
            creditService.saveCredit(credit);

            showAlert("Success", "Credit validated successfully!");

        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Montant must be a valid number.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private static final String UPLOAD_DIR = "uploads/";

    @FXML
    public void handleUpload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try (Connection connection = DatabaseConnection.getInstance().getConnection();
                 FileInputStream inputStream = new FileInputStream(selectedFile)) {
                String query = "UPDATE credit SET document = ? WHERE id_compte = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setBinaryStream(1, inputStream, (int) selectedFile.length());
                statement.setString(2, idCompteField.getText());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "PDF uploaded successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload PDF.");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while uploading the PDF.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
