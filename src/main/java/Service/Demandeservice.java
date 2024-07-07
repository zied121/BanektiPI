    package Service;

    import entite.Demande;
    import util.DatabaseUtil;
    import util.MyConnection;

    import java.sql.*;
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    public class Demandeservice implements Iservice<Demande> {

        private Connection connection;

        public Demandeservice() {
            // Accéder à la source de données
            try {
                connection = DatabaseUtil.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void insert(Demande demande) {
            String query = "INSERT INTO demande(type, statut, description, id_user, date) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, demande.getType());
                pst.setString(2, demande.getStatut());
                pst.setString(3, demande.getDescription());
                pst.setInt(4, demande.getId_user());
                pst.setDate(5, Date.valueOf(demande.getDate()));

                pst.executeUpdate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            }



        @Override
        public void delete(Demande demande){


            try {
                String requete="DELETE FROM demande WHERE id =" +demande.getId(); ;

                Statement st= MyConnection.getInstance().getCnx().createStatement();
                st.executeUpdate(requete);
                System.out.println("Demande supprimer!");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        @Override
        public void update(Demande demande) {
            String query = "UPDATE demande SET type = ?, statut = ?, description = ?, id_user = ?, date = ? " +
                    "WHERE id = ?";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, demande.getType());
                pst.setString(2, demande.getStatut());
                pst.setString(3, demande.getDescription());
                pst.setInt(4, demande.getId_user());
                pst.setDate(5, Date.valueOf(demande.getDate()));
                pst.setInt(6, demande.getId());

                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }




        @Override
        public List<Demande> readAll() {
            List<Demande> demandes = new ArrayList<>();
            String query = "SELECT d.*, doc.reponse FROM demande d LEFT JOIN document_1 doc ON d.id = doc.id_dem";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    Demande demande = new Demande();
                    demande.setId(rs.getInt("id"));
                    demande.setType(rs.getString("type"));
                    demande.setStatut(rs.getString("statut"));
                    demande.setDescription(rs.getString("description"));
                    demande.setId_user(rs.getInt("id_user"));
                    demande.setDate(rs.getDate("date").toLocalDate());
                    demande.setReponse(rs.getString("reponse"));

                    demandes.add(demande);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return demandes;
        }


        @Override
        public Demande readById(int id) {
            String query = "SELECT d.*, u.email FROM demande d JOIN user u ON d.id_user = u.id WHERE d.id = ?";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    Demande demande = new Demande();
                    demande.setId(rs.getInt("id"));
                    demande.setType(rs.getString("type"));
                    demande.setStatut(rs.getString("statut"));
                    demande.setDescription(rs.getString("description"));
                    demande.setId_user(rs.getInt("id_user"));
                    demande.setDate(rs.getDate("date").toLocalDate());
                    demande.setEmail(rs.getString("email")); // Add this line

                    return demande;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }



    }
