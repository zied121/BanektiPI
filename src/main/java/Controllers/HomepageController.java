package Controllers;

import Service.OperationService;
import Service.UserService;
import entite.Operation;
import entite.User;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entite.Compte;
import Service.CompteService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.UserSession;
import javafx.scene.chart.LineChart;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomepageController {

    @FXML
    private Label userNameLabel;
    @FXML
    private TableView<Compte> accountsTable;
    @FXML
    private TableColumn<Compte, String> ribColumn;
    @FXML
    private TableColumn<Compte, Double> soldeColumn;
    @FXML
    private TableColumn<Compte, String> statusColumn;
    @FXML
    private Label totalBalanceLabel;
    @FXML
    private VBox recentTransactionsBox;
    @FXML
    private LineChart<Number, Number> moneyFlowChart;

    private String userId;
    private UserService userService = new UserService();
    private CompteService compteService = new CompteService();
    private OperationService operationService = new OperationService();
    private User user;

    public void initialize() {
        userId = String.valueOf(UserSession.getInstance().getUserId());
        user = UserSession.getInstance().getUser();
        System.out.println("User ID: " + user);
        loadUserAccountDetails();
        loadRecentTransactions();
        loadMoneyFlowChart();
    }

    private void loadUserAccountDetails() {
        try {
            List<Compte> comptes = compteService.getComptesByUserId(user.getId());
            double totalBalance = comptes.stream().mapToDouble(Compte::getSolde).sum();
            totalBalanceLabel.setText(String.format("$%,.2f", totalBalance));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRecentTransactions() {
        try {
            List<Compte> comptes = compteService.getComptesByUserId(user.getId());
            List<Integer> accountIds = comptes.stream().map(Compte::getId).collect(Collectors.toList());
            List<Operation> operations = operationService.getOperationsByAccountIds(accountIds);

            for (Operation operation : operations) {
                Label transactionLabel = new Label(
                        String.format("%s: %,.2f (Date: %s)",
                                operation.getTypeOp(),
                                operation.getMontant(),
                                operation.getDateOp().toString())
                );
                recentTransactionsBox.getChildren().add(transactionLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMoneyFlowChart() {
        try {
            List<Compte> comptes = compteService.getComptesByUserId(user.getId());
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Account Balance Over Time");

            for (Compte compte : comptes) {
                List<Integer> accountIds = comptes.stream().map(Compte::getId).collect(Collectors.toList());
                List<Operation> operations = operationService.getOperationsByAccountIds(accountIds);
                int dayCounter = 1;
                double currentBalance = 0;
                for (Operation operation : operations) {
                    currentBalance += operation.getMontant();
                    series.getData().add(new XYChart.Data<>(dayCounter++, currentBalance));
                }
            }

            moneyFlowChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlecreditButtoncredituser(ActionEvent event) {
        try {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/homePage.fxml"));
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
            stage.setTitle("Assurance Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
