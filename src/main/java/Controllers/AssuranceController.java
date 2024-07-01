package Controllers;

import Service.AssuranceService;
import entite.Assurance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AssuranceController {
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

    public AssuranceController() {
        assuranceService = new AssuranceService();
    }

    @FXML
    public void handleAddAssurance(ActionEvent actionEvent) {
        String idUser = idUserField.getText();
        String type = typeField.getText();
        String dateDebut = dateDebutField.getText();
        String dateFin = dateFinField.getText();
        String document = documentField.getText();
        String image = imageField.getText();

        Assurance assurance = new Assurance();
        assurance.setIdUser(idUser);
        assurance.setType(type);
        assurance.setDateDebut(dateDebut);
        assurance.setDateFin(dateFin);
        assurance.setDocument(document);
        assurance.setImage(image);

        boolean success = assuranceService.addAssurance(assurance);
        if (success) {
            System.out.println("Assurance added successfully!");
        } else {
            System.out.println("Failed to add assurance.");
        }
    }

    @FXML
    public void handleUploadDocument(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showOpenDialog(new Stage());
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
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                String imageUrl = assuranceService.uploadFileToCloudinary(file);
                imageField.setText(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
