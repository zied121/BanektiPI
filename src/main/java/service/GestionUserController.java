package service;

import entite.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionUserController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> typeColumn;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField typeField;

    private ObservableList<User> userList;

    @FXML
    private void initialize() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        userList = FXCollections.observableArrayList();
        userTable.setItems(userList);

        loadUsers();
    }

    private void loadUsers() {
        String sql = "SELECT p.person_id, p.first_name, p.last_name, p.email, p.role FROM person p JOIN client c ON p.person_id = c.person_id";
        userList.clear();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("person_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String type = resultSet.getString("role");

                userList.add(new User(id, firstName, lastName, email, type));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String type = typeField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || type.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "Please fill in all fields");
            return;
        }

        String sqlPerson = "INSERT INTO person (first_name, last_name, email, role) VALUES (?, ?, ?, ?)";
        String sqlClient = "INSERT INTO client (person_id, subscription_type) VALUES (LAST_INSERT_ID(), 'Basic')";
        String sqlCompte = "INSERT INTO compte (client_id, email, password, created_by_admin_id) VALUES (LAST_INSERT_ID(), ?, ?, 1)"; // assuming admin_id = 1

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statementPerson = connection.prepareStatement(sqlPerson);
             PreparedStatement statementClient = connection.prepareStatement(sqlClient);
             PreparedStatement statementCompte = connection.prepareStatement(sqlCompte)) {

            connection.setAutoCommit(false); // Start transaction

            // Insert into person table
            statementPerson.setString(1, firstName);
            statementPerson.setString(2, lastName);
            statementPerson.setString(3, email);
            statementPerson.setString(4, type);
            statementPerson.executeUpdate();

            // Insert into client table
            statementClient.executeUpdate();

            // Insert into compte table
            statementCompte.setString(1, email);
            statementCompte.setString(2, password);
            statementCompte.executeUpdate();

            connection.commit(); // Commit transaction

            loadUsers(); // Refresh user list

            clearFields();

            showAlert(AlertType.INFORMATION, "Success", "User added successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(AlertType.ERROR, "Selection Error", "Please select a user to update");
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String type = typeField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || type.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error", "Please fill in all fields");
            return;
        }

        String sqlPerson = "UPDATE person SET first_name = ?, last_name = ?, email = ?, role = ? WHERE person_id = ?";
        String sqlCompte = "UPDATE compte SET email = ?, password = ? WHERE client_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statementPerson = connection.prepareStatement(sqlPerson);
             PreparedStatement statementCompte = connection.prepareStatement(sqlCompte)) {

            connection.setAutoCommit(false); // Start transaction

            // Update person table
            statementPerson.setString(1, firstName);
            statementPerson.setString(2, lastName);
            statementPerson.setString(3, email);
            statementPerson.setString(4, type);
            statementPerson.setInt(5, selectedUser.getId());
            statementPerson.executeUpdate();

            // Update compte table
            statementCompte.setString(1, email);
            statementCompte.setString(2, password);
            statementCompte.setInt(3, selectedUser.getId());
            statementCompte.executeUpdate();

            connection.commit(); // Commit transaction

            loadUsers(); // Refresh user list

            clearFields();

            showAlert(AlertType.INFORMATION, "Success", "User updated successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showAlert(AlertType.ERROR, "Selection Error", "Please select a user to delete");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected user?");
        if (alert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        String sqlCompte = "DELETE FROM compte WHERE client_id = ?";
        String sqlClient = "DELETE FROM client WHERE person_id = ?";
        String sqlPerson = "DELETE FROM person WHERE person_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statementCompte = connection.prepareStatement(sqlCompte);
             PreparedStatement statementClient = connection.prepareStatement(sqlClient);
             PreparedStatement statementPerson = connection.prepareStatement(sqlPerson)) {

            connection.setAutoCommit(false); // Start transaction

            // Delete from compte table
            statementCompte.setInt(1, selectedUser.getId());
            statementCompte.executeUpdate();

            // Delete from client table
            statementClient.setInt(1, selectedUser.getId());
            statementClient.executeUpdate();

            // Delete from person table
            statementPerson.setInt(1, selectedUser.getId());
            statementPerson.executeUpdate();

            connection.commit(); // Commit transaction

            loadUsers(); // Refresh user list

            clearFields();

            showAlert(AlertType.INFORMATION, "Success", "User deleted successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection connection = DatabaseUtil.getConnection()) {
                connection.rollback(); // Rollback transaction if any error occurs
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        typeField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
