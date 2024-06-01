// File: service/ClientAuthService.java
package Service;

import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientAuthService {

    public boolean authenticate(String CIN, String password) {
        String sql = "SELECT CIN, mdp FROM user WHERE CIN = ? AND mdp = ? AND role = 'client'";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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
