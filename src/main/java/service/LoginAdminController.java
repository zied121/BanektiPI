package service;

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
import util.DatabaseUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginAdminController {

    @FXML
    private TextField adminEmailField;

    @FXML
    private PasswordField adminPasswordField;

    @FXML
    private void handleAdminLoginButtonAction(ActionEvent event) {
        String adminEmail = adminEmailField.getText();
        String adminPassword = adminPasswordField.getText();

        if (authenticateAdmin(adminEmail, adminPassword)) {
            showAlert(AlertType.INFORMATION, "Admin Login Successful", "Welcome Admin " + adminEmail + "!");
            redirectToGestionUser(event);
        } else {
            showAlert(AlertType.ERROR, "Admin Login Failed", "Invalid admin email or password.");
        }
    }

    private boolean authenticateAdmin(String adminEmail, String adminPassword) {
        String sql = "SELECT p.id FROM persone p " +
                "JOIN Compte c ON p.id = c.person_id " +
                "WHERE p.email = ? AND c.password = ? AND p.role = 'admin'";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, adminEmail);
            statement.setString(2, adminPassword);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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
            Parent gestionUserRoot = FXMLLoader.load(getClass().getResource("GestionUser.fxml"));
            Scene gestionUserScene = new Scene(gestionUserRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(gestionUserScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
