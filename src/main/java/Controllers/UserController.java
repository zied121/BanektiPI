package Controllers;

import entite.User;
import Service.UserServiceInterface;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import util.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public class UserController {
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> nomColumn;
    @FXML
    private TableColumn<User, String> prenomColumn;
    @FXML
    private TableColumn<User, Integer> ageColumn;
    @FXML
    private TableColumn<User, String> mdpColumn;
    @FXML
    private TableColumn<User, Integer> cinColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Integer> nbCompteColumn;

    @FXML
    private TextField userIdField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField mdpField;
    @FXML
    private TextField cinField;
    @FXML
    private TextField EmailField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private DatePicker birthdatePicker;

    private UserServiceInterface userService = new UserService();

    @FXML
    public void initialize() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        mdpColumn.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        nbCompteColumn.setCellValueFactory(new PropertyValueFactory<>("nbCompte"));

        userTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                onEdit();
            } else if (event.getClickCount() == 2) {
                onShowCompteDetails();
            }
        });

        birthdatePicker.valueProperty().addListener((observable, oldValue, newValue) -> calculateAndSetAge(newValue));

        try {
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void calculateAndSetAge(LocalDate birthdate) {
        if (birthdate != null) {
            int age = Period.between(birthdate, LocalDate.now()).getYears();
            ageField.setText(String.valueOf(age));
        }
    }

    private void onEdit() {
        if (userTable.getSelectionModel().getSelectedItem() != null) {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            userIdField.setText(String.valueOf(selectedUser.getId()));
            nomField.setText(selectedUser.getNom());
            prenomField.setText(selectedUser.getPrenom());
            ageField.setText(String.valueOf(selectedUser.getAge()));
            mdpField.setText(selectedUser.getMdp());
            cinField.setText(String.valueOf(selectedUser.getCin()));
            roleComboBox.setValue(selectedUser.getRole());
            EmailField.setText(selectedUser.getEmail());
            birthdatePicker.setValue(null);  // Reset DatePicker, or set to a saved value if available
        }
    }

    private void onShowCompteDetails() {
        try {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/CompteView.fxml"));
                Parent root = loader.load();

                CompteController compteController = loader.getController();
                compteController.setUser(selectedUser);
                compteController.loadComptes(selectedUser.getId());

                Stage stage = new Stage();
                stage.setTitle("Compte Details");
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        userTable.getItems().setAll(users);
    }

    @FXML
    private void createUser() throws SQLException {
        if (!isCINValid(cinField.getText())) {
            showAlert(AlertType.ERROR, "Invalid CIN", "CIN must be exactly 8 digits.");
            return;
        }
        if (!isEmailValid(EmailField.getText())) {
            showAlert(AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        int cin = Integer.parseInt(cinField.getText());
        if (userService.isCINExists(cin)) {
            showAlert(AlertType.ERROR, "Duplicate CIN", "A user with this CIN already exists.");
            return;
        }

        User user = new User();
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        user.setMdp(mdpField.getText());
        user.setCin(cin);
        user.setRole(roleComboBox.getValue());
        user.setEmail(EmailField.getText());
        userService.createUser(user);
        showAlert(AlertType.INFORMATION, "User Created", "User created successfully!");
        loadUsers();
    }

    @FXML
    private void updateUser() throws SQLException {
        if (!isCINValid(cinField.getText())) {
            showAlert(AlertType.ERROR, "Invalid CIN", "CIN must be exactly 8 digits.");
            return;
        }
        if (!isEmailValid(EmailField.getText())) {
            showAlert(AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        User user = new User();
        user.setId(Integer.parseInt(userIdField.getText()));
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        user.setMdp(mdpField.getText());
        user.setCin(Integer.parseInt(cinField.getText()));
        user.setRole(roleComboBox.getValue());
        user.setEmail(EmailField.getText());
        userService.updateUser(user);
        showAlert(AlertType.INFORMATION, "User Updated", "User updated successfully!");
        loadUsers();
    }

    @FXML
    private void deleteUser() throws SQLException {
        try {
            int userId = Integer.parseInt(userIdField.getText());
            userService.deleteUser(userId);
            showAlert(AlertType.INFORMATION, "User Deleted", "User deleted successfully!");
            loadUsers();
        } catch (SQLException e) {
            if (e.getMessage().contains("Cannot delete user because they have associated accounts")) {
                showAlert(AlertType.ERROR, "Delete Error", "Cannot delete user because they have associated accounts.");
            } else {
                showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clearFields() {
        userIdField.clear();
        nomField.clear();
        prenomField.clear();
        ageField.clear();
        mdpField.clear();
        cinField.clear();
        roleComboBox.setValue(null);
        EmailField.clear();
        birthdatePicker.setValue(null);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isCINValid(String cin) {
        return cin != null && cin.matches("\\d{8}");
    }

    private boolean isEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    @FXML
    private void sendCredentials() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userService.sendCredentials(selectedUser);
            showAlert(AlertType.INFORMATION, "Email sent", "Email has been sent successfully!");
        } else {
            showAlert(AlertType.INFORMATION, "Select User", "Please click on the user you want to send the email to.");
        }
    }
    @FXML
    private void handlehomeButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/UserView.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre2.fxml"));
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
