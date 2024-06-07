package Service;

import entite.User;
import entite.Compte;
import entite.UserWithCompte;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    // Create User and Compte
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
                userStatement.setInt(7, user.getNbCompte());
                userStatement.executeUpdate();

                ResultSet rs = userStatement.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }

            // Insert compte
            Compte compte = new Compte();
            compte.setUserId(user.getId());
            compte.setType("Default Type"); // Example type
            compte.setNum(12345); // Example num
            compte.setRib(67890); // Example RIB
            compte.setDateOuverture(new Timestamp(System.currentTimeMillis())); // Example date
            compte.setDateValidite(new Date(System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000))); // Example date
            compte.setStatut("Active"); // Example statut
            compte.setSolde(0.0f); // Initial balance
            compte.setDevise("USD"); // Example devise

            String compteQuery = "INSERT INTO compte (type, num, rib, date_ouverture, date_validite, statut, solde, devise, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement compteStatement = connection.prepareStatement(compteQuery, Statement.RETURN_GENERATED_KEYS)) {
                compteStatement.setString(1, compte.getType());
                compteStatement.setInt(2, compte.getNum());
                compteStatement.setInt(3, compte.getRib());
                compteStatement.setTimestamp(4, compte.getDateOuverture());
                compteStatement.setDate(5, compte.getDateValidite());
                compteStatement.setString(6, compte.getStatut());
                compteStatement.setFloat(7, compte.getSolde());
                compteStatement.setString(8, compte.getDevise());
                compteStatement.setInt(9, compte.getUserId());
                compteStatement.executeUpdate();

                ResultSet rs = compteStatement.getGeneratedKeys();
                if (rs.next()) {
                    compte.setId(rs.getInt(1));
                }
            }
        }
    }

    // Update User
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

    // Delete User and associated Compte
    public void deleteUser(int userId) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String deleteUserQuery = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement userStatement = connection.prepareStatement(deleteUserQuery)) {
                userStatement.setInt(1, userId);
                userStatement.executeUpdate();
            }

            String deleteCompteQuery = "DELETE FROM compte WHERE id_user = ?";
            try (PreparedStatement compteStatement = connection.prepareStatement(deleteCompteQuery)) {
                compteStatement.setInt(1, userId);
                compteStatement.executeUpdate();
            }
        }
    }

    // Get All Users with their Compte
    public List<UserWithCompte> getAllUsersWithCompte() throws SQLException {
        String query = "SELECT u.id, u.nom, u.prenom, u.age, u.mdp, u.CIN, u.role, u.nb_compte, c.type, c.num, c.rib, c.date_ouverture, c.date_validite, c.statut, c.solde, c.devise " +
                "FROM user u LEFT JOIN compte c ON u.id = c.id_user";
        List<UserWithCompte> usersWithCompte = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                UserWithCompte userWithCompte = new UserWithCompte();
                userWithCompte.setUserId(resultSet.getInt("u.id"));
                userWithCompte.setNom(resultSet.getString("u.nom"));
                userWithCompte.setPrenom(resultSet.getString("u.prenom"));
                userWithCompte.setAge(resultSet.getInt("u.age"));
                userWithCompte.setMdp(resultSet.getString("u.mdp"));
                userWithCompte.setCin(resultSet.getInt("u.CIN"));
                userWithCompte.setRole(resultSet.getString("u.role"));
                userWithCompte.setNbCompte(resultSet.getInt("u.nb_compte"));
                userWithCompte.setType(resultSet.getString("c.type"));
                userWithCompte.setNum(resultSet.getInt("c.num"));
                userWithCompte.setRib(resultSet.getInt("c.rib"));
                userWithCompte.setDateOuverture(resultSet.getTimestamp("c.date_ouverture"));
                userWithCompte.setDateValidite(resultSet.getDate("c.date_validite"));
                userWithCompte.setStatut(resultSet.getString("c.statut"));
                userWithCompte.setSolde(resultSet.getFloat("c.solde"));
                userWithCompte.setDevise(resultSet.getString("c.devise"));
                usersWithCompte.add(userWithCompte);
            }
        }
        return usersWithCompte;
    }
}
