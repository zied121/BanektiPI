// File: Controllers/LoginAdminController.java
package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Service.AdminAuthService;

import java.io.IOException;

public class LoginAdminController {

    @FXML
    private TextField adminEmailField;

    @FXML
    private PasswordField adminPasswordField;

    private AdminAuthService adminAuthService = new AdminAuthService();

    @FXML
    private void handleAdminLoginButtonAction(ActionEvent event) {
        String adminCin = adminEmailField.getText();
        String adminPassword = adminPasswordField.getText();

        if (adminAuthService.authenticateAdmin(adminCin, adminPassword)) {
            showAlert(AlertType.INFORMATION, "Admin Login Successful", "Welcome Admin " + adminCin + "!");
            redirectToGestionUser(event);
        } else {
            showAlert(AlertType.ERROR, "Admin Login Failed", "Invalid admin email or password.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void redirectToGestionUser(ActionEvent event) {
        try {
            Parent gestionUserRoot = FXMLLoader.load(getClass().getResource("/Main/AdminChart.fxml"));
            Scene gestionUserScene = new Scene(gestionUserRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(gestionUserScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClientRedirectAction(ActionEvent event) {
        try {
            // Ensure the correct path to the FXML file
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/login.fxml"));
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

