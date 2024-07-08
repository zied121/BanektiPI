package Service;

import entite.Demande;
import entite.Document;
import util.DatabaseUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static util.DatabaseUtil.getConnection;

public class Documentservice implements IserviceDocument<Document> {

    private Connection connection;

    public Documentservice() {
        try {
            connection = getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Document document) {
        String query = "INSERT INTO document_1(reponse, date, id_dem) VALUES (?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, document.getReponse());
            pst.setDate(2, Date.valueOf(document.getDate()));
            pst.setInt(3, document.getId_dem());


            System.out.println("Requête exécutée : " + pst.toString());

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Document document ) {
        String query = "DELETE FROM document_1 WHERE id = ? ";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, document.getId());


            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Document document) {
        String query = "UPDATE document_1 SET reponse = ?, date = ?, id_dem = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, document.getReponse());
            pst.setDate(2, Date.valueOf(document.getDate()));
            pst.setInt(3, document.getId_dem());
            pst.setInt(4, document.getId());


            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Document> readAll( ) {
        List<Document> documents = new ArrayList<>();
        String query = "SELECT * FROM document_1 WHERE id_user = ?";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Document document = new Document();
                document.setId(rs.getInt("id"));
                document.setReponse(rs.getString("reponse"));
                document.setDate(rs.getDate("date").toLocalDate());
                document.setId_dem(rs.getInt("id_dem"));


                documents.add(document);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return documents;
    }

    @Override
    public Document readById(int id) {
        String query = "SELECT * FROM document_1 WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Document document = new Document();
                document.setId(rs.getInt("id"));
                document.setReponse(rs.getString("reponse"));
                document.setDate(rs.getDate("date").toLocalDate());
                document.setId_dem(rs.getInt("id_dem"));

                return document;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Document> readAllWithDemandeDetails() {
        List<Document> documents = new ArrayList<>();

        String query = "SELECT dd.id AS demande_id, dd.type, dd.statut, dd.description, dd.id_user, dd.date AS demande_date, " +
                "d.id AS document_id, d.reponse, d.date AS document_date, d.id_dem, " +
                "c.rib, u.nom, u.prenom, u.nb_compte, u.email, " + // Jointure avec la table user
                "c.solde, c.statut AS statut_compte, o.type_op, o.montant " +
                "FROM demande dd " +
                "LEFT JOIN document_1 d ON dd.id = d.id_dem " +
                "LEFT JOIN compte c ON dd.id_user = c.id " +
                "LEFT JOIN user u ON dd.id_user = u.id " + // Jointure avec la table user
                "LEFT JOIN operation o ON o.id_compte = c.id"; // Jointure avec la table operation

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Fields from the demande table
                int demandeId = resultSet.getInt("demande_id");
                String type = resultSet.getString("type");
                String statut = resultSet.getString("statut");
                String description = resultSet.getString("description");
                int id_user = resultSet.getInt("id_user");
                LocalDate demandeDate = resultSet.getDate("demande_date").toLocalDate();
                String rib = resultSet.getString("rib"); // Récupérer le RIB
                String nomUtilisateur = resultSet.getString("nom"); // Récupérer le nom de l'utilisateur
                String prenomUtilisateur = resultSet.getString("prenom"); // Récupérer le prénom de l'utilisateur
                int nb_compte = resultSet.getInt("nb_compte");
                double solde = resultSet.getDouble("solde");
                String statut_compte = resultSet.getString("statut_compte");
                String type_op = resultSet.getString("type_op");
                double montant = resultSet.getDouble("montant");
                String email = resultSet.getString("email");

                // Create the Demande object
                Demande demande = new Demande();
                demande.setId(demandeId);
                demande.setType(type);
                demande.setStatut(statut);
                demande.setDescription(description);
                demande.setId_user(id_user);
                demande.setDate(demandeDate);
                demande.setRib(rib);
                demande.setNom(nomUtilisateur);
                demande.setPrenom(prenomUtilisateur);
                demande.setNb_compte(nb_compte);
                demande.setSolde(solde);
                demande.setStatut_compte(statut_compte);
                demande.setType_op(type_op);
                demande.setMontant(montant);
                demande.setEmail(email);

                // Fields from the document table (which can be null)
                int documentId = resultSet.getInt("document_id");
                String reponse = resultSet.getString("reponse");
                java.sql.Date sqlDocumentDate = resultSet.getDate("document_date");
                LocalDate documentDate = (sqlDocumentDate != null) ? sqlDocumentDate.toLocalDate() : null;
                int id_dem = resultSet.getInt("id_dem");

                // Create the Document object with Demande associated
                Document document = new Document(documentId, reponse, documentDate, id_dem, demande);
                documents.add(document);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return documents;
    }

    public Map<String, Integer> fetchDataFromDatabase() {
        Map<String, Integer> data = new HashMap<>();
        String query = "SELECT statut, COUNT(*) as count FROM demande GROUP BY statut";

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


    public void sendEmail(String to, String subject, String body) {
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
            // Crée un objet MimeMessage pour contenir le message e-mail

            MimeMessage message = new MimeMessage(session);
            // Définit l'adresse e-mail de l'expéditeur

            message.setFrom(new InternetAddress(from));
            // Ajoute le destinataire à l'e-mail

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Définit le sujet de l'e-mail

            message.setSubject(subject);
            // Définit le corps de l'e-mail

            message.setText(body);

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }
}
