package Controllers;

import entite.Demande;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Service.Demandeservice;

import java.time.LocalDate;
import java.util.List;

public class Fenetre2Controller {

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
    private TextField typeField;

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

    private void showDetails(Demande demande) {
        if (demande != null) {
            typeField.setText(demande.getType());
            descriptionField.setText(demande.getDescription());
            statutField.setText(demande.getStatut());
            dateField.setText(demande.getDate().toString());
        } else {
            clearFields();
        }
    }

    @FXML
    private void modifierDemande() {
        Demande selectedDemande = demandesTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            // Récupérez les valeurs modifiées des champs de texte
            String newType = typeField.getText();
            String newDescription = descriptionField.getText();
            String newStatut = statutField.getText();
            LocalDate newDate = LocalDate.parse(dateField.getText());

            // Mettez à jour l'objet Demande sélectionné avec les nouvelles valeurs
            selectedDemande.setType(newType);
            selectedDemande.setDescription(newDescription);
            selectedDemande.setStatut(newStatut);
            selectedDemande.setDate(newDate);

            // Appelez le service pour mettre à jour la demande dans la base de données
            demandeservice.update(selectedDemande);

            // Rafraîchissez la table après la mise à jour
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
            loadDemandes(); // Rechargez la table après la suppression
        } else {
            System.out.println("Aucune demande sélectionnée pour suppression.");
        }
    }

    private void loadDemandes() {
        List<Demande> demandes = demandeservice.readAll();
        demandesTable.setItems(FXCollections.observableArrayList(demandes));
    }

    private void clearFields() {
        typeField.clear();
        descriptionField.clear();
        statutField.clear();
        dateField.clear();
    }
}
