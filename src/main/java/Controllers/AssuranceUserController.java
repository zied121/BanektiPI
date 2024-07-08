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
public class AssuranceUserController {

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
    private String userId;
    private User user;

    public AssuranceUserController() {
        assuranceService = new AssuranceService();
    }
    @FXML
    public void initialize() {
        User user = UserSession.getInstance().getUser();
        this.userId = String.valueOf(user.getId());
        idUserField.setText(this.userId);

    }
    @FXML
    public void handleAddAssurance(ActionEvent actionEvent) {
        String idUser =idUserField.getText();
        String type = typeField.getText();
        String dateDebut = dateDebutField.getText();
        String dateFin = dateFinField.getText();
        String document = documentField.getText();
        String image = imageField.getText();


        if (!validateInputs(idUser, type, dateDebut, dateFin, document, image)) {
            return;
        }

        Assurance assurance = new Assurance();
        assurance.setIdUser(idUser);
        assurance.setType(type);
        assurance.setDateDebut(dateDebut);
        assurance.setDateFin(dateFin);
        assurance.setDocument(document);
        assurance.setImage(image);
       // assurance.setStatus("Pending"); // Default status for user-added assurances

        boolean success = assuranceService.addAssurance(assurance);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Assurance added successfully!");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add assurance.");
        }
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

    @FXML
    public void handleUploadDocument(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String documentUrl = assuranceService.uploadFileToCloudinary(file);
                documentField.setText(documentUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleUploadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String imageUrl = assuranceService.uploadFileToCloudinary(file);
                imageField.setText(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void handleDownloadDocument(String documentUrl) {
        if (documentUrl != null && !documentUrl.isEmpty()) {
            // Implement your logic to download the document using the URL
            // For example, opening it in a browser or using a download manager
            System.out.println("Downloading document from URL: " + documentUrl);
        } else {
            showAlert(Alert.AlertType.ERROR, "Download Failed", "No document URL available.");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void clearFields() {
        idUserField.clear();
        typeField.clear();
        dateDebutField.clear();
        dateFinField.clear();
        documentField.clear();
        imageField.clear();
    }
}
