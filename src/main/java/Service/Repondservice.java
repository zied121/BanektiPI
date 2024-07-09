package Service;

import entite.Reclamation;
import entite.Repond;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static util.DatabaseUtil.getConnection;

public class Repondservice implements Iservice<Repond> {

    private Connection connection;

    public Repondservice() {
        try {
            connection = getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Repond Repond) {
        String query = "INSERT INTO repond(id, reponse, date, id_rec) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, Repond.getId());
            pst.setString(2, Repond.getReponse());
            pst.setDate(3, Date.valueOf(Repond.getDate()));
            pst.setInt(4, Repond.getId_rec());

            System.out.println("Requête exécutée : " + pst.toString());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Repond Repond) {
        String query = "DELETE FROM repond WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, Repond.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Repond Repond) {
        String query = "UPDATE repond SET reponse = ?, date = ?, id_rec = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, Repond.getReponse());
            pst.setDate(2, Date.valueOf(Repond.getDate()));
            pst.setInt(3, Repond.getId_rec());
            pst.setInt(4, Repond.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Repond entity, int id_user) {

    }

    @Override
    public void delete(Repond entity, int id_user) {

    }

    @Override
    public void update(Repond entity, int id_user) {

    }

    @Override
    public void insert(Reclamation Reclamation) {

    }

    @Override
    public void delete(Reclamation Reclamation) {

    }

    @Override
    public void update(Reclamation Reclamation) {

    }

    @Override
    public List<Repond> readAll(int id_user){
        return null ;
    }

    public List<Repond> readAll() {
        List<Repond> Reponds = new ArrayList<>();
        String query = "SELECT * FROM repond";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Repond Repond = new Repond();
                Repond.setId(rs.getInt("id"));
                Repond.setReponse(rs.getString("reponse"));
                Repond.setDate(rs.getDate("date").toLocalDate());
                Repond.setId_rec(rs.getInt("id_rec"));

                Reponds.add(Repond);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Reponds;
    }

    @Override
    public Repond readById(int id) {
        String query = "SELECT * FROM repond WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Repond Repond = new Repond();
                Repond.setId(rs.getInt("id"));
                Repond.setReponse(rs.getString("reponse"));
                Repond.setDate(rs.getDate("date").toLocalDate());
                Repond.setId_rec(rs.getInt("id_rec"));

                return Repond;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Repond> readAllWithReclamationDetails() {
        List<Repond> Reponds = new ArrayList<>();

        String query = "SELECT dd.id, dd.type, dd.statut, dd.description, dd.id_user, dd.date, " +
                "d.id AS Repond_id, d.reponse, d.date AS Repond_date, d.id_rec, " +
                "u.nom, u.prenom, u.nb_compte, u.email " + // Jointure avec la table user
                "FROM reclamation dd " +
                "LEFT JOIN repond d ON dd.id = d.id_rec " +
                "LEFT JOIN user u ON dd.id_user = u.id "; // Jointure avec la table user

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Fields from the Reclamation table
                int ReclamationId = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String statut = resultSet.getString("statut");
                String description = resultSet.getString("description");
                int id_user = resultSet.getInt("id_user");
                LocalDate ReclamationDate = resultSet.getDate("date").toLocalDate();
                String nomUtilisateur = resultSet.getString("nom"); // Récupérer le nom de l'utilisateur
                String prenomUtilisateur = resultSet.getString("prenom"); // Récupérer le prénom de l'utilisateur
                int nb_compte = resultSet.getInt("nb_compte");
                String email = resultSet.getString("email");

                // Create the Reclamation object
                Reclamation Reclamation = new Reclamation();
                Reclamation.setId(ReclamationId);
                Reclamation.setType(type);
                Reclamation.setStatut(statut);
                Reclamation.setDescription(description);
                Reclamation.setId_user(id_user);
                Reclamation.setDate(ReclamationDate);
                Reclamation.setNom(nomUtilisateur);
                Reclamation.setPrenom(prenomUtilisateur);
                Reclamation.setNb_compte(nb_compte);
                Reclamation.setEmail(email);

                // Fields from the Repond table (which can be null)
                int RepondId = resultSet.getInt("Repond_id");
                String reponse = resultSet.getString("reponse");
                java.sql.Date sqlRepondDate = resultSet.getDate("Repond_date");
                LocalDate RepondDate = (sqlRepondDate != null) ? sqlRepondDate.toLocalDate() : null;
                int id_rec = resultSet.getInt("id_rec");

                // Create the Repond object with Reclamation associated
                Repond Repond = new Repond(RepondId, reponse, RepondDate, id_rec, Reclamation);
                Reponds.add(Repond);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return Reponds;
    }

    public void sendEmail(String to, String subject, String body) {
        String from = "zied.s@convegen.io";
        String host = "smtp.gmail.com";


        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
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
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    public Map<String, Integer> fetchDataFromDatabase() {
        Map<String, Integer> data = new HashMap<>();
        String query = "SELECT statut , count(*) as count FROM reclamation group by statut ";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String statut = resultSet.getString("statut");
                int count = resultSet.getInt("count");
                data.put(statut, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

}