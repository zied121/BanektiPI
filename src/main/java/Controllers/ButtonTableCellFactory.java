// File: src/main/java/Controllers/ButtonTableCellFactory.java
package Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ButtonTableCellFactory<S> implements Callback<TableColumn<S, String>, TableCell<S, String>> {

    private final String buttonText;
    private final String buttonStyleClass;

    public ButtonTableCellFactory(String buttonText, String buttonStyleClass) {
        this.buttonText = buttonText;
        this.buttonStyleClass = buttonStyleClass;
    }

    @Override
    public TableCell<S, String> call(final TableColumn<S, String> param) {
        return new TableCell<S, String>() {
            final Button btn = new Button(buttonText);

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.getStyleClass().add(buttonStyleClass);
                    setGraphic(btn);
                    setText(null);
                }
            }
        };
    }
}
