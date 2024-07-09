package Controllers;

import entite.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import Service.CreditService;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    private User user;

    public CreditUserController() {
        this.creditService = new CreditService();
    }

    public void initialize() {
        User user = UserSession.getInstance().getUser();
        this.userId = String.valueOf(user.getId());
        System.out.println("User ID: " + user.getId());
    }

    @FXML
    public void handleValidate(ActionEvent actionEvent) {
        if (validateInput()) {
            String typeCredit = typeCreditCombo.getValue();
            String idCompte = idCompteField.getText();
            String status = statusCombo.getValue();
            String montant = montantField.getText();
            String echeancier = echeancierField.getText();
            String document = documentField.getText();

            boolean isUpdated = creditService.updateCreditDetailsuser(idCompte, typeCredit, Double.valueOf(montant), status, echeancier, document, Integer.valueOf(this.userId));

            Alert alert = new Alert(isUpdated ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(isUpdated ? "Success" : "Error");
            alert.setHeaderText(isUpdated ? "Credit details updated successfully" : "Failed to update credit details");
            alert.showAndWait();
        }
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (typeCreditCombo.getValue() == null || typeCreditCombo.getValue().trim().isEmpty()) {
            errorMessage += "No valid credit type selected!\n";
        }
        if (idCompteField.getText() == null || idCompteField.getText().trim().isEmpty()) {
            errorMessage += "No valid account ID!\n";
        }
        if (montantField.getText() == null || montantField.getText().trim().isEmpty()) {
            errorMessage += "No valid amount!\n";
        } else {
            try {
                Double.parseDouble(montantField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid amount (must be a number)!\n";
            }
        }
        if (statusCombo.getValue() == null || statusCombo.getValue().trim().isEmpty()) {
            errorMessage += "No valid status selected!\n";
        }
        if (echeancierField.getText() == null || echeancierField.getText().trim().isEmpty()) {
            errorMessage += "No valid schedule!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Invalid Fields", errorMessage);
            return false;
        }
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

    @FXML
    private void handlehomeButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/homePage.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/user.fxml"));
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

    @FXML
    private void handleoperationButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/operation_view.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Operation Management");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Document Management");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre1_reclamation.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Reclamation Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
