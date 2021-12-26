package com.gabriel.gestaodeservicos;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class Controller {

    public void close(ActionEvent e) {
        Platform.exit();
    }

}
