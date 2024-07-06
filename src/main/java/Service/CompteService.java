package Service;

import entite.Compte;
import util.DatabaseUtil;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteService implements CompteServiceInterface {
    @Override
    public List<Compte> getComptesByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM compte WHERE id_user = ?";
        List<Compte> comptes = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Compte compte = new Compte();
                    compte.setId(resultSet.getInt("id"));
                    compte.setType(resultSet.getString("type"));
                    compte.setNum(resultSet.getInt("num"));
                    compte.setRib(resultSet.getLong("rib"));
                    compte.setDateOuverture(resultSet.getTimestamp("date_ouverture"));
                    compte.setDateValidite(resultSet.getDate("date_validite"));
                    compte.setStatut(resultSet.getString("statut"));
                    compte.setSolde(resultSet.getFloat("solde"));
                    compte.setDevise(resultSet.getString("devise"));
                    compte.setUserId(resultSet.getInt("id_user"));
                    comptes.add(compte);
                }
            }
        }
        return comptes;
    }

    @Override
    public void createCompte(Compte compte) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String compteQuery = "INSERT INTO compte (type, num, rib, date_ouverture, date_validite, statut, solde, devise, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement compteStatement = connection.prepareStatement(compteQuery, Statement.RETURN_GENERATED_KEYS)) {
                compteStatement.setString(1, compte.getType());
                compteStatement.setInt(2, compte.getNum());
                compteStatement.setLong(3, compte.getRib());
                compteStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                compteStatement.setDate(5, new Date(System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000))); // Example date
                compteStatement.setString(6, "Active"); // Example statut
                compteStatement.setFloat(7, 0.0f); // Initial balance
                compteStatement.setString(8, "USD"); // Example devise
                compteStatement.setInt(9, compte.getUserId());
                compteStatement.executeUpdate();

                ResultSet rs = compteStatement.getGeneratedKeys();
                if (rs.next()) {
                    compte.setId(rs.getInt(1));
                }

                // Update the user's nb_compte
                String updateUserQuery = "UPDATE user SET nb_compte = nb_compte + 1 WHERE id = ?";
                try (PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery)) {
                    updateUserStatement.setInt(1, compte.getUserId());
                    updateUserStatement.executeUpdate();
                }
            }
        }
    }

    @Override
    public boolean hasCourantAccount(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM compte WHERE type = 'courant' AND id_user = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }
    @Override
    public void updateCompte(Compte compte) throws SQLException {
        String query = "UPDATE compte SET type = ?, num = ?, rib = ?, statut = ?, solde = ?, devise = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, compte.getType());
            statement.setInt(2, compte.getNum());
            statement.setLong(3, compte.getRib());
            statement.setString(4, compte.getStatut());
            statement.setFloat(5, compte.getSolde());
            statement.setString(6, compte.getDevise());
            statement.setInt(7, compte.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteCompte(int compteId, int userId) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String deleteCompteQuery = "DELETE FROM compte WHERE id = ?";
            try (PreparedStatement compteStatement = connection.prepareStatement(deleteCompteQuery)) {
                compteStatement.setInt(1, compteId);
                compteStatement.executeUpdate();
            }

            // Update the user's nb_compte
            String updateUserQuery = "UPDATE user SET nb_compte = nb_compte - 1 WHERE id = ?";
            try (PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery)) {
                updateUserStatement.setInt(1, userId);
                updateUserStatement.executeUpdate();
            }
        }
    }

    // Generate a 13-digit RIB
    private long generateRib() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(52, random).longValue();
    }
}
