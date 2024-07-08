package Service;

import entite.Compte;
import entite.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private static Connection cnx;
    public static User getUserIdByCIN(String CIN) {
        cnx = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT id, role FROM user WHERE CIN = ?";
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, CIN);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String role = resultSet.getString("role");
                return new User(id, role);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public static Map<String, String> getUserDetailsById(String userId) {
        cnx = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT nom, prenom FROM user WHERE id = ?";
        Map<String, String> userDetails = new HashMap<>();

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userDetails.put("nom", resultSet.getString("nom"));
                userDetails.put("prenom", resultSet.getString("prenom"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userDetails;
    }
    public static List<Compte> getUserAccountsById(String userId) {
        cnx = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT * FROM compte WHERE id_user = ?";
        List<Compte> accounts = new ArrayList<>();

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Compte compte = new Compte();
                compte.setId(Integer.parseInt(resultSet.getString("id")));
                compte.setRib(Long.parseLong(resultSet.getString("RIB")));
                compte.setSolde((float) resultSet.getDouble("solde"));
                compte.setStatus(resultSet.getString("statut"));
                accounts.add(compte);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }
}
