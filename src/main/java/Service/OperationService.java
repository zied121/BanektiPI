package Service;

import entite.Operation;
import util.DatabaseUtil;
import util.SmsUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperationService {

    public void addOperation(Operation operation) throws SQLException {
        String query = "INSERT INTO operation (type_op, montant, id_compte, date_op, Rib_compte_destination) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, operation.getTypeOp());
            pst.setDouble(2, operation.getMontant());
            pst.setInt(3, operation.getIdCompte());
            pst.setDate(4, new Date(System.currentTimeMillis()));
            pst.setLong(5, operation.getRibCompteDestination());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int operationId = rs.getInt(1);
                operation.setId(operationId);

                // Retrieve the operation details including the date_op
                retrieveOperationDetails(operationId, operation);
            }

            // Update the balance of the source account
            updateAccountBalance(operation.getIdCompte(), -operation.getMontant());

            // Update the balance of the destination account
            int destinationAccountId = getAccountIdByRib(operation.getRibCompteDestination());
            if (destinationAccountId != -1) {
                updateAccountBalance(destinationAccountId, operation.getMontant());
            } else {
                throw new SQLException("Destination account with specified RIB does not exist.");
            }

            // Send SMS notification
            sendSmsNotification(operation);
        }
    }

    private void retrieveOperationDetails(int operationId, Operation operation) throws SQLException {
        String query = "SELECT * FROM operation WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, operationId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                operation.setDateOp(rs.getDate("date_op"));
            }
        }
    }

    private void sendSmsNotification(Operation operation) {
        try {
            String phoneNumber = "+216" + getPhoneNumberByAccountId(operation.getIdCompte()); // Assuming Tunisian phone numbers
            String message = "An operation has been performed on your account. Type: " + operation.getTypeOp() +
                    ", Amount: " + operation.getMontant() + ", Date: " + operation.getDateOp();
            SmsUtil.sendSms(phoneNumber, message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Operation> getOperationsByAccountIds(List<Integer> accountIds) throws SQLException {
        String query = "SELECT * FROM operation WHERE id_compte IN (" +
                accountIds.stream().map(String::valueOf).reduce((a, b) -> a + "," + b).orElse("") + ")";
        List<Operation> operations = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setTypeOp(rs.getString("type_op"));
                operation.setMontant(rs.getDouble("montant"));
                operation.setIdCompte(rs.getInt("id_compte"));
                operation.setDateOp(rs.getDate("date_op"));
                operation.setRibCompteDestination(rs.getLong("Rib_compte_destination"));
                operations.add(operation);
            }
        }
        return operations;
    }

    public double getAccountBalance(int accountId) throws SQLException {
        String query = "SELECT solde FROM compte WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, accountId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble("solde");
            }
        }
        return 0.0;
    }

    private void updateAccountBalance(int accountId, double amount) throws SQLException {
        String query = "UPDATE compte SET solde = solde + ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setDouble(1, amount);
            pst.setInt(2, accountId);
            pst.executeUpdate();
        }
    }

    public int getAccountIdByRib(long rib) throws SQLException {
        String query = "SELECT id FROM compte WHERE rib = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, rib);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    public String getPhoneNumberByAccountId(int accountId) throws SQLException {
        String query = "SELECT num FROM compte WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, accountId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("num");
            }
        }
        return null;
    }
}
