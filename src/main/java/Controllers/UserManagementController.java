// File: src/main/java/Controllers/UserManagementController.java
package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entite.User;

public class UserManagementController {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> dateCreationColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> viewDetailsColumn;
    @FXML
    private TableColumn<User, String> editColumn;
    @FXML
    private TableColumn<User, String> deleteColumn;

    @FXML
    public void initialize() {
        viewDetailsColumn.setCellFactory(new ButtonTableCellFactory<>("View", "view-button"));
        editColumn.setCellFactory(new ButtonTableCellFactory<>("Edit", "edit-button"));
        deleteColumn.setCellFactory(new ButtonTableCellFactory<>("Delete", "delete-button"));
    }
}
