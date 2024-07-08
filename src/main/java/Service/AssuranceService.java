package Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import entite.Assurance;
import util.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.DatabaseConnection;

public class AssuranceService {
    private Cloudinary cloudinary;

    private Connection cnx;


    public AssuranceService() {
        cnx = DatabaseConnection.getInstance().getConnection();
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dsyszjsc2",
                "api_key", "967221561574594",
                "api_secret", "IosNjEIg0uEroIu7yXlS08os2bg"
        ));
    }

    public boolean addAssurance(Assurance assurance) {
        String query = "INSERT INTO assurance (id_user, type, date_debut, date_fin, document, image,status) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, assurance.getIdUser());
            statement.setString(2, assurance.getType());
            statement.setString(3, assurance.getDateDebut());
            statement.setString(4, assurance.getDateFin());
            statement.setString(5, assurance.getDocument());
            statement.setString(6, assurance.getImage());
            statement.setString(7, assurance.getStatus());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Assurance> getAllAssurances() {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {;
            ResultSet resultSet = statement.executeQuery() ;
            while (resultSet.next()) {
                Assurance assurance = new Assurance();
                assurance.setId(resultSet.getString("id"));
                assurance.setIdUser(resultSet.getString("id_user"));
                assurance.setType(resultSet.getString("type"));
                assurance.setDateDebut(resultSet.getString("date_debut"));
                assurance.setDateFin(resultSet.getString("date_fin"));
                assurance.setDocument(resultSet.getString("document"));
                assurance.setImage(resultSet.getString("image"));
                assurance.setStatus(resultSet.getString("status"));
                assurances.add(assurance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assurances;
    }

    public String uploadFileToCloudinary(File file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }
    public boolean updateAssuranceStatus(Assurance assurance) {
        String query = "UPDATE assurance SET status = ? WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, assurance.getStatus());
            statement.setInt(2, assurance.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAssurance(int id) {
        String query = "DELETE FROM assurance WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
