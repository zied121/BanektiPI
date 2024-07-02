package Controllers;
import entite.User;
import entite.Operation;
import Service.OperationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.UserSession;

import java.sql.SQLException;
import java.util.Date;

public class    OperationController {

    @FXML
    private TextField typeOpField;
    @FXML
    private TextField montantField;
    @FXML
    private TextField idCompteField;
    @FXML
    private TextField idCompteDestinationField;
    @FXML
    private DatePicker dateOpPicker;
    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<Operation, String> typeOpColumn;
    @FXML
    private TableColumn<Operation, Double> montantColumn;
    @FXML
    private TableColumn<Operation, Long> idCompteColumn;
    @FXML
    private TableColumn<Operation, Long> idCompteDestinationColumn;
    @FXML
    private TableColumn<Operation, Date> dateOpColumn;
    private User user;


    private ObservableList<Operation> operationData = FXCollections.observableArrayList();
    private OperationService operationService = new OperationService();

    @FXML
    private void initialize() {
        User user = UserSession.getInstance().getUser();
        System.out.println(user.getId());
        typeOpColumn.setCellValueFactory(new PropertyValueFactory<>("typeOp"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        idCompteColumn.setCellValueFactory(new PropertyValueFactory<>("idCompte"));
        idCompteDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("idCompteDestination"));
        dateOpColumn.setCellValueFactory(new PropertyValueFactory<>("dateOp"));

        operationTable.setItems(operationData);
    }

    @FXML
    private void handleAddOperation() {
        try {
            Operation operation = new Operation();
            operation.setTypeOp(typeOpField.getText());
            operation.setMontant(Double.parseDouble(montantField.getText()));
            operation.setIdCompte(Long.parseLong(idCompteField.getText()));
            operation.setDateOp(java.sql.Date.valueOf(dateOpPicker.getValue()));
            operation.setIdCompteDestination(Long.parseLong(idCompteDestinationField.getText()));

            operationService.addOperation(operation);
            operationData.add(operation);

            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewOperations() {
        try {
            Long accountId = Long.parseLong(idCompteField.getText());
            operationData.setAll(operationService.getOperationsByAccountId(accountId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        typeOpField.clear();
        montantField.clear();
        idCompteField.clear();
        idCompteDestinationField.clear();
        dateOpPicker.setValue(null);
    }
    public void setUserData(User user) {
        this.user = user;
        // Use the user data to update the view
       System.out.println("Welcome, " + user.getId());
    }
}
