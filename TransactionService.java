package Service;

import util.DatabaseUtil;
import entite.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    Connection connection;

    public TransactionService() {
         connection = DatabaseUtil.getConnection() ;
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions";

        try (
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getString(5)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        String query = "INSERT INTO Transactions(police_id, date, type, montant, statut) VALUES(?, ?, ?, ?, ?)";

        try (
             PreparedStatement stmt = connection.prepareStatement(query)) {


            stmt.setString(1, transaction.getDate());
            stmt.setString(2, transaction.getType());
            stmt.setDouble(3, transaction.getMontant());
            stmt.setString(4, transaction.getStatut());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTransaction(Transaction transaction) {
        String query = "UPDATE Transactions SET police_id = ?, date = ?, type = ?, montant = ?, statut = ? WHERE id = ?";

        try (
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, transaction.getDate());
            stmt.setString(2, transaction.getType());
            stmt.setDouble(3, transaction.getMontant());
            stmt.setString(4, transaction.getStatut());
            stmt.setInt(5, transaction.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction(int id) {
        String query = "DELETE FROM Transactions WHERE id = ?";

        try (
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
