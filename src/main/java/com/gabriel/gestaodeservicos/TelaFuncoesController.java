package com.gabriel.gestaodeservicos;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.constant.Constable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

public class TelaFuncoesController implements Initializable {

    @FXML
    private Label welcomeLabel, dateLabel, labelEs;
    @FXML
    private Button cadastroBtn, relatBtn, optBtn, minimizeBtn;

    public void displayName(String username) {
        welcomeLabel.setText("Olá, " + username);
        labelEs.setText(username);
    }


    private Stage stage;
    private Scene scene;

    private double xOffset = 0;
    private double yOffset = 0;
    Parent root;

    public void irParaCadastro(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Cadastro.fxml"));
        root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        CadastroController cc = loader.getController();
        cc.getText(labelEs.getText());
        cc.funcDisable(labelEs.getText());


        String css = Objects.requireNonNull(this.getClass().getResource("cadastroCSS.css")).toExternalForm();
        scene.getStylesheets().add(css);
        scene.setFill(Color.TRANSPARENT);

        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage.setScene(scene);
        stage.show();
    }

    public void irParaRelatorio(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Relatorio.fxml"));
        root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);

        RelatorioController rc = loader.getController();
        rc.getText(labelEs.getText());

        scene.setFill(Color.TRANSPARENT);

        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
                event -> {
                    Calendar cal = Calendar.getInstance();
                    dateLabel.setText(format.format(cal.getTime()));
                }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        //MINIMIZAR APP
        minimizeBtn.setOnMouseClicked( event -> {
            Stage obj = (Stage) minimizeBtn.getScene().getWindow();
            obj.setIconified(true);
        });

        //ANIMAÇÃO HOVER BOTÃO CADASTRAR
        RotateTransition rt = new RotateTransition(Duration.seconds(0.3), cadastroBtn);
        RotateTransition rtE = new RotateTransition(Duration.seconds(0.3), cadastroBtn);
        rt.setFromAngle(0);
        rt.setToAngle(10);

        cadastroBtn.setOnMouseEntered(e -> rt.play());
        cadastroBtn.setOnMouseExited(e -> {
            rtE.setFromAngle(10);
            rtE.setToAngle(0);
            rtE.play();
        });

        //ANIMAÇÃO HOVER BOTÃO RELATÓRIO
        RotateTransition rtR = new RotateTransition(Duration.seconds(0.3), relatBtn);
        RotateTransition rtRE = new RotateTransition(Duration.seconds(0.3), relatBtn);
        rtR.setFromAngle(0);
        rtR.setToAngle(-12);

        relatBtn.setOnMouseEntered(e -> rtR.play());
        relatBtn.setOnMouseExited(e -> {
            rtRE.setFromAngle(-12);
            rtRE.setToAngle(0);
            rtRE.play();
        });

        //ANIMAÇÃO HOVER BOTÃO CONFIGURAR
        RotateTransition rtC = new RotateTransition(Duration.seconds(0.3), optBtn);
        rtC.setFromAngle(0);
        rtC.setToAngle(90);

        optBtn.setOnMouseEntered(e -> rtC.play());
    }
    public void close(ActionEvent e) {
        Platform.exit();
    }
}
