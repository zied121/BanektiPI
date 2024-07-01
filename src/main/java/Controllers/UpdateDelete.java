package Controllers;

import entite.Reclamation;
import entite.Reponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Service.Reclamationservice;

import java.time.LocalDate;
import java.util.List;

public class UpdateDelete {

    @FXML
    private TableView<Reclamation> RecTable;

    @FXML
    private TableColumn<Reclamation, String> typeColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;
    @FXML
    private TableColumn<Reclamation, String> statutColumn;

    @FXML
    private TableColumn<Reclamation, LocalDate> dateColumn;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField statutField;

    @FXML
    private TextField dateField;

    private Reclamationservice recService = new Reclamationservice();
    @FXML
    private TableColumn<Reponse, String> reponseColumn;
    private TableView<Reclamation> tableView;

    @FXML
    public void initialize() {

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse.message"));


        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(recService.readAll(new Reclamation()));
        RecTable.setItems(reclamations);
    }

//    @FXML
//    void initialize() {
//        initializeTable();
//        setupComboBox();
//        showDetails(null);
//        demandesTable.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> showDetails(newValue));
//    }
//
//    private void initializeTable() {
//        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
//        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
//        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));
//
//        List<Demande> demandes = demandeservice.readAll();
//        demandesTable.setItems(FXCollections.observableArrayList(demandes));
//    }

    private void setupComboBox() {
        typeComboBox.setItems(FXCollections.observableArrayList("Service clientele", "Remboursement", "Validite carte", "autres"));
    }

    private void showDetails(Reclamation reclamation) {
        if (reclamation != null) {
            typeComboBox.setValue(reclamation.getType_rec());
            descriptionField.setText(reclamation.getDescription());
            statutField.setText(reclamation.getStatut());
            dateField.setText(reclamation.getDate_rec().toString());

            // Disable editing for statut and date
            statutField.setDisable(true);
            dateField.setDisable(true);
        } else {
            clearFields();
        }
    }

    @FXML
    void action_ajouter(ActionEvent event) {
        String type_rec = typeComboBox.getValue();
        String description = descriptionField.getText();
        String statut = statutField.getText();
        LocalDate date_rec = LocalDate.now();

        Reclamation reclamation = new Reclamation(type_rec, description, statut, date_rec);//, id_user);
        Reclamationservice reclamationservice = new Reclamationservice();
        reclamationservice.insertPST(reclamation);

        loadReclamation();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../AfficherReclamation.fxml"));
        try {
            Parent root = loader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @FXML
//    private void ajouterDemande(ActionEvent event) {
//        try {
//            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/fenetre1.fxml"));
//            Scene adminLoginScene = new Scene(adminLoginRoot);
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(adminLoginScene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

@FXML
private void modifierRec() {
    Reclamation selectedRec = RecTable.getSelectionModel().getSelectedItem();
    if (selectedRec != null) {
        String newType = typeComboBox.getValue();
        String newDescription = descriptionField.getText();

        selectedRec.setType_rec(newType);
        selectedRec.setDescription(newDescription);

        recService.update(selectedRec);

        loadReclamation();
    } else {
        System.out.println("Please select a reclamation to update");
    }
}

    @FXML
    private void DeleteRec() {
        Reclamation selectedRec = RecTable.getSelectionModel().getSelectedItem();
        if (selectedRec != null) {
            recService.delete(selectedRec);
            loadReclamation();
        } else {
            System.out.println("Please select a reclamation to delete");
        }
    }

    private void loadReclamation() {
        List<Reclamation> reclamationList = recService.readAll(new Reclamation());
        if (reclamationList != null) {
            RecTable.setItems(FXCollections.observableArrayList(reclamationList));
        } else {
            System.err.println("Failed to load data from the service.");
        }
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