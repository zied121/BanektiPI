package Controllers;

import Service.Reclamationservice;
import Service.Repondservice;
import entite.Repond;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class RepondController {

    @FXML
    private TableView<Repond> RepondsTable;

    @FXML
    private TableColumn<Repond, String> reponseColumn;

    @FXML
    private TableColumn<Repond, LocalDate> dateColumn;

    @FXML
    private TableColumn<Repond, String> typeColumn;

    @FXML
    private TableColumn<Repond, String> statutColumn;

    @FXML
    private TableColumn<Repond, String> descriptionColumn;

    @FXML
    private TableColumn<Repond, Integer> idUserColumn;

    @FXML
    private TableColumn<Repond, LocalDate> ReclamationDateColumn;

    @FXML
    private TableColumn<Repond, String> emailColumn;

    @FXML
    private TextArea reponseField;

    @FXML
    private TextField dateField;

    @FXML
    private ComboBox<String> statutComboBox;

    private Repondservice RepondService = new Repondservice();
    private Reclamationservice ReclamationService = new Reclamationservice();

    @FXML
    void initialize() {
        initializeTable();
        showDetails(null);

        RepondsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
    }

    private void initializeTable() {

        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReclamation().getType()));
        statutColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReclamation().getStatut()));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReclamation().getDescription()));
        idUserColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getReclamation().getId_user()).asObject());
        ReclamationDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getReclamation().getDate()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReclamation().getEmail()));
        loadReponds();
    }

    private void loadReponds() {
        List<Repond> Reponds = RepondService.readAllWithReclamationDetails();
        RepondsTable.setItems(FXCollections.observableArrayList(Reponds));
    }

    @FXML
    private void showDetails(Repond Repond) {
        if (Repond != null) {
            reponseField.setText(Repond.getReponse());
            dateField.setText(Repond.getDate() != null ? Repond.getDate().toString() : "");
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        reponseField.clear();
        dateField.clear();
    }

    @FXML
    private void ajouterRepond(ActionEvent event) {
        Repond selectedRepond = RepondsTable.getSelectionModel().getSelectedItem();
        if (selectedRepond != null) {
            String newReponse = reponseField.getText();
            if (newReponse != null && !newReponse.isEmpty()) {
                Repond newRepond = new Repond();
                newRepond.setReponse(newReponse);
                newRepond.setDate(LocalDate.now()); // Set the current date
                newRepond.setId_rec(selectedRepond.getReclamation().getId()); // Use the associated Reclamation id

                RepondService.insert(newRepond);

                loadReponds();
                showAlert("Ajout Réussi", null, "La réponse a été ajoutée avec succès.");
            } else {
                System.out.println("La réponse ne peut pas être vide.");
            }
        } else {
            System.out.println("Aucun Repond sélectionné pour modification.");
        }
    }

    @FXML
    private void modifierRepond() {
        Repond selectedRepond = RepondsTable.getSelectionModel().getSelectedItem();
        if (selectedRepond != null) {
            String newReponse = reponseField.getText();

            if (newReponse != null && !newReponse.isEmpty()) {
                selectedRepond.setReponse(newReponse);
                selectedRepond.setDate(LocalDate.now()); // Set the date to the current date

                RepondService.update(selectedRepond);

                loadReponds();
                showAlert("Modification Réussie", null, "La réponse a été modifiée avec succès.");
            } else {
                System.out.println("La réponse ne peut pas être vide.");
            }
        } else {
            System.out.println("Aucun Repond sélectionné pour modification.");
        }
    }

    @FXML
    private void supprimerRepond() {
        Repond selectedRepond = RepondsTable.getSelectionModel().getSelectedItem();
        if (selectedRepond != null) {
            RepondService.delete(selectedRepond);
            loadReponds();
            showAlert("Suppression Réussie", null, "Le Repond a été supprimé avec succès.");
        } else {
            System.out.println("Aucun Repond sélectionné pour suppression.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Appliquer le style personnalisé au popup
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/Main/styleFile.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("popup");

        alert.showAndWait();
    }

    @FXML
    private void modifierStatut() {
        Repond selectedRepond = RepondsTable.getSelectionModel().getSelectedItem();
        if (selectedRepond != null) {
            String newStatut = statutComboBox.getValue();
            if (newStatut != null && !newStatut.isEmpty()) {
                selectedRepond.getReclamation().setStatut(newStatut);
                ReclamationService.update(selectedRepond.getReclamation());
                loadReponds();
                showAlert("Modification du Statut Réussie", null, "Le statut a été modifié avec succès.");

                // Send email
                String email = selectedRepond.getReclamation().getEmail();

                if (email != null) { // Add this null check
                    String subject;
                    String body;
                    if ("terminé".equals(newStatut)) {
                        subject = "Votre Reclamation est terminée";
                        body = "Bonjour," +"/n"+  "votre Reclamation est terminée." + "/n" + "Vous pouvez consulter notre application pour voir la réponse.";
                    } else if ("annulée".equals(newStatut)) {
                        subject = "Votre Reclamation est annulée";
                        body = "Bonjour, votre Reclamation est annulée. Pour connaître les raisons de l'annulation, vous pouvez consulter notre application ou appeler notre service client.";
                    } else {
                        return;
                    }
                    RepondService.sendEmail(email, subject, body);
                }
            } else {
                System.out.println("Veuillez sélectionner un statut.");
            }
        } else {
            System.out.println("Aucun Repond sélectionné pour modifier le statut.");
        }
    }

}
