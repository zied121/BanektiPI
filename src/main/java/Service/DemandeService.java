package Service;

import entite.Demande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.DatabaseConnection;

import java.sql.*;

public class DemandeService {
    private Connection cnx;

    public ObservableList<Demande> getAllDemandes() {
        cnx = DatabaseConnection.getInstance().getConnection();
        ObservableList<Demande> demandes = FXCollections.observableArrayList();
        String query = "SELECT type, description, statut, date, id_user FROM demande";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                demandes.add(new Demande(
                        resultSet.getString("type"),
                        resultSet.getString("description"),
                        resultSet.getString("statut"),
                        resultSet.getString("date"),
                        resultSet.getInt("id_user")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return demandes;
    }
}
