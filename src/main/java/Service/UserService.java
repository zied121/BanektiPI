package Service;

import entite.User;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements UserServiceInterface {
    @Override
    public void createUser(User user) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
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

    @Override
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET nom = ?, prenom = ?, age = ?, mdp = ?, CIN = ?, role = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setInt(3, user.getAge());
            statement.setString(4, user.getMdp());
            statement.setInt(5, user.getCin());
            statement.setString(6, user.getRole());
            statement.setInt(7, user.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int userId) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String deleteUserQuery = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement userStatement = connection.prepareStatement(deleteUserQuery)) {
                userStatement.setInt(1, userId);
                userStatement.executeUpdate();
            }
        }
    }

    @Override
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
}
