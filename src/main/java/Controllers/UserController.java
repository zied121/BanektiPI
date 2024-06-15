package Controllers;

import entite.User;
import entite.Compte;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    private TextField roleField;
    @FXML
    private TextField nbCompteField;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        mdpColumn.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        nbCompteColumn.setCellValueFactory(new PropertyValueFactory<>("nbCompte"));

        userTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                onEdit();
            } else if (event.getClickCount() == 2) {
                onShowCompteDetails();
            }
        });

        try {
            loadUsers();
        } catch (SQLException e) {
            e.printStackTrace();
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
        User user = new User();
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        user.setMdp(mdpField.getText());
        user.setCin(Integer.parseInt(cinField.getText()));
        userService.createUser(user);
        showAlert(AlertType.INFORMATION, "User Created", "User created successfully!");
        loadUsers();
    }

    @FXML
    private void updateUser() throws SQLException {
        User user = new User();
        user.setId(Integer.parseInt(userIdField.getText()));
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        user.setMdp(mdpField.getText());
        user.setCin(Integer.parseInt(cinField.getText()));

        userService.updateUser(user);
        showAlert(AlertType.INFORMATION, "User Updated", "User updated successfully!");
        loadUsers();
    }

    @FXML
    private void deleteUser() throws SQLException {
        int userId = Integer.parseInt(userIdField.getText());
        userService.deleteUser(userId);
        showAlert(AlertType.INFORMATION, "User Deleted", "User deleted successfully!");
        loadUsers();
    }

    @FXML
    private void clearFields() {
        userIdField.clear();
        nomField.clear();
        prenomField.clear();
        ageField.clear();
        mdpField.clear();
        cinField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
