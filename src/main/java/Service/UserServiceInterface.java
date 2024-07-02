package Service;

import entite.User;

import java.sql.SQLException;
import java.util.List;

public interface UserServiceInterface {
    void createUser(User user) throws SQLException;
    void updateUser(User user) throws SQLException;
    void deleteUser(int userId) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    void sendCredentials(User user);
}
