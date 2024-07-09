package Controllers;

import entite.Credit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Service.CreditService;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.UserSession;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.List;

public class CreditadminController {
    @FXML
    private TextField userIdField;
    @FXML
    private ComboBox<String> accountComboBox;
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
        accountComboBox.setOnAction(this::handleAccountSelection);
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
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Document");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("document.pdf"); // You can customize the initial file name

            // Show save file dialog
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (InputStream in = new BufferedInputStream(new URL(documentUrl).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(file)) {

                    byte dataBuffer[] = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Download Success");
                    alert.setHeaderText("Document downloaded successfully");
                    alert.setContentText("Document saved to: " + file.getAbsolutePath());
                    alert.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Download Failed");
                    alert.setHeaderText("Failed to download document");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            alert.setHeaderText("No document URL available");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCheckAccount(ActionEvent actionEvent) {
        // Get the user ID from the TextField
        String userId = userIdField.getText();

        if (userId == null || userId.isEmpty()) {
            showAlert("Validation Error", "Please enter a User ID.");
            return;
        }

        // Fetch account details from the database using userId
        List<Credit> credits = creditService.getCreditsByUserId(userId);

        if (!credits.isEmpty()) {
            // Populate accountComboBox with account IDs
            accountComboBox.getItems().clear();
            for (Credit credit : credits) {
                accountComboBox.getItems().add(credit.getIdCompte());
            }
        } else {
            showAlert("Error", "No accounts found for the provided User ID.");
        }
    }

    @FXML
    private void handleAccountSelection(ActionEvent event) {
        String selectedAccountId = accountComboBox.getValue();
        if (selectedAccountId != null) {
            Credit credit = creditService.getCreditByAccountId(selectedAccountId);
            if (credit != null) {
                idCompteField.setText(credit.getIdCompte());
                typeCreditCombo.setValue(credit.getTypeCredit());
                montantField.setText(String.valueOf(credit.getMontant()));
                statusCombo.setValue(credit.getStatus());
                documentField.setText(credit.getDocument());
                echeancierField.setText(credit.getEcheancier());
            }
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
                Credit credit = new Credit(userId, idCompte,typeCredit, montant, statut, echeancier, document);

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

    @FXML
    private void handlehomeButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleusersButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/UserView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Users Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlecreditButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Credits Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleassuranceButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/Assurance_Admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Assurance Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handlesignout(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            UserSession.clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handledocumentButtonuser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre_admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Assurance Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handlereclamationButtonuser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/AdminChart.fxml"));
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
