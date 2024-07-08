    package Service;
    import entite.User;
    import util.DatabaseUtil;
    import util.UserSession;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    public class    ClientAuthService {

        public User authenticate(String ClientCIN, String ClientPassword) {
            String sql = "SELECT * FROM user WHERE CIN = ? AND mdp = ? AND role = 'client'";

            try (Connection connection = DatabaseUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, ClientCIN);
                statement.setString(2, ClientPassword);


                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        User user = new User(); 
                        user.setId(resultSet.getInt("id"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setAge(resultSet.getInt("age"));
                        user.setMdp(resultSet.getString("mdp"));
                        user.setCin(resultSet.getInt("cin"));
                        user.setRole(resultSet.getString("role"));
                        user.setNbCompte(resultSet.getInt("nb_compte"));
                        user.setEmail(resultSet.getString("email"));
                        return user;
                    }
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
