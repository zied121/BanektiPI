package Controllers;

import entite.Reclamation;
import entite.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import Service.Reclamationservice;
import javafx.scene.control.Alert.AlertType;
import util.UserSession;

public class modifier_supprimer_reclamation {

    @FXML
    private TableView<Reclamation> ReclamationsTable;

    @FXML
    private TableColumn<Reclamation, String> typeColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, String> statutColumn;

    @FXML
    private TableColumn<Reclamation, LocalDate> dateColumn;

    @FXML
    private TableColumn<Reclamation, String> reponseColumn;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField statutField;

    @FXML
    private TextField dateField;

    private Reclamationservice Reclamationservice = new Reclamationservice();
    public User user = UserSession.getInstance().getUser();

    @FXML
    void initialize() {
        initializeTable();
        showDetails(null);
        ReclamationsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
    }

    private void initializeTable() {
        System.out.println(user.getId());
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));

        List<Reclamation> Reclamations = Reclamationservice.readAll(user.getId());
        for (Reclamation Reclamation : Reclamations) {
            if (Reclamation.getReponse() == null || Reclamation.getReponse().isEmpty()) {
                Reclamation.setReponse("Votre réponse sera traitée au plus tôt possible. Merci.\n");
            }
        }
        ReclamationsTable.setItems(FXCollections.observableArrayList(Reclamations));
    }

    private void showDetails(Reclamation Reclamation) {
        if (Reclamation != null) {
            typeComboBox.setValue(Reclamation.getType());
            descriptionField.setText(Reclamation.getDescription());
            statutField.setText(Reclamation.getStatut());
            dateField.setText(Reclamation.getDate().toString());

            statutField.setDisable(true);
            dateField.setDisable(true);
        } else {
            clearFields();
        }
    }

    @FXML
    private void ajouterReclamation(ActionEvent event) {
        try {
            Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("/Main/ajouter_reclamation.fxml"));
            Scene adminLoginScene = new Scene(adminLoginRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(adminLoginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierReclamation() {
        Reclamation selectedReclamation = ReclamationsTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            String newType = typeComboBox.getValue();
            String newDescription = descriptionField.getText();

            selectedReclamation.setType(newType);
            selectedReclamation.setDescription(newDescription);

            // Vérifier et remplir la réponse par défaut si elle est vide
            if (selectedReclamation.getReponse() == null || selectedReclamation.getReponse().isEmpty()) {
                selectedReclamation.setReponse("Votre réponse sera traitée au plus tôt possible. Merci.\n");
            }

            Reclamationservice.update(selectedReclamation);

            loadReclamations();

            showAlert(AlertType.INFORMATION, "Modification réussie", "La Reclamation a été modifiée avec succès.");
        } else {
            showAlert(AlertType.WARNING, "Aucune sélection", "Aucune Reclamation sélectionnée pour modification.");
        }
    }

    @FXML
    private void supprimerReclamation() {
        Reclamation selectedReclamation = ReclamationsTable.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            Reclamationservice.delete(selectedReclamation);
            loadReclamations();

            showAlert(AlertType.INFORMATION, "Suppression réussie", "La Reclamation a été supprimée avec succès.");
        } else {
            showAlert(AlertType.WARNING, "Aucune sélection", "Aucune Reclamation sélectionnée pour suppression.");
        }
    }

//    @FXML
//    private void telechargerPDF() {
//        Reclamation selectedReclamation = ReclamationsTable.getSelectionModel().getSelectedItem();
//        if (selectedReclamation != null && selectedReclamation.getReponse() != null && !selectedReclamation.getReponse().isEmpty()) {
//            String content = selectedReclamation.getReponse();
//            String fileName = "reponse_Reclamation_" + selectedReclamation.getId() + ".pdf";
//            String fileLocation = System.getProperty("user.home") + "/Downloads/" + fileName;
//
//            String formattedContent = generateFormattedContent(selectedReclamation);
//
//            PdfGenerator.generatePdf(fileLocation, formattedContent);
//
//            showAlert(Alert.AlertType.INFORMATION, "Téléchargement réussi", "Le PDF a été généré et téléchargé avec succès.");
//
//            openPdfFile(fileLocation);
//        } else {
//            showAlert(Alert.AlertType.WARNING, "Aucune réponse disponible", "Aucune réponse n'est disponible pour cette Reclamation.");
//        }
//    }

    private String generateFormattedContent(Reclamation Reclamation) {
        StringBuilder contentBuilder = new StringBuilder();

        contentBuilder.append("Réponse à la Reclamation ").append(Reclamation.getId()).append("\n\n");

        contentBuilder.append("Type de Reclamation : ").append(Reclamation.getType()).append("\n");
        contentBuilder.append("Description : ").append(Reclamation.getDescription()).append("\n");
        contentBuilder.append("Statut : ").append(Reclamation.getStatut()).append("\n");
        contentBuilder.append("Date : ").append(Reclamation.getDate()).append("\n\n");

        contentBuilder.append("Réponse :\n");
        contentBuilder.append(Reclamation.getReponse()).append("\n");

        return contentBuilder.toString();
    }

//    private void openPdfFile(String fileLocation) {
//        try {
//            File pdfFile = new File(fileLocation);
//            if (pdfFile.exists()) {
//                Desktop.getDesktop().open(pdfFile);
//            } else {
//                showAlert(Alert.AlertType.ERROR, "Fichier introuvable", "Le fichier PDF n'a pas été trouvé.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Erreur d'ouverture", "Erreur lors de l'ouverture du fichier PDF : " + e.getMessage());
//        }
//    }

    @FXML
    private void loadReclamations() {
        List<Reclamation> Reclamations = Reclamationservice.readAll(user.getId());
        for (Reclamation Reclamation : Reclamations) {
            if (Reclamation.getReponse() == null || Reclamation.getReponse().isEmpty()) {
                Reclamation.setReponse("Votre réponse sera traitée au plus tôt possible. Merci.\n");
            }
        }
        ReclamationsTable.setItems(FXCollections.observableArrayList(Reclamations));
    }

    private void clearFields() {
        typeComboBox.setValue(null);
        descriptionField.clear();
        statutField.clear();
        dateField.clear();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
