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
    requires java.desktop;
    requires java.mail;
    requires cloudinary.core;
    requires itextpdf;

    opens Main to javafx.fxml;
    exports Main;
    exports entite;
    opens entite to javafx.fxml;
    exports util;
    opens util to javafx.fxml;
    exports Controllers;
    opens Controllers to javafx.fxml;
}