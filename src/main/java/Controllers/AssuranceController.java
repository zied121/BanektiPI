package Controllers;

import Service.AssuranceService;
import entite.Assurance;
import entite.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import util.UserSession;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AssuranceController {
    @FXML
    private ComboBox<String> statusField;
    @FXML
    private TextField idUserField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField dateDebutField;
    @FXML
    private TextField dateFinField;
    @FXML
    private TextField documentField;
    @FXML
    private TextField imageField;
    private AssuranceService assuranceService;
    @FXML
    private TableView<Assurance> assuranceTable;
    @FXML
    private TableColumn<Assurance, Integer> idColumn;
    @FXML
    private TableColumn<Assurance, String> idUserColumn;
    @FXML
    private TableColumn<Assurance, String> typeColumn;
    @FXML
    private TableColumn<Assurance, String> dateDebutColumn;
    @FXML
    private TableColumn<Assurance, String> dateFinColumn;
    @FXML
    private TableColumn<Assurance, String> statusColumn;
    @FXML
    private TableColumn<Assurance, Void> actionsColumn;

    private ObservableList<Assurance> assuranceList;
    private String idUser;
    private String userId;

    public AssuranceController() {
        assuranceService = new AssuranceService();
    }

    @FXML
    public void initialize() {
        User user = UserSession.getInstance().getUser();
        this.userId = String.valueOf(user.getId());
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));



        assuranceList = FXCollections.observableArrayList();
        assuranceTable.setItems(assuranceList);

        loadAssurances();
    }
    private void loadAssurances() {
        assuranceList.clear();
        assuranceList.addAll(assuranceService.getAllAssurances());
    }

    private boolean validateInputs(String idUser, String type, String dateDebut, String dateFin, String document, String image) {
        if (idUser.isEmpty() || type.isEmpty() || dateDebut.isEmpty() || dateFin.isEmpty() || document.isEmpty() || image.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return false;
        }

        if (!isValidDate(dateDebut) || !isValidDate(dateFin)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Dates must be in the format yyyy-MM-dd.");
            return false;
        }

        return true;
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    private void deleteAssurance(Assurance assurance) {
        boolean success = assuranceService.deleteAssurance(assurance.getId());
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Assurance deleted successfully!");
            loadAssurances();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete assurance.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void handleUpdateStatus(ActionEvent actionEvent) {
        Assurance selectedAssurance = assuranceTable.getSelectionModel().getSelectedItem();
        if (selectedAssurance != null) {
            String newStatus = statusField.getValue();
            if (newStatus.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Status field cannot be empty.");
                return;
            }

            selectedAssurance.setStatus(newStatus);
            boolean success = assuranceService.updateAssuranceStatus(selectedAssurance);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Status updated successfully!");
                loadAssurances();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update status.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No assurance selected.");
        }
    }

    @FXML
    public void handleDeleteAssurance(ActionEvent actionEvent) {
        Assurance selectedAssurance = assuranceTable.getSelectionModel().getSelectedItem();
        if (selectedAssurance != null) {
            boolean success = assuranceService.deleteAssurance(selectedAssurance.getId());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Assurance deleted successfully!");
                loadAssurances();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete assurance.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No assurance selected.");
        }
    }

    private void clearFields() {
        idUserField.clear();
        typeField.clear();
        dateDebutField.clear();
        dateFinField.clear();
        documentField.clear();
        imageField.clear();
    }

    public void setUserId(String userId) {
    }
}
