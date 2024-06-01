// File: Controllers/LoginController.java
package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import Service.ClientAuthService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField EmailField;

    @FXML
    private PasswordField passwordField;

    private ClientAuthService clientAuthService = new ClientAuthService();

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String CIN = EmailField.getText();
        String password = passwordField.getText();

        if (clientAuthService.authenticate(CIN, password)) {
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome " + CIN + "!");
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        EmailField.clear();
        passwordField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAdminLoginButtonAction(ActionEvent event) {
        try {
            // Ensure the correct path to the FXML file
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/login-admin.fxml"));
            Scene adminLoginScene = new Scene(adminLoginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminLoginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Unable to load admin login screen.");
        }
    }
}
