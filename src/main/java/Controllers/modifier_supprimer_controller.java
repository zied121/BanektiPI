package Controllers;

import Service.PdfGenerator;
import entite.Demande;
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

import Service.Demandeservice;
import javafx.scene.control.Alert.AlertType;
import util.UserSession;

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
     User user = UserSession.getInstance().getUser();



    @FXML
    void initialize() {
        initializeTable();
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

        List<Demande> demandes = demandeservice.readAll(user.getId());
        for (Demande demande : demandes) {
            if (demande.getReponse() == null || demande.getReponse().isEmpty()) {
                demande.setReponse("Votre réponse sera traitée au plus tôt possible. Merci.\n");
            }
        }
        demandesTable.setItems(FXCollections.observableArrayList(demandes));
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

            // Vérifier et remplir la réponse par défaut si elle est vide
            if (selectedDemande.getReponse() == null || selectedDemande.getReponse().isEmpty()) {
                selectedDemande.setReponse("Votre réponse sera traitée au plus tôt possible. Merci.\n");
            }

            demandeservice.update(selectedDemande, user.getId())    ;

            loadDemandes();

            showAlert(AlertType.INFORMATION, "Modification réussie", "La demande a été modifiée avec succès.");
        } else {
            showAlert(AlertType.WARNING, "Aucune sélection", "Aucune demande sélectionnée pour modification.");
        }
    }

    @FXML
    private void supprimerDemande() {
        Demande selectedDemande = demandesTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            demandeservice.delete(selectedDemande, user.getId())   ;
            loadDemandes();

            showAlert(AlertType.INFORMATION, "Suppression réussie", "La demande a été supprimée avec succès.");
        } else {
            showAlert(AlertType.WARNING, "Aucune sélection", "Aucune demande sélectionnée pour suppression.");
        }
    }

    @FXML
    private void telechargerPDF() {
        Demande selectedDemande = demandesTable.getSelectionModel().getSelectedItem();
        if (selectedDemande != null && selectedDemande.getReponse() != null && !selectedDemande.getReponse().isEmpty()) {
            // Récupère le contenu de la réponse de la demande sélectionnée

            String content = selectedDemande.getReponse();
            // Génère le nom de fichier pour le PDF

            String fileName = "reponse_demande_" + selectedDemande.getId() + ".pdf";
            // Définit l'emplacement où le fichier PDF sera sauvegardé

            String fileLocation = System.getProperty("user.home") + "/Downloads/" + fileName;
            // Formate le contenu de la réponse

            String formattedContent = generateFormattedContent(selectedDemande);
            // Génère le fichier PDF en utilisant le contenu formaté

            PdfGenerator.generatePdf(fileLocation, formattedContent);

            showAlert(Alert.AlertType.INFORMATION, "Téléchargement réussi", "Le PDF a été généré et téléchargé avec succès.");

            openPdfFile(fileLocation);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune réponse disponible", "Aucune réponse n'est disponible pour cette demande.");
        }
    }

    private String generateFormattedContent(Demande demande) {
        StringBuilder contentBuilder = new StringBuilder();

        contentBuilder.append("Réponse à la demande ").append(demande.getId()).append("\n\n");

        contentBuilder.append("Type de demande : ").append(demande.getType()).append("\n");
        contentBuilder.append("Description : ").append(demande.getDescription()).append("\n");
        contentBuilder.append("Statut : ").append(demande.getStatut()).append("\n");
        contentBuilder.append("Date : ").append(demande.getDate()).append("\n\n");

        contentBuilder.append("Réponse :\n");
        contentBuilder.append(demande.getReponse()).append("\n");

        return contentBuilder.toString();
    }

    private void openPdfFile(String fileLocation) {
        try {
            // Vérifie si le fichier PDF existe

            File pdfFile = new File(fileLocation);
            if (pdfFile.exists()) {
                // Ouvre le fichier PDF en utilisant l'application par défaut du système

                Desktop.getDesktop().open(pdfFile);
            } else {
                showAlert(Alert.AlertType.ERROR, "Fichier introuvable", "Le fichier PDF n'a pas été trouvé.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur d'ouverture", "Erreur lors de l'ouverture du fichier PDF : " + e.getMessage());
        }
    }

    @FXML
    private void loadDemandes() {
        List<Demande> demandes = demandeservice.readAll(user.getId());
        for (Demande demande : demandes) {
            if (demande.getReponse() == null || demande.getReponse().isEmpty()) {
                demande.setReponse("Votre réponse sera traitée au plus tôt possible. Merci.\n");
            }
        }
        demandesTable.setItems(FXCollections.observableArrayList(demandes));
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
    @FXML
    private void handlecreditButtoncredituser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
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
    private void handleassuranceButtonuser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
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
    @FXML
    private void handleoperationButtonuser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/operation_view.fxml"));
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
    private void handledocumentButtonuser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre2.fxml"));
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
    private void handlehomeButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/homePage.fxml"));
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
    private void handlesginout(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            UserSession.clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/login.fxml"));
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
