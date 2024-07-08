// File: service/ClientAuthService.java
package Service;

import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DatabaseConnection;


public class ClientAuthService {
    private static Connection cnx;
    public boolean authenticate(String CIN, String password) {
        cnx = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT CIN, mdp FROM user WHERE CIN = ? AND mdp = ? AND role = 'client'";

        try {

            PreparedStatement statement = cnx.prepareStatement(query);

            statement.setString(1, CIN);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
