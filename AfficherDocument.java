package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AfficherDocument {

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txttype;

    @FXML
    private TextField txturl;

    @FXML
    private TextField txtlist;

    public void setTxtnom(String nom) {
        this.txtnom.setText(nom);
    }

    public void setTxttype(String type) {
        this.txttype.setText(type);
    }

    public void setTxturl(String url) {
        this.txturl.setText(url);
    }

    public void setTxtlist(String list) {
        this.txtlist.setText(list);
    }
}
