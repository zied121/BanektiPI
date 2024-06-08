package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import service.Documentservice;
import entite.Document;
import java.time.LocalDate;


public class AjouterDocument {

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txttype;

    @FXML
    private TextField txturl;

    @FXML
    private TextField txtuser;

    @FXML
    void action_ajouter(ActionEvent event) {
        String nom = txtnom.getText();
        String type = txttype.getText();
        String url = txturl.getText();
        LocalDate date = LocalDate.now(); // Date actuelle générée dynamiquement
        int id_user = Integer.parseInt(txtuser.getText());
        Document document = new Document(nom, type, url, date, id_user);
        Documentservice documentservice = new Documentservice();
        documentservice.insertPST(document);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDocument.fxml"));
            Parent root = loader.load();

            AfficherDocument afficherDocument = loader.getController();
            afficherDocument.setTxtlist(documentservice.readAll().toString());
            afficherDocument.setTxtnom(nom);
            afficherDocument.setTxttype(type);
            afficherDocument.setTxturl(url);

            Stage stage = (Stage) txtnom.getScene().getWindow();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
