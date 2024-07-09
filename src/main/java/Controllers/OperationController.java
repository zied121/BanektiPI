package Controllers;

import entite.User;
import entite.Operation;
import entite.Compte;
import Service.OperationService;
import Service.CompteServiceInterface;
import Service.CompteService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import util.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OperationController {

    @FXML
    private ComboBox<String> typeOpField;
    @FXML
    private TextField montantField;
    @FXML
    private ComboBox<Compte> idCompteField;
    @FXML
    private TextField RibCompteDestinationField;
    @FXML
    private DatePicker dateOpPicker;
    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<Operation, String> typeOpColumn;
    @FXML
    private TableColumn<Operation, Double> montantColumn;
    @FXML
    private TableColumn<Operation, Integer> idCompteColumn;
    @FXML
    private TableColumn<Operation, Long> RibCompteDestinationColumn;
    @FXML
    private TableColumn<Operation, Date> dateOpColumn;
    private User user;

    private ObservableList<Operation> operationData = FXCollections.observableArrayList();
    private OperationService operationService = new OperationService();
    private CompteServiceInterface compteService = new CompteService();

    @FXML
    private void initialize() {
        User user = UserSession.getInstance().getUser();
        System.out.println(user.getId());
        typeOpColumn.setCellValueFactory(new PropertyValueFactory<>("typeOp"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        idCompteColumn.setCellValueFactory(new PropertyValueFactory<>("idCompte"));
        RibCompteDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("RibCompteDestination"));
        dateOpColumn.setCellValueFactory(new PropertyValueFactory<>("dateOp"));

        operationTable.setItems(operationData);

        // Initialize typeOpField ComboBox with operation types
        typeOpField.getItems().addAll("Deposit", "Withdrawal", "Transfer");

        // Load the user's accounts into the idCompteField ComboBox
        try {
            List<Compte> comptes = compteService.getComptesByUserId(user.getId());
            idCompteField.setItems(FXCollections.observableArrayList(comptes));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set a custom cell factory to display the rib
        idCompteField.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(compte.getRib()));
                }
            }
        });

        // Set a custom converter to convert Compte objects to their rib string representation
        idCompteField.setConverter(new StringConverter<>() {
            @Override
            public String toString(Compte compte) {
                if (compte == null) {
                    return null;
                } else {
                    return String.valueOf(compte.getRib());
                }
            }

            @Override
            public Compte fromString(String rib) {
                return idCompteField.getItems().stream()
                        .filter(compte -> String.valueOf(compte.getRib()).equals(rib))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Load operations when the interface is initialized
        loadOperations();
    }

    private void loadOperations() {
        try {
            List<Compte> comptes = idCompteField.getItems();
            List<Integer> accountIds = comptes.stream().map(Compte::getId).collect(Collectors.toList());

            operationData.setAll(operationService.getOperationsByAccountIds(accountIds));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddOperation() {
        try {
            if (!isMontantValid(montantField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Amount must be a valid number.");
                return;
            }

            int selectedAccountId = idCompteField.getValue().getId();
            double amount = Double.parseDouble(montantField.getText());
            double currentBalance = operationService.getAccountBalance(selectedAccountId);

            if (currentBalance < amount) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Funds", "The account does not have enough balance for this operation.");
                return;
            }

            long ribDestination = Long.parseLong(RibCompteDestinationField.getText());
            int destinationAccountId = operationService.getAccountIdByRib(ribDestination);

            if (destinationAccountId == -1) {
                showAlert(Alert.AlertType.ERROR, "Invalid RIB", "The destination account with specified RIB does not exist.");
                return;
            }

            Operation operation = new Operation();
            operation.setTypeOp(typeOpField.getValue());
            operation.setMontant(amount);
            operation.setIdCompte(selectedAccountId);
//            operation.setDateOp(java.sql.Date.valueOf(dateOpPicker.getValue()));
            operation.setRibCompteDestination(ribDestination);

            operationService.addOperation(operation);
            operationData.add(operation);

            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        typeOpField.setValue(null);
        montantField.clear();
        idCompteField.setValue(null);
        RibCompteDestinationField.clear();
//        dateOpPicker.setValue(null);
    }

    private boolean isMontantValid(String montant) {
        try {
            Double.parseDouble(montant);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
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
    private void handlehomeButton(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/UserView.fxml"));
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
    private void handlereclamationButtonuser(ActionEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/fenetre1_reclamation.fxml"));
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
