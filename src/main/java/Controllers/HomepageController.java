package Controllers;

import Service.UserService;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entite.Compte;
import Service.CompteService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class HomepageController {

    @FXML
    private Label userNameLabel;
    @FXML
    private TableView<Compte> accountsTable;
    @FXML
    private TableColumn<Compte, String> ribColumn;
    @FXML
    private TableColumn<Compte, Double> soldeColumn;
    @FXML
    private TableColumn<Compte, String> statusColumn;
    private String userId;
    private UserService UserService;
    @FXML
    public void initialize() {
        userId = String.valueOf(UserSession.getInstance().getUserId());
        System.out.println("User ID: " + userId);
        populateUserDetails();
        populateUserAccounts();
    }

    private void populateUserDetails() {

        Map<String, String> userDetails = UserService.getUserDetailsById(userId);
        if (userDetails != null) {
            String fullName = userDetails.get("nom") + " " + userDetails.get("prenom");
            userNameLabel.setText(fullName);
        } else {
            userNameLabel.setText("User not found");
        }
    }

    private void populateUserAccounts() {
        List<Compte> accounts = UserService.getUserAccountsById(userId);

        ribColumn.setCellValueFactory(new PropertyValueFactory<>("rib"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        accountsTable.getItems().setAll(accounts);
    }
    @FXML
    private void handlecreditButtoncredituser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/user.fxml"));
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
    private void handleButtonAssurance(ActionEvent event) {
        try {
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
}
