package Service;

import entite.User;
import entite.Compte;
import entite.UserWithCompte;
import util.DatabaseUtil;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService  {

    public void createUser(User user) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            // Insert user
            String userQuery = "INSERT INTO user (nom, prenom, age, mdp, CIN, role, nb_compte) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, user.getNom());
                userStatement.setString(2, user.getPrenom());
                userStatement.setInt(3, user.getAge());
                userStatement.setString(4, user.getMdp());
                userStatement.setInt(5, user.getCin());
                userStatement.setString(6, user.getRole());
                userStatement.setInt(7, 0); // nb_compte is initially 0
                userStatement.executeUpdate();

                ResultSet rs = userStatement.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        }
    }


    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET nom = ?, prenom = ?, age = ?, mdp = ?, CIN = ?, role = ?, nb_compte = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setInt(3, user.getAge());
            statement.setString(4, user.getMdp());
            statement.setInt(5, user.getCin());
            statement.setString(6, user.getRole());
            statement.setInt(7, user.getNbCompte());
            statement.setInt(8, user.getId());
            statement.executeUpdate();
        }
    }


    public void deleteUser(int userId) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            // First delete associated compte records
            String deleteCompteQuery = "DELETE FROM compte WHERE id_user = ?";
            try (PreparedStatement compteStatement = connection.prepareStatement(deleteCompteQuery)) {
                compteStatement.setInt(1, userId);
                compteStatement.executeUpdate();
            }

            // Then delete the user record
            String deleteUserQuery = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement userStatement = connection.prepareStatement(deleteUserQuery)) {
                userStatement.setInt(1, userId);
                userStatement.executeUpdate();
            }
        }
    }


    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setAge(resultSet.getInt("age"));
                user.setMdp(resultSet.getString("mdp"));
                user.setCin(resultSet.getInt("CIN"));
                user.setRole(resultSet.getString("role"));
                user.setNbCompte(resultSet.getInt("nb_compte"));
                users.add(user);
            }
        }
        return users;
    }


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

    public void createCompte(Compte compte) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String compteQuery = "INSERT INTO compte (type, num, rib, date_ouverture, date_validite, statut, solde, devise, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement compteStatement = connection.prepareStatement(compteQuery, Statement.RETURN_GENERATED_KEYS)) {
                compteStatement.setString(1, compte.getType());
                compteStatement.setInt(2, compte.getNum());
                compteStatement.setLong(3, 1266598);
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
}
