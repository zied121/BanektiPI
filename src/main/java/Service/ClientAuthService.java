    package Service;
    import entite.User;
    import util.DatabaseUtil;
    import util.UserSession;

    import javax.mail.*;
    import javax.mail.internet.InternetAddress;
    import javax.mail.internet.MimeMessage;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.Properties;

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
        public void sendPasswordByEmail(String cin) throws SQLException, MessagingException {
            String sql = "SELECT email, mdp FROM user WHERE CIN = ? AND role = 'client'";

            try (Connection connection = DatabaseUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, cin);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("mdp");

                        // Email configuration
                        String from = "zied.s@convegen.io";
                        String host = "smtp.gmail.com";

                        Properties properties = System.getProperties();
                        properties.setProperty("mail.smtp.host", host);
                        properties.setProperty("mail.smtp.port", "587"); // replace with your SMTP port
                        properties.setProperty("mail.smtp.auth", "true");
                        properties.setProperty("mail.smtp.starttls.enable", "true");

                        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication("zied.s@convergen.io", "yxdnryxspyycpvjd");
                            }
                        });

                        MimeMessage message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(from));
                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                        message.setSubject("Your Password");
                        message.setText("Your password is: " + password);

                        Transport.send(message);
                    } else {
                        throw new SQLException("User with provided CIN does not exist.");
                    }
                }
            }
        }

    }
