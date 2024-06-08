package Controllers;

import entite.Transaction;
import Service.TransactionService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionController {

    @FXML
    private TableView<Transaction> tableView;
    @FXML
    private TableColumn<Transaction, Integer> colId;
    @FXML
    private TableColumn<Transaction, Integer> colPoliceId;
    @FXML
    private TableColumn<Transaction, String> colDate;
    @FXML
    private TableColumn<Transaction, String> colType;
    @FXML
    private TableColumn<Transaction, Double> colMontant;
    @FXML
    private TableColumn<Transaction, String> colStatut;
    @FXML
    private TextField txtPoliceId;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtType;
    @FXML
    private TextField txtMontant;
    @FXML
    private TextField txtStatut;

    private TransactionService transactionService = new TransactionService();
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    public TransactionController(TextField txtMontant, TextField txtStatut) {
        this.txtMontant = txtMontant;
        this.txtStatut = txtStatut;
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPoliceId.setCellValueFactory(new PropertyValueFactory<>("policeId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statute"));

        loadTransactions();
    }

    private void loadTransactions() {
        transactionList.clear();
        transactionList.addAll(transactionService.getAllTransactions());
        tableView.setItems(transactionList);
    }

    @FXML
    private void handleAddTransaction() {
        Transaction transaction = new Transaction(
                Integer.parseInt(txtPoliceId.getText()),
                txtDate.getText(),
                txtType.getText(),
                Double.parseDouble(txtMontant.getText()),
                txtStatut.getText()
        );

        transactionService.addTransaction(transaction);
        loadTransactions();
    }

    @FXML
    private void handleUpdateTransaction() {
        Transaction selectedTransaction = tableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            return;
        }

        selectedTransaction.setDate(txtDate.getText());
        selectedTransaction.setType(txtType.getText());
        selectedTransaction.setMontant(Double.parseDouble(txtMontant.getText()));
        selectedTransaction.setStatut(txtStatut.getText());

        transactionService.updateTransaction(selectedTransaction);
        loadTransactions();
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = tableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            return;
        }

        transactionService.deleteTransaction(selectedTransaction.getId());
        loadTransactions();
    }
}
