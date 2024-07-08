// File: service/AdminAuthService.java
package Service;

import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminAuthService {

    public boolean authenticateAdmin(String adminCin, String adminPassword) {
        String sql = "SELECT CIN, mdp FROM user WHERE CIN = ? AND mdp = ? AND role = 'admin'";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, adminCin);
            statement.setString(2, adminPassword);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
