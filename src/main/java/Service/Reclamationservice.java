package Service;

import entite.Demande;
import entite.Reclamation;
import entite.Repond;
import util.DatabaseUtil;
import util.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reclamationservice implements Iservice<Reclamation> {

    private Connection connection;
    //User user = UserSession.getInstance().getUser();

    public Reclamationservice() {
        // Accéder à la source de données
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Repond Repond) {

    }

    @Override
    public void update(Repond Repond) {

    }

    @Override
    public void insert(Reclamation entity, int id_user) {

    }

    @Override
    public void delete(Reclamation entity, int id_user) {

    }

    @Override
    public void update(Reclamation entity, int id_user) {

    }

    @Override
    public void insert(Reclamation Reclamation) {
        String query = "INSERT INTO reclamation(type, statut, description, id_user, date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, Reclamation.getType());
            pst.setString(2, Reclamation.getStatut());
            pst.setString(3, Reclamation.getDescription());
             pst.setInt(4, Reclamation.getId_user());
            //pst.setInt(4, User.getId();
            pst.setDate(5, Date.valueOf(Reclamation.getDate()));

            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void delete(Reclamation Reclamation){


        try {
            String requete="DELETE FROM reclamation WHERE id =" +Reclamation.getId(); ;

            Statement st= MyConnection.getInstance().getCnx().createStatement();
            st.executeUpdate(requete);
            System.out.println("Reclamation supprimer!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void update(Reclamation Reclamation) {
        String query = "UPDATE reclamation SET type = ?, statut = ?, description = ?, id_user = ?, date = ? " +
                "WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, Reclamation.getType());
            pst.setString(2, Reclamation.getStatut());
            pst.setString(3, Reclamation.getDescription());
            pst.setInt(4, Reclamation.getId_user());
            pst.setDate(5, Date.valueOf(Reclamation.getDate()));
            pst.setInt(6, Reclamation.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

@Override
    public List<Reclamation> readAll() {
        List<Reclamation> Reclamations = new ArrayList<>();
        String query = "SELECT d.*, doc.reponse FROM reclamation d LEFT JOIN repond doc ON d.id = doc.id_rec";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Reclamation Reclamation = new Reclamation();
                Reclamation.setId(rs.getInt("id"));
                Reclamation.setType(rs.getString("type"));
                Reclamation.setStatut(rs.getString("statut"));
                Reclamation.setDescription(rs.getString("description"));
                Reclamation.setId_user(rs.getInt("id_user"));
                Reclamation.setDate(rs.getDate("date").toLocalDate());
                Reclamation.setReponse(rs.getString("reponse"));

                Reclamations.add(Reclamation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Reclamations;
    }


    @Override
    public List<Reclamation> readAll(int id_user) { //id user
        List<Reclamation> Reclamations = new ArrayList<>();
        String query = "SELECT d.*, doc.reponse FROM reclamation d LEFT JOIN repond doc ON d.id = doc.id_rec where d.id_user=?";
//        try (Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            stmt.setInt(1, id_user);
//            while (rs.next()) {
//                Reclamation Reclamation = new Reclamation();
//                Reclamation.setId(rs.getInt("id"));
//                Reclamation.setType(rs.getString("type"));
//                Reclamation.setStatut(rs.getString("statut"));
//                Reclamation.setDescription(rs.getString("description"));
//                Reclamation.setId_user(rs.getInt("id_user"));
//                Reclamation.setDate(rs.getDate("date").toLocalDate());
//                Reclamation.setReponse(rs.getString("reponse"));
//
//                Reclamations.add(Reclamation);
//            }
//        }
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id_user);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reclamation Reclamation = new Reclamation();
                Reclamation.setId(rs.getInt("id"));
                Reclamation.setType(rs.getString("type"));
                Reclamation.setStatut(rs.getString("statut"));
                Reclamation.setDescription(rs.getString("description"));
                Reclamation.setId_user(rs.getInt("id_user"));
                Reclamation.setDate(rs.getDate("date").toLocalDate());
                Reclamation.setReponse(rs.getString("reponse"));

                Reclamations.add(Reclamation);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Reclamations;
    }


    @Override
    public Reclamation readById(int id) {
        String query = "SELECT d.*, u.email FROM reclamation d JOIN user u ON d.id_user = u.id WHERE d.id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Reclamation Reclamation = new Reclamation();
                Reclamation.setId(rs.getInt("id"));
                Reclamation.setType(rs.getString("type"));
                Reclamation.setStatut(rs.getString("statut"));
                Reclamation.setDescription(rs.getString("description"));
                Reclamation.setId_user(rs.getInt("id_user"));
                Reclamation.setDate(rs.getDate("date").toLocalDate());
                Reclamation.setEmail(rs.getString("email")); // Add this line

                return Reclamation;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}
