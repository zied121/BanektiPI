// File: Controllers/LoginController.java
package Controllers;

import entite.User;
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
import Service.UserService;
import util.UserSession;

import java.io.IOException;

public class LoginController {
    public LoginController() {
        UserService userService = new UserService();
    }
    @FXML
    private TextField EmailField;

    @FXML
    private PasswordField passwordField;


    private final ClientAuthService clientAuthService = new ClientAuthService();

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        String CIN = EmailField.getText();
        String password = passwordField.getText();

        if (clientAuthService.authenticate(CIN, password)) {
            User userDetails = UserService.getUserIdByCIN(CIN);
            if (userDetails != null) {
                UserSession.getInstance().setUserId(userDetails.getId());
                String userId = String.valueOf(UserSession.getInstance().getUserId());
                UserSession.getInstance().setUserRole(userDetails.getRole());
                String role = UserSession.getInstance().getUserRole();
                showAlert(AlertType.INFORMATION, "Login Successful", "Welcome " + CIN + "!");
                try {
                    // Ensure the correct path to the FXML file
                    Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/homePage.fxml"));
                    Scene adminLoginScene = new Scene(adminLoginRoot);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(adminLoginScene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(AlertType.ERROR, "Error", "Unable to load admin login screen.");
                }

//                // Load the admin interface after successful login
//                if ("admin".equals(role)) {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/admin.fxml"));
//                    Parent root = loader.load();
//                    Scene scene = new Scene(root);
//
//                    // Get the controller and pass the userId
//                   AssuranceController AssuranceController = loader.getController();
//                    AssuranceController.setUserId(userId);
//
//                    // Set the scene
//                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                    stage.setScene(scene);
//                    stage.show();
//                } else if ("client".equals(role)) {
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/Assurance.fxml"));
//                    Parent root = loader.load();
//                    Scene scene = new Scene(root);
//
//                    // Get the controller and pass the userId
//                    AssuranceController assuranceController = loader.getController();
//                    assuranceController.setUserId(userId);
//
//                    // Set the scene
//                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                    stage.setScene(scene);
//                    stage.show();
//                }
//            } else {
//                showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
//            }
            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
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
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/Assurance.fxml"));
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
