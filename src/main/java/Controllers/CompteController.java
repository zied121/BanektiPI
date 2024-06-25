package Controllers;

import entite.Compte;
import entite.User;
import Service.CompteServiceInterface;
import Service.CompteService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.util.List;

public class CompteController {
    @FXML
    private TableView<Compte> compteTable;
    @FXML
    private TableColumn<Compte, Integer> compteIdColumn;
    @FXML
    private TableColumn<Compte, String> typeColumn;
    @FXML
    private TableColumn<Compte, Integer> numColumn;
    @FXML
    private TableColumn<Compte, Long> ribColumn;
    @FXML
    private TableColumn<Compte, String> statutColumn;
    @FXML
    private TableColumn<Compte, Float> soldeColumn;
    @FXML
    private TableColumn<Compte, String> deviseColumn;

    @FXML
    private TextField compteIdField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField numField;
    @FXML
    private TextField ribField;
    @FXML
    private TextField statutField;
    @FXML
    private TextField soldeField;
    @FXML
    private TextField deviseField;

    private User selectedUser;
    private CompteServiceInterface compteService = new CompteService();

    public void setUser(User user) {
        this.selectedUser = user;
    }

    @FXML
    public void initialize() {
        compteIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
        ribColumn.setCellValueFactory(new PropertyValueFactory<>("rib"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        deviseColumn.setCellValueFactory(new PropertyValueFactory<>("devise"));

        compteTable.setOnMouseClicked(this::handleMouseClick);
    }

    private void handleMouseClick(MouseEvent event) {
        if (event.getClickCount() > 1) {
            onEditCompte();
        }
    }

    public void loadComptes(int userId) throws SQLException {
        List<Compte> comptes = compteService.getComptesByUserId(userId);
        compteTable.getItems().setAll(comptes);
    }

    private void onEditCompte() {
        if (compteTable.getSelectionModel().getSelectedItem() != null) {
            Compte selectedCompte = compteTable.getSelectionModel().getSelectedItem();
            compteIdField.setText(String.valueOf(selectedCompte.getId()));
            typeField.setText(selectedCompte.getType());
            numField.setText(String.valueOf(selectedCompte.getNum()));
            ribField.setText(String.valueOf(selectedCompte.getRib()));
            statutField.setText(selectedCompte.getStatut());
            soldeField.setText(String.valueOf(selectedCompte.getSolde()));
            deviseField.setText(selectedCompte.getDevise());
        }
    }

    @FXML
    private void createCompte() throws SQLException {
        Compte compte = new Compte();
        compte.setType(typeField.getText());
        compte.setNum(Integer.parseInt(numField.getText()));
        compte.setRib(Long.parseLong(ribField.getText()));
        compte.setStatut(statutField.getText());
        compte.setSolde(Float.parseFloat(soldeField.getText()));
        compte.setDevise(deviseField.getText());
        compte.setUserId(selectedUser.getId());
        compteService.createCompte(compte);
        showAlert(AlertType.INFORMATION, "Compte Created", "Compte created successfully!");
        loadComptes(selectedUser.getId());
    }

    @FXML
    private void updateCompte() throws SQLException {
        Compte compte = new Compte();
        compte.setId(Integer.parseInt(compteIdField.getText()));
        compte.setType(typeField.getText());
        compte.setNum(Integer.parseInt(numField.getText()));
        compte.setRib(Long.parseLong(ribField.getText()));
        compte.setStatut(statutField.getText());
        compte.setSolde(Float.parseFloat(soldeField.getText()));
        compte.setDevise(deviseField.getText());
        compteService.updateCompte(compte);
        showAlert(AlertType.INFORMATION, "Compte Updated", "Compte updated successfully!");
        loadComptes(selectedUser.getId());
    }

    @FXML
    private void deleteCompte() throws SQLException {
        int compteId = Integer.parseInt(compteIdField.getText());
        compteService.deleteCompte(compteId, selectedUser.getId());
        showAlert(AlertType.INFORMATION, "Compte Deleted", "Compte deleted successfully!");
        loadComptes(selectedUser.getId());
    }

    @FXML
    private void clearCompteFields() {
        compteIdField.clear();
        typeField.clear();
        numField.clear();
        ribField.clear();
        statutField.clear();
        soldeField.clear();
        deviseField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
