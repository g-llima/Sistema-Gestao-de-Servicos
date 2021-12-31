package com.gabriel.gestaodeservicos;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class TelaLogin implements Initializable {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    @FXML
    private TextField userTextField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button myBtn, minimizeBtn;
    @FXML
    private Circle c1, c2, c3;
    @FXML
    private Label loginFail;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //MINIMIZAR APP
        minimizeBtn.setOnMouseClicked( event -> {
            Stage obj = (Stage) minimizeBtn.getScene().getWindow();
            obj.setIconified(true);
        });


        //LOGAR AO CLICAR NO "ENTER"
        userTextField.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)) {
                myBtn.fire();
            }
        });
        passField.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER)) {
                myBtn.fire();
            }
        });

        conexao = ModuloConexao.conector();
        if (conexao != null) {

            Tooltip.install(c1, new Tooltip("Conectado!"));
            Tooltip.install(c2, new Tooltip("Conectado!"));
            Tooltip.install(c3, new Tooltip("Conectado!"));

            TranslateTransition translate = new TranslateTransition();
            translate.setNode(c1);
            translate.setDuration(Duration.millis(3000));
            translate.setCycleCount(TranslateTransition.INDEFINITE);
            translate.setByY(5);
            translate.setAutoReverse(true);
            translate.play();

            TranslateTransition translate3 = new TranslateTransition();
            translate3.setNode(c3);
            translate3.setDuration(Duration.millis(3000));
            translate3.setCycleCount(TranslateTransition.INDEFINITE);
            translate3.setByY(-5);
            translate3.setAutoReverse(true);
            translate3.play();

        } else {

            Tooltip.install(c1, new Tooltip("Sem conexão"));
            Tooltip.install(c2, new Tooltip("Sem conexão"));
            Tooltip.install(c3, new Tooltip("Sem conexão"));

            c1.setFill(Color.CRIMSON);
            c2.setFill(Color.CRIMSON);
            c3.setFill(Color.CRIMSON);
            userTextField.setDisable(true);
            passField.setDisable(true);
            myBtn.setDisable(true);
        }
    }
    private Stage stage;
    private Scene scene;

    double xOffset = 0;
    double yOffset = 0;

    public void logar(ActionEvent e) {

        try {
            String user = userTextField.getText();
            String pass = passField.getText();

            //BUG FIX CASO INPUT = "admin"
            user = "'" + user + "'";
            pass = "'" + pass + "'";


            String sql = "SELECT * FROM dbinfox.tbfuncionarios WHERE BINARY Login = " + user + " AND Senha = " + pass;
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                String username = rs.getString(2);
                String aUsername = username;
                if (username.equals("admin")) {
                    username = "Administrador";
                }

                loginFail.setOpacity(0);
                Parent root;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("telafuncoes.fxml"));
                root = loader.load();

                TelaFuncoesController scene2Controller = loader.getController();
                scene2Controller.displayName(username); //Insere o nome do usuário na proxima tela

                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);

                root.setOnMousePressed(mouseEvent -> { //Arrastar o aplicativo com o mouse
                    xOffset = mouseEvent.getSceneX();
                    yOffset = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged(mouseEvent -> {
                    stage.setX(mouseEvent.getScreenX() - xOffset);
                    stage.setY(mouseEvent.getScreenY() - yOffset);
                });


                stage.setScene(scene);
                stage.show();
                conexao.close();
            } else {
                FadeTransition fade = new FadeTransition();
                fade.setNode(loginFail);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setDuration(Duration.millis(200));
                fade.play();

                userTextField.requestFocus();
                userTextField.setText("");
                passField.setText("");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void close(ActionEvent e) {
        Platform.exit();
    }
}
