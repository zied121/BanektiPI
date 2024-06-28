package Controllers;

import entite.Demande;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Service.Demandeservice;

import java.time.LocalDate;
import java.util.List;

public class modifier_supprimer_controller {

    @FXML
    private TableView<Demande> demandesTable;

    @FXML
    private TableColumn<Demande, String> typeColumn;

    @FXML
    private TableColumn<Demande, String> descriptionColumn;

    @FXML
    private TableColumn<Demande, String> statutColumn;

    @FXML
    private TableColumn<Demande, LocalDate> dateColumn;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField statutField;

    @FXML
    private TextField dateField;

    private Demandeservice demandeservice = new Demandeservice();

    @FXML
    void initialize() {
        initializeTable();
        setupComboBox();
        showDetails(null); // Initially, no request selected
        demandesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
    }

    private void initializeTable() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        List<Demande> demandes = demandeservice.readAll();
        demandesTable.setItems(FXCollections.observableArrayList(demandes));
    }

    private void setupComboBox() {
        typeComboBox.setItems(FXCollections.observableArrayList("rip", "sit"));
    }

    private void showDetails(Demande demande) {
        if (demande != null) {
            typeComboBox.setValue(demande.getType());
            descriptionField.setText(demande.getDescription());
            statutField.setText(demande.getStatut());
            dateField.setText(demande.getDate().toString());

            // Disable editing for statut and date
            statutField.setDisable(true);
            dateField.setDisable(true);
        } else {
            clearFields();
        }
    }

    @FXML
    private void modifierDemande() {
        Demande selectedDemande = demandesTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            // Get modified values from fields
            String newType = typeComboBox.getValue();
            String newDescription = descriptionField.getText();
            // You might want to handle updates to statut and date separately if needed

            // Update the selected Demande object with new values
            selectedDemande.setType(newType);
            selectedDemande.setDescription(newDescription);

            // Call the service to update the demande in the database
            demandeservice.update(selectedDemande);

            // Refresh the table after update
            loadDemandes();
        } else {
            System.out.println("Aucune demande sélectionnée pour modification.");
        }
    }

    @FXML
    private void supprimerDemande() {
        Demande selectedDemande = demandesTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            demandeservice.delete(selectedDemande);
            loadDemandes(); // Refresh table after deletion
        } else {
            System.out.println("Aucune demande sélectionnée pour suppression.");
        }
    }

    private void loadDemandes() {
        List<Demande> demandes = demandeservice.readAll();
        demandesTable.setItems(FXCollections.observableArrayList(demandes));
    }

    private void clearFields() {
        typeComboBox.setValue(null);
        descriptionField.clear();
        statutField.clear();
        dateField.clear();

        // Enable editing for statut and date
        statutField.setDisable(false);
        dateField.setDisable(false);
    }
}
