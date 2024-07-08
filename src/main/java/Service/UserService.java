package Service;

import entite.Compte;
import entite.User;
import util.DatabaseConnection;
import util.DatabaseUtil;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.sql.*;

public class UserService implements UserServiceInterface {
    private static Connection cnx;
    @Override
    public void createUser(User user) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String userQuery = "INSERT INTO user (nom, prenom, age, mdp, CIN, role, nb_compte,email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStatement = connection.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, user.getNom());
                userStatement.setString(2, user.getPrenom());
                userStatement.setInt(3, user.getAge());
                userStatement.setString(4, user.getMdp());
                userStatement.setInt(5, user.getCin());
                userStatement.setString(6, user.getRole());
                userStatement.setInt(7, 0); // nb_compte is initially 0
                userStatement.setString(8, user.getEmail());
                userStatement.executeUpdate();

                ResultSet rs = userStatement.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        }
    }
    @Override

    public boolean isCINExists(int cin) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE CIN = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cin);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET nom = ?, prenom = ?, age = ?, mdp = ?, CIN = ?, role = ? ,email= ?  WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setInt(3, user.getAge());
            statement.setString(4, user.getMdp());
            statement.setInt(5, user.getCin());
            statement.setString(6, user.getRole());
            statement.setString(7, user.getEmail());
            statement.setInt(8, user.getId());

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
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) { // MySQL error code for foreign key constraint violation
                throw new SQLException("Cannot delete user because they have associated accounts.");
            } else {
                throw e;
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
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setNbCompte(resultSet.getInt("nb_compte"));
                users.add(user);
            }
        }
        return users;
    }
    @Override
    public void sendCredentials(User user) {
        // Email configuration
        String to = user.getEmail();
        String from = "zied.s@convegen.io";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587"); // replace with your SMTP port
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("zied.s@convergen.io", "yxdnryxspyycpvjd"); // replace with your credentials
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Your Access Credentials");
            message.setText("CIN: " + user.getCin() + "\nPassword: " + user.getMdp());

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
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
