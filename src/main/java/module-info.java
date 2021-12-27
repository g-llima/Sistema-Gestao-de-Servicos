module com.gabriel.gestaodeservicos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.gabriel.gestaodeservicos to javafx.fxml;
    exports com.gabriel.gestaodeservicos;
}