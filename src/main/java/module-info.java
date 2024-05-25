module com.example.projetpi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires  javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.example.projetpi to javafx.fxml;
    exports com.example.projetpi;
    exports entite;
    opens entite to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    exports service;
    opens service to javafx.fxml;
}