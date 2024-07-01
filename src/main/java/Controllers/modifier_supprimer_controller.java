package Controllers;

import entite.Demande;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Service.Demandeservice;
import javafx.stage.Stage;

import java.io.IOException;
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
    private TableColumn<Demande, String> reponseColumn;

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
        showDetails(null);
        demandesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
    }

    private void initializeTable() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));

        List<Demande> demandes = demandeservice.readAll();
        demandesTable.setItems(FXCollections.observableArrayList(demandes));
    }

    private void setupComboBox() {
        typeComboBox.setItems(FXCollections.observableArrayList("rib", "extraits de compte", "Identité banquaire", "autres"));
    }

    private void showDetails(Demande demande) {
        if (demande != null) {
            typeComboBox.setValue(demande.getType());
            descriptionField.setText(demande.getDescription());
            statutField.setText(demande.getStatut());
            dateField.setText(demande.getDate().toString());

            statutField.setDisable(true);
            dateField.setDisable(true);
        } else {
            clearFields();
        }
    }

    @FXML
    private void ajouterDemande(ActionEvent event) {
        try {
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/fenetre1.fxml"));
            Scene adminLoginScene = new Scene(adminLoginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminLoginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierDemande() {
        Demande selectedDemande = demandesTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            String newType = typeComboBox.getValue();
            String newDescription = descriptionField.getText();

            selectedDemande.setType(newType);
            selectedDemande.setDescription(newDescription);

            demandeservice.update(selectedDemande);

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
            loadDemandes();
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
    }
}
