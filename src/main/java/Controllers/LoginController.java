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
import entite.User;
import util.UserSession;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField clientCinField;

    @FXML
    private PasswordField clientpasswordField;

    private ClientAuthService clientAuthService = new ClientAuthService();

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String ClientCIN = clientCinField.getText();
        String ClientPassword = clientpasswordField.getText();
        User authenticatedUser = clientAuthService.authenticate(ClientCIN, ClientPassword);
        if (authenticatedUser != null) {
            UserSession.initSession(authenticatedUser);
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome " + authenticatedUser.getNom() + "!");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre1_reclamation.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Error", "Unable to load operation view.");
            }
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        clientCinField.clear();
        clientpasswordField.clear();
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
