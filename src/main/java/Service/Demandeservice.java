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
        public void insert(Demande demande , int id_user) {
            String query = "INSERT INTO demande(type, statut, description, id_user, date) VALUES (?, ?, ?, ?, ?)";
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
        public void delete(Demande demande ,int id_user){


//
            String query = "DELETE FROM demande WHERE id = ?  AND id_user = ?";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setInt(1, demande.getId());
                pst.setInt(2, id_user);


                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void update(Demande demande ,int id_user) {
            String query = "UPDATE demande SET type = ?, statut = ?, description = ?, date = ? " +
                    "WHERE id = ? and id_user = ?";
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, demande.getType());
                pst.setString(2, demande.getStatut());
                pst.setString(3, demande.getDescription());
                pst.setDate(4, Date.valueOf(demande.getDate()));
                pst.setInt(5, demande.getId());
                pst.setInt(6, demande.getId_user());

                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }




            @Override
            public List<Demande> readAll(int id_user) {
                List<Demande> demandes = new ArrayList<>();
                String query = "SELECT DISTINCT d.*, doc.reponse " +
                        "FROM demande d " +
                        "LEFT JOIN document_1 doc ON d.id = doc.id_dem " +
                        "WHERE d.id_user = ?";

                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, id_user);

                    try (ResultSet rs = stmt.executeQuery()) {
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
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error fetching demands for user with id " + id_user, e);
                }
                System.out.println(demandes);
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
