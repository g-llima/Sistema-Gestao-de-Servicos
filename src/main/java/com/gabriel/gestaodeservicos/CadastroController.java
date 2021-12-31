package com.gabriel.gestaodeservicos;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

public class CadastroController implements Initializable {

    ResultSet rs = null;
    PreparedStatement pst = null;
    Parent root;
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    //                  Cliente
    @FXML
    private Button minimizeBtn, submitBtn;
    @FXML
    private Label labelEs, OSClienteFail;
    @FXML
    private TextField telefoneField, nomeField, mailField, endField;
    @FXML
    private AnchorPane ClientePane, OSPane;
    @FXML
    private Pane activePane;

    //                  ORDEM DE SERVIÇO
    @FXML
    private TextField nomeFieldOS, defeitoFieldOS, EquipFieldOS, descFieldOS, valorFieldOS;
    @FXML
    private Button submitBtnOS, clienteBtn, OSBtn;

    //               FUNCIONÁRIO
    @FXML
    private Button FunBtn, submitBtnFunc;
    @FXML
    private TextField nomeFieldFunc, telefoneFieldFunc, loginFieldFunc;
    @FXML
    private PasswordField senhaFieldFunc;
    @FXML
    private CheckBox checkFieldFunc;
    @FXML
    private AnchorPane FuncPane;

    boolean isFunc = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        //          CLIENTE
        maxLength(telefoneField, 15);
        maxLength(nomeField, 50);
        maxLength(endField, 200);
        maxLength(mailField, 80);

        //      ORDEM DE SERVIÇO
        maxLength(nomeFieldOS, 50);
        maxLength(defeitoFieldOS, 150);
        maxLength(EquipFieldOS, 150);
        maxLength(descFieldOS, 150);
        maxLength(valorFieldOS, 10);

        //      FUNCIONARIO
        maxLength(nomeFieldFunc, 50);
        maxLength(telefoneFieldFunc, 15);
        maxLength(loginFieldFunc, 20);
        maxLength(senhaFieldFunc, 45);

        OSClienteFail.setVisible(false);

        ClientePane.setVisible(true);
        ClientePane.setDisable(false);

        OSPane.setVisible(false);
        OSPane.setDisable(true);

        FuncPane.setVisible(false);
        FuncPane.setDisable(true);

        enterCliente(telefoneField);
        enterCliente(nomeField);
        enterCliente(endField);
        enterCliente(mailField);

        enterOS(nomeFieldOS);
        enterOS(defeitoFieldOS);
        enterOS(EquipFieldOS);
        enterOS(descFieldOS);
        enterOS(valorFieldOS);

        OSBtn.setOnMouseClicked(event -> {
            if (labelEs.getText().equals("Administrador")) {
                if (ClientePane.isVisible() || FuncPane.isVisible()){
                    clienteBtn.setDisable(true);
                    FunBtn.setDisable(true);
                    changeToOS();
                    final Timeline animation = new Timeline(
                            new KeyFrame(Duration.millis(300),
                                    actionEvent -> {
                                        clienteBtn.setDisable(false);
                                        FunBtn.setDisable(false);
                                        isFunc = false;
                                    }));

                    animation.setCycleCount(1);
                    animation.play();
                }
            } else {
                if (ClientePane.isVisible()){
                    clienteBtn.setDisable(true);
                    changeToOS();
                    final Timeline animation = new Timeline(
                            new KeyFrame(Duration.millis(300),
                                    actionEvent -> {
                                        clienteBtn.setDisable(false);
                                    }));

                    animation.setCycleCount(1);
                    animation.play();
                }
            }

        });
        clienteBtn.setOnMouseClicked(event -> {
            if (labelEs.getText().equals("Administrador")) {
                if (OSPane.isVisible() || FuncPane.isVisible()){
                    OSBtn.setDisable(true);
                    FunBtn.setDisable(true);
                    changeToCliente();
                    final Timeline animation = new Timeline(
                            new KeyFrame(Duration.millis(300),
                                    actionEvent -> {
                                        OSBtn.setDisable(false);
                                        FunBtn.setDisable(false);
                                        isFunc = false;
                                    }));
                    animation.setCycleCount(1);
                    animation.play();
                }
            } else {
                if (OSPane.isVisible()){
                    OSBtn.setDisable(true);
                    changeToCliente();
                    final Timeline animation = new Timeline(
                            new KeyFrame(Duration.millis(300),
                                    actionEvent -> {
                                        OSBtn.setDisable(false);
                                    }));
                    animation.setCycleCount(1);
                    animation.play();
                }
            }

        });
        FunBtn.setOnMouseClicked(event -> {
            if (!isFunc){
                OSBtn.setDisable(true);
                clienteBtn.setDisable(true);
                changeToFunc();
                final Timeline animation = new Timeline(
                        new KeyFrame(Duration.millis(300),
                                actionEvent -> {
                                    OSBtn.setDisable(false);
                                    clienteBtn.setDisable(false);
                                    isFunc = true;
                                }));

                animation.setCycleCount(1);
                animation.play();
            }
        });

        //                  CLIENTE
        telefoneField.textProperty().addListener((observableValue, s, t1) -> {
            if (!telefoneField.getText().isEmpty() &&
                !nomeField.getText().isEmpty()) {
                submitBtn.setDisable(false);
            } else {
                submitBtn.setDisable(true);
            }
        });
        nomeField.textProperty().addListener((observableValue, s, t1) -> {
            if (!telefoneField.getText().isEmpty() &&
                    !nomeField.getText().isEmpty()) {
                submitBtn.setDisable(false);
            } else {
                submitBtn.setDisable(true);
            }
        });

        //                      APENAS NÚMEROS NO TELEFONE
        telefoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                telefoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        telefoneFieldFunc.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                telefoneFieldFunc.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //                      APENAS NÚMEROS NO VALOR
        valorFieldOS.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                valorFieldOS.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        //                  ORDEM DE SERVIÇO
        EquipFieldOS.textProperty().addListener((observableValue, s, t1) -> {
            if (!EquipFieldOS.getText().isEmpty() &&
                    !defeitoFieldOS.getText().isEmpty() &&
                !nomeFieldOS.getText().isEmpty()) {
                submitBtnOS.setDisable(false);
            } else {
                submitBtnOS.setDisable(true);
            }
        });
        defeitoFieldOS.textProperty().addListener((observableValue, s, t1) -> {
            if (!defeitoFieldOS.getText().isEmpty() &&
                    !EquipFieldOS.getText().isEmpty() &&
                    !nomeFieldOS.getText().isEmpty()) {
                submitBtnOS.setDisable(false);
            } else {
                submitBtnOS.setDisable(true);
            }
        });
        nomeFieldOS.textProperty().addListener((observableValue, s, t1) -> {
            if (!nomeFieldOS.getText().isEmpty() &&
                    !EquipFieldOS.getText().isEmpty() &&
                    !defeitoFieldOS.getText().isEmpty()) {
                submitBtnOS.setDisable(false);
            } else {
                submitBtnOS.setDisable(true);
            }
        });

        //              FUNCIONÁRIO
        nomeFieldFunc.textProperty().addListener((observableValue, s, t1) -> {
            if (!nomeFieldFunc.getText().isEmpty() &&
                    !telefoneFieldFunc.getText().isEmpty() &&
                    !loginFieldFunc.getText().isEmpty() &&
                    !senhaFieldFunc.getText().isEmpty()) {
                submitBtnFunc.setDisable(false);
            } else {
                submitBtnFunc.setDisable(true);
            }
        });
        telefoneFieldFunc.textProperty().addListener((observableValue, s, t1) -> {
            if (!telefoneFieldFunc.getText().isEmpty() &&
                    !nomeFieldFunc.getText().isEmpty() &&
                    !loginFieldFunc.getText().isEmpty() &&
                    !senhaFieldFunc.getText().isEmpty()) {
                submitBtnFunc.setDisable(false);
            } else {
                submitBtnFunc.setDisable(true);
            }
        });
        loginFieldFunc.textProperty().addListener((observableValue, s, t1) -> {
            if (!loginFieldFunc.getText().isEmpty() &&
                    !nomeFieldFunc.getText().isEmpty() &&
                    !telefoneFieldFunc.getText().isEmpty() &&
                    !senhaFieldFunc.getText().isEmpty()) {
                submitBtnFunc.setDisable(false);
            } else {
                submitBtnFunc.setDisable(true);
            }
        });
        senhaFieldFunc.textProperty().addListener((observableValue, s, t1) -> {
            if (!senhaFieldFunc.getText().isEmpty() &&
                    !nomeFieldFunc.getText().isEmpty() &&
                    !telefoneFieldFunc.getText().isEmpty() &&
                    !loginFieldFunc.getText().isEmpty()) {
                submitBtnFunc.setDisable(false);
            } else {
                submitBtnFunc.setDisable(true);
            }
        });

        //MINIMIZAR APP
        minimizeBtn.setOnMouseClicked( event -> {
            Stage obj = (Stage) minimizeBtn.getScene().getWindow();
            obj.setIconified(true);
        });
    }

    // Gambiarra para quando voltar aparecer o nome do usuário no "Olá, ..."
    public void getText(String username) {
        labelEs.setText(username);
    }

    // Setar o número de characteres máximo nos inputs
    public void maxLength(TextField tf, int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    // Detectar quando o usuário é administrador(libera o cadastro de funcionários)
    public void funcDisable(String username) {
        if (labelEs.getText().equals("Administrador")){
            FunBtn.setDisable(false);
            FunBtn.setCursor(Cursor.HAND);
        }
    }

    // Fecha o aplicativo clicando no "X"
    public void close(ActionEvent e) {
        Platform.exit();
    }

    // Enviar formulário de cadastro de ordem de serviço (Método para diminuir repetição de código)
    public void enterOS(TextField tf) {
        tf.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER) && !submitBtnOS.isDisabled()) {
                submitBtnOS.fire();
            }
        });
    }

    // Enviar formulário de cadastro de cliente (Método para diminuir repetição de código)
    public void enterCliente(TextField tf) {
        tf.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER) && !submitBtn.isDisabled()) {
                submitBtn.fire();
            }
        });
    }

    // Enviar as informações do cliente para o banco de dados
    public void submitCliente(ActionEvent e) {
        try {
            Connection conexao = null;

            String nome = nomeField.getText();
            String telefone = telefoneField.getText();
            String email = mailField.getText();
            String endereco = endField.getText();

            if (nome.equals("admin")) {
                nome = "'" + nome + "'";
            }

            conexao = ModuloConexao.conector();
            if (conexao != null) {
                String sql = "INSERT INTO tbclientes (ID_Cliente, Nome_Cliente, Endereco_Cliente, Telefone_Cliente, Email_Cliente)" +
                        " VALUES (DEFAULT, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = conexao.prepareStatement(sql);
                preparedStmt.setString (1, nome);
                preparedStmt.setString (2, endereco);
                preparedStmt.setString (3, telefone);
                preparedStmt.setString (4, email);
                preparedStmt.execute();
                conexao.close();

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CadastroClienteSucesso.fxml")));
                Scene scene = new Scene(root);
                Stage stage = new Stage();

                scene.setFill(Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);

                root.setOnMousePressed(mouseEvent -> {
                    xOffset = mouseEvent.getSceneX();
                    yOffset = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged(mouseEvent -> {
                    stage.setX(mouseEvent.getScreenX() - xOffset);
                    stage.setY(mouseEvent.getScreenY() - yOffset);
                });

                nomeField.setText("");
                telefoneField.setText("");
                mailField.setText("");
                endField.setText("");

                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
        }

        } catch (Exception ex) {
            submitBtn.setText("SEM CONEXÃO");
            submitBtn.setDisable(true);
            nomeField.setDisable(true);
            telefoneField.setDisable(true);
            mailField.setDisable(true);
            endField.setDisable(true);
            ex.printStackTrace();
        }


    }

    // Enviar as informações da ordem de serviço para o banco de dados
    public void submitOS(ActionEvent e) {
        try {
            Connection conexao = null;

            String nome = nomeFieldOS.getText();

            conexao = ModuloConexao.conector();
            if (conexao != null) {

                if(valorFieldOS.getText().isEmpty()) {
                    valorFieldOS.setText("0");
                }

                String sql = "SELECT * FROM dbinfox.tbclientes WHERE BINARY Nome_Cliente = '" + nome + "'";
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();

                if(rs.next()) {
                    int id_Cliente = Integer.parseInt(rs.getString("ID_Cliente"));

                    String sqlSubmit = "INSERT INTO tbos (OS, Data_OS, Equipamento, Defeito, Servico," +
                            " Valor, ID_Cliente) VALUES (DEFAULT, DEFAULT, ?, ?, ?, ?, ?)";

                    PreparedStatement preparedStmt = conexao.prepareStatement(sqlSubmit);
                    preparedStmt.setString (1, EquipFieldOS.getText());
                    preparedStmt.setString (2, defeitoFieldOS.getText());
                    preparedStmt.setString (3, descFieldOS.getText());
                    preparedStmt.setString (4, valorFieldOS.getText());
                    preparedStmt.setInt(5, id_Cliente);
                    preparedStmt.execute();

                    conexao.close();
                    Parent root;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CadastroOSSucesso.fxml"));
                    root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();

                    CadastroSucessoOSController csc = loader.getController();
                    csc.getInfo(nomeFieldOS.getText(), EquipFieldOS.getText(), defeitoFieldOS.getText(), descFieldOS.getText(), valorFieldOS.getText());

                    scene.setFill(Color.TRANSPARENT);
                    stage.initStyle(StageStyle.TRANSPARENT);

                    root.setOnMousePressed(mouseEvent -> {
                        xOffset = mouseEvent.getSceneX();
                        yOffset = mouseEvent.getSceneY();
                    });
                    root.setOnMouseDragged(mouseEvent -> {
                        stage.setX(mouseEvent.getScreenX() - xOffset);
                        stage.setY(mouseEvent.getScreenY() - yOffset);
                    });

                    nomeFieldOS.setText("");
                    defeitoFieldOS.setText("");
                    EquipFieldOS.setText("");
                    descFieldOS.setText("");
                    valorFieldOS.setText("");
                    OSClienteFail.setVisible(false);

                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    showClientError();
                }
            }

        } catch (Exception ex) {
            submitBtnOS.setText("SEM CONEXÃO");
            submitBtnOS.setDisable(true);
            nomeFieldOS.setDisable(true);
            defeitoFieldOS.setDisable(true);
            EquipFieldOS.setDisable(true);
            descFieldOS.setDisable(true);
            valorFieldOS.setDisable(true);
            ex.printStackTrace();
        }


    }

    // Enviar as informações da ordem de serviço para o banco de dados
    public void submitFunc(ActionEvent e) {
        try {
            Connection conexao = null;
            String userType;

            if(checkFieldFunc.isSelected()) {
                userType = "admin";
            } else {
                userType = "user";
            }

            String nome = nomeFieldFunc.getText();
            String telefone = telefoneFieldFunc.getText();
            String login = loginFieldFunc.getText();
            String senha = senhaFieldFunc.getText();

            if (nome.equals("admin")) {
                nome = "'" + nome + "'";
            }

            conexao = ModuloConexao.conector();
            if (conexao != null) {
                String sql = "INSERT INTO tbfuncionarios (ID, Funcionario, Telefone, Login, Senha, Perfil)" +
                        " VALUES (DEFAULT, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = conexao.prepareStatement(sql);
                preparedStmt.setString (1, nome);
                preparedStmt.setString (2, telefone);
                preparedStmt.setString (3, login);
                preparedStmt.setString (4, senha);
                preparedStmt.setString (5, userType);
                preparedStmt.execute();
                conexao.close();

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CadastroFuncSucesso.fxml")));
                Scene scene = new Scene(root);

                Stage stage = new Stage();

                scene.setFill(Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);

                root.setOnMousePressed(mouseEvent -> {
                    xOffset = mouseEvent.getSceneX();
                    yOffset = mouseEvent.getSceneY();
                });
                root.setOnMouseDragged(mouseEvent -> {
                    stage.setX(mouseEvent.getScreenX() - xOffset);
                    stage.setY(mouseEvent.getScreenY() - yOffset);
                });

                nomeFieldFunc.setText("");
                telefoneFieldFunc.setText("");
                loginFieldFunc.setText("");
                senhaFieldFunc.setText("");
                checkFieldFunc.setSelected(false);

                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception ex) {
            submitBtnFunc.setText("SEM CONEXÃO");
            submitBtnFunc.setDisable(true);
            nomeFieldFunc.setDisable(true);
            telefoneFieldFunc.setDisable(true);
            loginFieldFunc.setDisable(true);
            senhaFieldFunc.setDisable(true);
            checkFieldFunc.setDisable(true);
        }


    }

    // Alternar para a aba de cadastro de cliente
    public void changeToCliente() {
        if (FuncPane.isVisible()){
            TranslateTransition tt = new TranslateTransition();
            tt.setNode(activePane);
            tt.setByX(-508);
            tt.setDuration(Duration.millis(300));
            tt.play();
        } else if (OSPane.isVisible()){
            TranslateTransition tt = new TranslateTransition();
            tt.setNode(activePane);
            tt.setByX(-254);
            tt.setDuration(Duration.millis(300));
            tt.play();
        }
        if (!ClientePane.isVisible()) {

            ClientePane.setDisable(false);
            ClientePane.setVisible(true);

            OSClienteFail.setVisible(false);
            OSPane.setVisible(false);
            OSPane.setDisable(true);

            FuncPane.setDisable(true);
            FuncPane.setVisible(false);

            FunBtn.setStyle("-fx-text-fill: #666666; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
            OSBtn.setStyle("-fx-text-fill: #666666; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
            clienteBtn.setStyle("-fx-text-fill: #36cda0; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
        }

    }

    // Alternar para a aba de cadastro de ordem de serviço
    public void changeToOS() {
        if (FuncPane.isVisible()) {
            TranslateTransition tt = new TranslateTransition();
            tt.setNode(activePane);
            tt.setByX(-254);
            tt.setDuration(Duration.millis(300));
            tt.play();
        } else if (ClientePane.isVisible()){
            TranslateTransition tt = new TranslateTransition();
            tt.setNode(activePane);
            tt.setByX(254);
            tt.setDuration(Duration.millis(300));
            tt.play();
        }
        if (!OSPane.isVisible()) {

            ClientePane.setDisable(true);
            ClientePane.setVisible(false);

            OSClienteFail.setVisible(false);
            OSPane.setVisible(true);
            OSPane.setDisable(false);

            FuncPane.setDisable(true);
            FuncPane.setVisible(false);

            FunBtn.setStyle("-fx-text-fill: #666666; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
            OSBtn.setStyle("-fx-text-fill: #36cda0; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
            clienteBtn.setStyle("-fx-text-fill: #666666; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
        }
    }

    // Alternar para a aba de cadastro de funcionário
    public void changeToFunc() {
        if (ClientePane.isVisible()){
            TranslateTransition tt = new TranslateTransition();
            tt.setNode(activePane);
            tt.setByX(508);
            tt.setDuration(Duration.millis(300));
            tt.play();
        } else if (OSPane.isVisible()) {
            TranslateTransition tt = new TranslateTransition();
            tt.setNode(activePane);
            tt.setByX(254);
            tt.setDuration(Duration.millis(300));
            tt.play();
        }
        if (!FuncPane.isVisible()) {

            ClientePane.setDisable(true);
            ClientePane.setVisible(false);

            OSPane.setDisable(true);
            OSPane.setVisible(false);

            FuncPane.setDisable(false);
            FuncPane.setVisible(true);

            FunBtn.setStyle("-fx-text-fill: #36cda0; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
            clienteBtn.setStyle("-fx-text-fill: #666666; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");
            OSBtn.setStyle("-fx-text-fill: #666666; -fx-background-color: TRANSPARENT; -fx-border-color: #b0b5b3");

        }
    }

    // Mostrar mensagem de erro ao não encontrar o nome do cliente no banco de dados
    public void showClientError(){
        OSClienteFail.setVisible(true);
        FadeTransition ft = new FadeTransition();
        ft.setNode(OSClienteFail);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDuration(Duration.millis(200));
        ft.play();
    }

    // Voltar para a cena anterior ao clicar no "<"
    public void voltar(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("telafuncoes.fxml"));
        root = loader.load();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);

        TelaFuncoesController scene2Controller = loader.getController();
        scene2Controller.displayName(labelEs.getText()); //Insere o nome do usuário na proxima tela

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
}
