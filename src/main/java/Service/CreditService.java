package Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entite.Credit;
import util.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.control.DatePicker;
public class CreditService {
    private Cloudinary cloudinary;
    private Connection cnx;

    public CreditService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dsyszjsc2",
                "api_key", "967221561574594",
                "api_secret", "IosNjEIg0uEroIu7yXlS08os2bg"
        ));
        cnx = DatabaseConnection.getInstance().getConnection();

    }

    public boolean updateCreditDetails(String idCompte, String typeCredit, double montant, String status, String echeancier, String document) {
        String query = "UPDATE credit SET type_credit = ?, montant = ?, status = ?, echeancier = ?, document = ? WHERE id_compte = ?";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1,typeCredit);
            statement.setDouble(2, montant);
            statement.setString(3, status);
            statement.setString(4, echeancier);
            statement.setString(5, document);
            statement.setString(6, idCompte);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateCreditDetailsuser(String idCompte, String typeCredit,String status ,String montant, String echeancier, String document, Integer userId) {

        String query = "INSERT INTO demande (id , type, statut,  description, id_user,echeancier,document,date) VALUES (?, ?, ?, ?, ?,?,?,?) ;";
        try {
            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, idCompte);
            statement.setString(2, typeCredit);
            statement.setString(3,status);
            statement.setString(4, montant);
            statement.setString(5, String.valueOf(userId));
            statement.setString(6, echeancier);
            statement.setString(7, document);
            statement.setDate(8, java.sql.Date.valueOf(LocalDate.now()));
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateCreditDocument(String idCompte, String documentUrl) {
        String query = "UPDATE credit SET document = ? WHERE id_compte = ?";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, documentUrl);
            statement.setString(2, idCompte);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String uploadPdfToCloudinary(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    public List<Credit> getAllCredits() {
        List<Credit> credits = new ArrayList<>();
        String query = "SELECT * FROM credit";
        try (

            PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Credit credit = new Credit(
                        resultSet.getString("id"),
                        resultSet.getString("id_compte"),
                        resultSet.getString("type_credit"),
                        resultSet.getDouble("montant"),
                        resultSet.getString("status"),
                        resultSet.getString("echeancier"),
                        resultSet.getString("document")
                );
                credits.add(credit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return credits;
    }

    public Credit getCreditById(String idCompte) {
        String query = "SELECT * FROM credit WHERE id_compte = ?";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, idCompte);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Credit(
                            resultSet.getString("id"),
                            resultSet.getString("id_compte"),
                            resultSet.getString("type_credit"),
                            resultSet.getDouble("montant"),
                            resultSet.getString("status"),
                            resultSet.getString("echeancier"),
                            resultSet.getString("document")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Credit getCreditByAccountId(String idCompte) {
        String query = "SELECT * FROM credit WHERE id_compte = ?";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, idCompte);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Credit credit = new Credit();
                credit.setId(resultSet.getString("id"));
                credit.setIdCompte(resultSet.getString("id_compte"));
                credit.setTypeCredit(resultSet.getString("type_credit"));
                credit.setMontant(resultSet.getDouble("montant"));
                credit.setStatus(resultSet.getString("status"));
                credit.setEcheancier(resultSet.getString("echeancier"));
                credit.setDocument(resultSet.getString("document"));
                return credit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updateCreditStatus(String idCompte, String status) {
        String query = "UPDATE credit SET status = ? WHERE id_compte = ?";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);

            // Set the parameters for the prepared statement
            statement.setString(1, status);
            statement.setString(2, idCompte);

            // Execute the update
            int rowsUpdated = statement.executeUpdate();

            // Return true if the update was successful, otherwise false
            return rowsUpdated > 0;
        } catch (SQLException e) {
            // Print the stack trace for debugging
            e.printStackTrace();

            // Log a meaningful error message
            System.err.println("Error updating credit status for account ID: " + idCompte + " to status: " + status);
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }
    public String getAccountIdByUserId(String userId) {
        String query = "SELECT id FROM compte WHERE id_user = ?";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("id_user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean insertCredit(Credit credit) {
        String query = "INSERT INTO credit (id_compte,id_user, type_credit, montant, status, echeancier, document) VALUES (?,?, ?, ?, ?, ?, ?)";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, credit.getIdCompte());
            statement.setString(2, credit.getIdCompte());
            statement.setString(3, credit.getTypeCredit());
            statement.setDouble(4, credit.getMontant());
            statement.setString(5, credit.getStatus());
            statement.setString(6, credit.getEcheancier());
            statement.setString(7, credit.getDocument());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCreditRequest(String idCompte) {
        String query = "DELETE FROM demande WHERE id_compte = ?";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, idCompte);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Credit getCreditByUserId(String userId) {
        String query = "SELECT * FROM demande WHERE id_compte = (SELECT id FROM compte WHERE id_user = ?)";
        try {

            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Credit credit = new Credit();
                credit.setIdCompte(resultSet.getString("id_compte"));
                credit.setTypeCredit(resultSet.getString("type"));
                credit.setMontant(resultSet.getDouble("description"));
                credit.setStatus(resultSet.getString("statut"));
                credit.setDocument(resultSet.getString("document"));
                credit.setEcheancier(resultSet.getString("echeancier"));
                return credit;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

