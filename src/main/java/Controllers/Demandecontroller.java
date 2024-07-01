package Controllers;


import Service.Demandeservice;
import entite.Demande;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import util.MyConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Demandecontroller {
    private Demandeservice demandeService = new Demandeservice();
    @FXML
    private TableView<Demande> demandeTable;
    @FXML
    private TableColumn<Demande, Integer> idColumn;
    @FXML
    private TableColumn<Demande, String> typeColumn;
    @FXML
    private TableColumn<Demande, String> statutColumn;
    @FXML
    private TableColumn<Demande, String> descriptionColumn;
    @FXML
    private TableColumn<Demande, Integer> userIdColumn;
    @FXML
    private TableColumn<Demande, LocalDate> dateColumn;

    @FXML
    private TextField idField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField statutField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField userIdField;
    @FXML
    private TextField dateField;

    @FXML
    private Button btnisert;
    @FXML
    private Button bttnsupp;
    int index=-1;
    private Demandeservice DemandeService = new Demandeservice();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadDemandes();
    }
    @FXML
    private void loadDemandes() {
        List<Demande> demandes = DemandeService.readAll();
        demandeTable.getItems().setAll(demandes);
    }


    private void createDemande() {
        String type = typeField.getText();
        String statut = statutField.getText();
        String description = descriptionField.getText();
        int id_user = Integer.parseInt(userIdField.getText());
        LocalDate date = LocalDate.parse(dateField.getText());

        Demande demande = new Demande(type, statut, description, id_user, date);
        DemandeService.insert(demande);
        loadDemandes();}

   /* @FXML
    private void updateDemande() {
        int id = Integer.parseInt(idField.getText());
        String type = typeField.getText();
        String statut = statutField.getText();
        String description = descriptionField.getText();
        int id_user = Integer.parseInt(userIdField.getText());
        LocalDate date = LocalDate.parse(dateField.getText());

        Demande demande = new Demande(id, type, statut, description, id_user, date);
        DemandeService.update(demande);
        loadDemandes();}*/

    @FXML
    void getSelected(javafx.scene.input.MouseEvent event) {
        index = demandeTable.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }

        typeField.setText(typeColumn.getCellData(index).toString());
        statutField.setText(statutColumn.getCellData(index).toString());
        descriptionField.setText(descriptionColumn.getCellData(index).toString());
        userIdField.setText(userIdColumn.getCellData(index).toString());
        dateField.setText(dateColumn.getCellData(index).toString());
    }

    @FXML
    public void edit(ActionEvent event){
        try {
            String val1 = typeField.getText();
            String val2 = statutField.getText();
            String val3 = descriptionField.getText();
            int id_user = Integer.parseInt(userIdField.getText());
            LocalDate date = LocalDate.parse(dateField.getText());

            String query = "UPDATE demande SET type = ?, statut = ?, description = ?, id_user = ?, date = ? WHERE type = ?";
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(query);
            ps.setString(1, val1);
            ps.setString(2, val2);
            ps.setString(3, val3);
            ps.setInt(4, id_user);
            ps.setDate(5, java.sql.Date.valueOf(date));
            ps.setString(6, val1);

            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        loadDemandes();
    }


    @FXML
    private  void deleteDemande() {
        int id = Integer.parseInt(idField.getText());
        String type = typeField.getText();
        String statut = statutField.getText();
        String description = descriptionField.getText();
        int id_user = Integer.parseInt(userIdField.getText());
        LocalDate date = LocalDate.parse(dateField.getText());

        Demande demande = new Demande(id, type, statut, description, id_user, date);
        DemandeService.delete(demande);
        loadDemandes();}
    @FXML
    private void Delete() {
        Demandeservice hot=new Demandeservice();
        Demande h=new Demande();
        h=demandeTable.getSelectionModel().getSelectedItem();
        hot.delete(h);
        loadDemandes();
    }


    @FXML
    private void hundleButtonAction(ActionEvent event) {
        if(event.getSource()==btnisert){
            createDemande();
        }
        else if(event.getSource()==bttnsupp){
            Delete();
        }


    }

}
