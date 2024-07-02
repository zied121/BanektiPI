package Service;
import entite.Operation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseUtil;


public class OperationService {

    public void addOperation(Operation operation) throws SQLException {
        String query = "INSERT INTO operation (type_op, montant, id_compte, date_op, id_compte_destination) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, operation.getTypeOp());
            pst.setDouble(2, operation.getMontant());
            pst.setLong(3, operation.getIdCompte());
            pst.setDate(4, new java.sql.Date(operation.getDateOp().getTime()));
            pst.setLong(5, operation.getIdCompteDestination());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                operation.setId(rs.getLong(1));
            }
        }
    }

    public List<Operation> getOperationsByAccountId(Long accountId) throws SQLException {
        String query = "SELECT * FROM operation WHERE id_compte = ?";
        List<Operation> operations = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, accountId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getLong("id"));
                operation.setTypeOp(rs.getString("type_op"));
                operation.setMontant(rs.getDouble("montant"));
                operation.setIdCompte(rs.getLong("id_compte"));
                operation.setDateOp(rs.getDate("date_op"));
                operation.setIdCompteDestination(rs.getLong("id_compte_destination"));
                operations.add(operation);
            }
        }
        return operations;
    }
}


