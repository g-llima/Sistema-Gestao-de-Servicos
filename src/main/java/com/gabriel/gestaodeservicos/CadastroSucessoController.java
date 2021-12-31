package com.gabriel.gestaodeservicos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CadastroSucessoController {

    @FXML
    private Button voltarBtn;

    public void closeWindow(ActionEvent e) {
        Stage stage = (Stage) voltarBtn.getScene().getWindow();
        stage.close();

    }
}
