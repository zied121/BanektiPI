package controllers;

import entite.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import service.Reclamationservice;

import java.time.LocalDate;

public class AjouterReclamation {

    @FXML
    private TextField txt_stat;

    @FXML
    private TextField txtdes;

    //@FXML
    //private TextField txtrec;

    @FXML
    private TextField txttype;

    @FXML
    private TextField txtuser;

    @FXML
    void action_ajouter(ActionEvent event) {
        String type_rec = txttype.getText();
        String description = txtdes.getText();
        int statut = Integer.parseInt(txt_stat.getText());
        int id_user = Integer.parseInt(txtuser.getText());
        LocalDate date_rec = LocalDate.now();

        Reclamation reclamation = new Reclamation(type_rec, description, statut, date_rec, id_user);
        Reclamationservice reclamationservice = new Reclamationservice();
        reclamationservice.insertPST(reclamation);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../AfficherReclamation.fxml"));
try {
    Parent root = loader.load();
}
catch (Exception e) {
    e.printStackTrace();
}
    }

    @FXML
    void action_modifier(ActionEvent event) {
        String type_rec = txttype.getText();
        String description = txtdes.getText();
        int statut = Integer.parseInt(txt_stat.getText());
        int id_user = Integer.parseInt(txtuser.getText());
        LocalDate date_rec = LocalDate.now();
        Reclamation reclamation = new Reclamation(type_rec, description, statut, date_rec, id_user);
        Reclamationservice reclamationservice = new Reclamationservice();
        reclamationservice.update(reclamation);
    }

    @FXML
    void action_supprimer(ActionEvent event) {
        int id_rec = Integer.parseInt(txtuser.getText());
        Reclamation reclamation = new Reclamation();
        reclamation.setId_rec(id_rec);
        Reclamationservice reclamationservice = new Reclamationservice();
        reclamationservice.delete(reclamation);
    }

    @FXML
    void action_afficher(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../AfficherReclamation.fxml"));
        try {
            Parent root = loader.load();
            AfficherReclamation afficherReclamation = loader.getController();
        }
        catch (Exception e) {
            e.printStackTrace();
}


        //reclamationservice.update(reclamation);
//        void  action_modifier (ActionEvent event){
//            String type_rec = txttype.getText();
//            String description = txtdes.getText();
//            int statut = Integer.parseInt(txt_stat.getText());
//            int id_user = Integer.parseInt(txtuser.getText());
//            LocalDate date_rec = LocalDate.now();
//            Reclamation reclamation = new Reclamation(type_rec, description, statut, date_rec, id_user);
//            Reclamationservice reclamationservice = new Reclamationservice();
//            reclamationservice.update(reclamation);
//        }
    }
}
