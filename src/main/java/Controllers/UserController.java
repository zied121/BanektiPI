package Controllers;

import entite.User;
import entite.UserWithCompte;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

public class UserController {
    @FXML
    private TableView<UserWithCompte> userTable;
    @FXML
    private TableColumn<UserWithCompte, Integer> userIdColumn;
    @FXML
    private TableColumn<UserWithCompte, String> nomColumn;
    @FXML
    private TableColumn<UserWithCompte, String> prenomColumn;
    @FXML
    private TableColumn<UserWithCompte, Integer> ageColumn;
    @FXML
    private TableColumn<UserWithCompte, String> mdpColumn;
    @FXML
    private TableColumn<UserWithCompte, Integer> cinColumn;
    @FXML
    private TableColumn<UserWithCompte, String> roleColumn;
    @FXML
    private TableColumn<UserWithCompte, Integer> nbCompteColumn;
    @FXML
    private TableColumn<UserWithCompte, String> typeColumn;
    @FXML
    private TableColumn<UserWithCompte, Integer> numColumn;
    @FXML
    private TableColumn<UserWithCompte, Integer> ribColumn;
    @FXML
    private TableColumn<UserWithCompte, Timestamp> dateOuvertureColumn;
    @FXML
    private TableColumn<UserWithCompte, Date> dateValiditeColumn;
    @FXML
    private TableColumn<UserWithCompte, String> statutColumn;
    @FXML
    private TableColumn<UserWithCompte, Float> soldeColumn;
    @FXML
    private TableColumn<UserWithCompte, String> deviseColumn;

    @FXML
    private TextField userIdField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField mdpField;
    @FXML
    private TextField cinField;
    @FXML
    private TextField roleField;
    @FXML
    private TextField nbCompteField;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        mdpColumn.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        nbCompteColumn.setCellValueFactory(new PropertyValueFactory<>("nbCompte"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        numColumn.setCellValueFactory(new PropertyValueFactory<>("num"));
        ribColumn.setCellValueFactory(new PropertyValueFactory<>("rib"));
        dateOuvertureColumn.setCellValueFactory(new PropertyValueFactory<>("dateOuverture"));
        dateValiditeColumn.setCellValueFactory(new PropertyValueFactory<>("dateValidite"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        deviseColumn.setCellValueFactory(new PropertyValueFactory<>("devise"));

        try {
            loadUsersWithCompte();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadUsersWithCompte() throws SQLException {
        List<UserWithCompte> usersWithCompte = userService.getAllUsersWithCompte();
        userTable.getItems().setAll(usersWithCompte);
    }

    @FXML
    private void createUser() throws SQLException {
        User user = new User();
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        user.setMdp(mdpField.getText());
        user.setCin(Integer.parseInt(cinField.getText()));
        user.setRole(roleField.getText());
        user.setNbCompte(Integer.parseInt(nbCompteField.getText()));
        userService.createUser(user);
        loadUsersWithCompte();
    }

    @FXML
    private void updateUser() throws SQLException {
        User user = new User();
        user.setId(Integer.parseInt(userIdField.getText()));
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        user.setMdp(mdpField.getText());
        user.setCin(Integer.parseInt(cinField.getText()));
        user.setRole(roleField.getText());
        user.setNbCompte(Integer.parseInt(nbCompteField.getText()));
        userService.updateUser(user);
        loadUsersWithCompte();
    }

    @FXML
    private void deleteUser() throws SQLException {
        int userId = Integer.parseInt(userIdField.getText());
        userService.deleteUser(userId);
        loadUsersWithCompte();
    }
}
