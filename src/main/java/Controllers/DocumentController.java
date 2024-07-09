package Controllers;

import Service.Demandeservice;
import Service.Documentservice;
import entite.Document;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.UserSession;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocumentController {

    @FXML
    private TableView<Document> documentsTable;

    @FXML
    private TableColumn<Document, String> reponseColumn;

    @FXML
    private TableColumn<Document, LocalDate> dateColumn;

    @FXML
    private TableColumn<Document, String> typeColumn;

    @FXML
    private TableColumn<Document, String> statutColumn;

    @FXML
    private TableColumn<Document, String> descriptionColumn;

    @FXML
    private TableColumn<Document, Integer> idUserColumn;

    @FXML
    private TableColumn<Document, LocalDate> demandeDateColumn;

    @FXML
    private TableColumn<Document, String> emailColumn;

    @FXML
    private TextArea reponseField;

    @FXML
    private TextField dateField;

    @FXML
    private ComboBox<String> statutComboBox;

    @FXML
    private ComboBox<String> filterStatusComboBox;
    @FXML
    private VBox pieChartContainer;

    private Documentservice documentService = new Documentservice();
    private Demandeservice demandeService = new Demandeservice();

    private List<Document> allDocuments;
    private PieChart pieChart;

    @FXML
    void initialize() {
        initializeTable();
        loadData();
        showDetails(null);
        PieChart pieChart = createPieChart();
        pieChartContainer.getChildren().add(pieChart);

        documentsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDetails(newValue));
    }

    private void initializeTable() {
        reponseColumn.setCellValueFactory(new PropertyValueFactory<>("reponse"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDemande().getType()));
        statutColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDemande().getStatut()));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDemande().getDescription()));
        idUserColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDemande().getId_user()).asObject());
        demandeDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDemande().getDate()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDemande().getEmail()));
    }

    private void loadData() {
        allDocuments = documentService.readAllWithDemandeDetails();
        documentsTable.setItems(FXCollections.observableArrayList(allDocuments));
    }

    @FXML
    private void filterByStatus(ActionEvent event) {
        String selectedStatus = filterStatusComboBox.getValue();
        if (selectedStatus.equals("Tous")) {
            documentsTable.setItems(FXCollections.observableArrayList(allDocuments));
        } else {
            List<Document> filteredDocuments = allDocuments.stream()
                    .filter(doc -> doc.getDemande().getStatut().equals(selectedStatus))
                    .collect(Collectors.toList());
            documentsTable.setItems(FXCollections.observableArrayList(filteredDocuments));
        }
    }

    private void loadDocuments() {
        List<Document> documents = documentService.readAllWithDemandeDetails();
        documentsTable.setItems(FXCollections.observableArrayList(documents));
    }

    private PieChart createPieChart() {
        Map<String, Integer> data = documentService.fetchDataFromDatabase();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        data.forEach((status, count) -> pieChartData.add(new PieChart.Data(status, count)));

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Répartition des Statuts");
        loadDocuments();
        return pieChart;
    }






    @FXML
    private void showDetails(Document document) {
        if (document != null && document.getDemande() != null) {
            reponseField.setText(document.getReponse());

            // Afficher la date actuelle comme date de réponse
            String dateReponse = "Date : " + LocalDate.now().toString();

            // Afficher les détails en fonction du type de demande
            switch (document.getDemande().getType()) {
                case "rib":
                    String nomPrenomRib = "Bonjour Mr/Mme " + document.getDemande().getNom() + " " + document.getDemande().getPrenom() + "\n" +
                            "Nom : " + document.getDemande().getNom() + "\n" +
                            "Prenom : " + document.getDemande().getPrenom() + "\n" +
                            "Rib : " + document.getDemande().getRib() + "\n" +
                            dateReponse;
                    reponseField.setText(nomPrenomRib);
                    break;
                case "extraits de compte":
                    String nomPrenomCompte = "Bonjour Mr/Mme " + document.getDemande().getNom() + " " + document.getDemande().getPrenom() + "\n" +
                            "Nom : " + document.getDemande().getNom() + "\n" +
                            "Prenom : " + document.getDemande().getPrenom() + "\n" +
                            "Rib : " + document.getDemande().getRib() + "\n" +
                            "Nombre compte : " + document.getDemande().getNb_compte() + "\n" +
                            "Solde : " + document.getDemande().getSolde() + "\n" +
                            "Type opération : " + document.getDemande().getType_op() + "\n" +
                            "Montant : " + document.getDemande().getMontant() + "\n" +
                            dateReponse;
                    reponseField.setText(nomPrenomCompte);
                    break;
                case "Identité banquaire":
                    String nomPrenomIdentite = "Bonjour Mr/Mme " + document.getDemande().getNom() + " " + document.getDemande().getPrenom() + "\n" +
                            "Nom : " + document.getDemande().getNom() + "\n" +
                            "Prenom : " + document.getDemande().getPrenom() + "\n" +
                            "Rib : " + document.getDemande().getRib() + "\n" +
                            "Nombre compte : " + document.getDemande().getNb_compte() + "\n" +
                            "Solde : " + document.getDemande().getSolde() + "\n" +
                            "Statut compte : " + document.getDemande().getStatut_compte() + "\n" +
                            "Type opération : " + document.getDemande().getType_op() + "\n" +
                            "Montant : " + document.getDemande().getMontant() + "\n" +
                            dateReponse;
                    reponseField.setText(nomPrenomIdentite);
                    break;
                default:
                    // Pour tous les autres types de demande, ne rien afficher dans reponseField
                    reponseField.setText("");
                    break;
            }
        } else {
            clearFields(); // Méthode pour effacer les champs
        }
    }


    private void clearFields() {
        reponseField.clear();
        dateField.clear();
    }



    @FXML

    private void ajouterDocument(ActionEvent event) {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            String newReponse;

            // Sinon, utiliser la réponse saisie dans le champ
            newReponse = reponseField.getText();
            if (newReponse != null && !newReponse.isEmpty()) {
                Document newDocument = new Document();
                newDocument.setReponse(newReponse);
                newDocument.setDate(LocalDate.now()); // Définir la date actuelle
                newDocument.setId_dem(selectedDocument.getDemande().getId()); // Utiliser l'id de la demande associée

                documentService.insert(newDocument);

                loadDocuments();
                showAlert("Ajout Réussi", null, "La réponse a été ajoutée avec succès.");
            } else {
                System.out.println("La réponse ne peut pas être vide.");
            }
        } else {
            System.out.println("Aucun document sélectionné pour modification.");
        }
    }


    @FXML
    private void modifierDocument() {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            String newReponse = reponseField.getText();

            if (newReponse != null && !newReponse.isEmpty()) {
                selectedDocument.setReponse(newReponse);
                selectedDocument.setDate(LocalDate.now()); // Set the date to the current date

                documentService.update(selectedDocument);

                loadDocuments();
                showAlert("Modification Réussie", null, "La réponse a été modifiée avec succès.");
            } else {
                System.out.println("La réponse ne peut pas être vide.");
            }
        } else {
            System.out.println("Aucun document sélectionné pour modification.");
        }
    }

    @FXML
    private void supprimerDocument() {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            documentService.delete(selectedDocument);
            loadDocuments();
            showAlert("Suppression Réussie", null, "Le document a été supprimé avec succès.");
        } else {
            System.out.println("Aucun document sélectionné pour suppression.");
        }
    }



    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Appliquer le style personnalisé au popup
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/Main/style.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("popup");

        alert.showAndWait();
    }


    @FXML
    private void modifierStatut() {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            String newStatut = statutComboBox.getValue();
            if (newStatut != null && !newStatut.isEmpty()) {
                selectedDocument.getDemande().setStatut(newStatut);
                demandeService.update(selectedDocument.getDemande(), selectedDocument.getDemande().getId());
                loadDocuments();
                showAlert("Modification du Statut Réussie", null, "Le statut a été modifié avec succès.");

                // Send email
                String email = selectedDocument.getDemande().getEmail();

                if (email != null) { // Add this null check
                    String subject;
                    String body;
                    if ("terminé".equals(newStatut)) {
                        subject = "Votre demande est terminée";
                        body = "Bonjour,\n  Votre demande est terminée.\n  Vous pouvez consulter notre application pour voir la réponse.\nCordialement.\nMerci pour votre confiance.\nBnekti ";
                    } else if ("annulée".equals(newStatut)) {
                        subject = "Votre demande est annulée";
                        body = "Bonjour,\n  Votre demande est annulée. \n  Pour connaître les raisons de l'annulation, vous pouvez consulter notre application ou appeler notre service client.\nCordialement.\nMerci pour votre confiance.\n Bnekti ";
                    } else {
                        return;
                    }

                    documentService.sendEmail(email, subject, body);
                }
            } else {
                System.out.println("Veuillez sélectionner un statut.");
            }
        } else {
            System.out.println("Aucun document sélectionné pour modifier le statut.");
        }
    }
    @FXML
    private void handlehomeButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre_admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleusersButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/UserView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Users Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlecreditButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Credits Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleassuranceButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/Assurance_Admin.fxml"));
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
    private void handlesignout(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            UserSession.clearSession();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre_admin.fxml"));
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
    private void handlereclamationButtonuser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/AdminChart.fxml"));
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
