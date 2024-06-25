package Controllers;

import entite.CreditOperation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import Service.CreditOperationService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DatabaseUtil;

public class CreditOperationController {

    @FXML
    private TextField idCompteField;
    @FXML
    private TextField typeCreditField;
    @FXML
    private TextField montantField;
    @FXML
    private TextField dateDebutField;
    @FXML
    private TextField dateFinField;
    @FXML
    private TextField tauxInteretField;
    @FXML
    private ComboBox<String> statutCombo;
    @FXML
    private TextField modePaiementField;

    private CreditOperationService creditOperationService;

    public CreditOperationController() {
        this.creditOperationService = new CreditOperationService();
    }

    @FXML
    private void initialize() {
        statutCombo.setItems(FXCollections.observableArrayList("Active", "Inactive"));
        statutCombo.setValue("Active");
    }

    @FXML
    private void handleCheckAccount() {
        try {
            int idCompte = Integer.parseInt(idCompteField.getText());
            String typeCredit = typeCreditField.getText();
            double montant = Double.parseDouble(montantField.getText());
            Date dateDebut = Date.valueOf(dateDebutField.getText());
            Date dateFin = Date.valueOf(dateFinField.getText());
            double tauxInteret = Double.parseDouble(tauxInteretField.getText());
            String statut = statutCombo.getValue();
            String modePaiement = modePaiementField.getText();

            if (typeCredit.isEmpty() || statut == null || modePaiement.isEmpty()) {
                showAlert("Error", "Please fill all the fields.");
                return;
            }

            if (!checkIfCompteExists(idCompte)) {
                showAlert("Error", "Account ID does not exist.");
                return;
            }

            CreditOperation creditOperation = new CreditOperation(idCompte, typeCredit, montant, dateDebut, dateFin, tauxInteret, statut, modePaiement);

            if (creditOperationService.validateCreditOperation(creditOperation)) {
                processCreditOperation(creditOperation);
                showAlert("Success", "Credit operation processed successfully.");
            } else {
                showAlert("Error", "Invalid credit operation.");
            }
        } catch (Exception e) {
            showAlert("Error", "Please enter valid data.");
        }
    }

    private boolean checkIfCompteExists(int idCompte) {
        String query = "SELECT COUNT(*) FROM compte WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idCompte);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to check account existence: " + e.getMessage());
        }
        return false;
    }

    private void processCreditOperation(CreditOperation creditOperation) {
        String query = "INSERT INTO credit (id_compte, type_credit, montant, date_debut, date_fin, taux_interet, statut, mode_paiement) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, creditOperation.getIdCompte());
            preparedStatement.setString(2, creditOperation.getTypeCredit());
            preparedStatement.setDouble(3, creditOperation.getMontant());
            preparedStatement.setDate(4, creditOperation.getDateDebut());
            preparedStatement.setDate(5, creditOperation.getDateFin());
            preparedStatement.setDouble(6, creditOperation.getTauxInteret());
            preparedStatement.setString(7, creditOperation.getStatut());
            preparedStatement.setString(8, creditOperation.getModePaiement());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            showAlert("Error", "Failed to process credit operation: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
