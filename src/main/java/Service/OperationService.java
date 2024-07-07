package Service;

import entite.Operation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseUtil;

public class OperationService {

    public void addOperation(Operation operation) throws SQLException {
        String query = "INSERT INTO operation (type_op, montant, id_compte, date_op, Rib_compte_destination) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, operation.getTypeOp());
            pst.setDouble(2, operation.getMontant());
            pst.setInt(3, operation.getIdCompte());
            pst.setDate(4, new java.sql.Date(operation.getDateOp().getTime()));
            pst.setLong(5, operation.getRibCompteDestination());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                operation.setId(rs.getInt(1));
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
}
