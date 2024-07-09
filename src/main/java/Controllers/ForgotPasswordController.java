package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import Service.ClientAuthService;

import java.io.IOException;
import java.sql.SQLException;
import javax.mail.MessagingException;

public class ForgotPasswordController {

    @FXML
    private TextField cinField;

    private ClientAuthService clientAuthService = new ClientAuthService();

    @FXML
    private void handleSendResetEmailAction(ActionEvent event) {
        String cin = cinField.getText();
        try {
            clientAuthService.sendPasswordByEmail(cin);
            showAlert(AlertType.INFORMATION, "Success", "Password reset email sent successfully.");
        } catch (SQLException | MessagingException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to send password reset email.");
        }
    }

    @FXML
    private void handleBackToLoginAction(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/Main/login.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Unable to load login screen.");
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
