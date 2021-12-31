module com.gabriel.gestaodeservicos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;


    opens com.gabriel.gestaodeservicos to javafx.fxml;
    exports com.gabriel.gestaodeservicos;
}