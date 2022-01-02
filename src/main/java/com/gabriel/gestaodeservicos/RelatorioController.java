package com.gabriel.gestaodeservicos;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class RelatorioController implements Initializable {

    Parent root;
    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private TableView<ModelTable> os_table;
    @FXML
    private TableColumn<ModelTable, String> os_col_num;
    @FXML
    private TableColumn<ModelTable, String> os_col_data;
    @FXML
    private TableColumn<ModelTable, String> os_col_equip;
    @FXML
    private TableColumn<ModelTable, String> os_col_def;
    @FXML
    private TableColumn<ModelTable, String> os_col_serv;
    @FXML
    private TableColumn<ModelTable, String> os_col_valor;
    @FXML
    private TableColumn<ModelTable, String> os_col_id_cliente;

    @FXML
    private Button minimizeBtn, OSDelBtn;
    @FXML
    private Label labelEs;
    @FXML
    private TextField filterField;

    String selectedOS = null;

    Connection conexao = ModuloConexao.conector();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        os_table.setRowFactory(tv -> {
            TableRow<ModelTable> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    ModelTable clickedRow = row.getItem();
                    selectedOS = clickedRow.getOs();
                }
            });
            return row ;
        });

        //MINIMIZAR APP
        minimizeBtn.setOnMouseClicked( event -> {
            Stage obj = (Stage) minimizeBtn.getScene().getWindow();
            obj.setIconified(true);
        });

        updateTable();
    }

    // Gambiarra para quando voltar aparecer o nome do usuário no "Olá, ..."
    public void getText(String username) {
        labelEs.setText(username);
    }

    // Fecha o aplicativo clicando no "X"
    public void close(ActionEvent e) {
        Platform.exit();
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


    public void delete(){
        if (selectedOS == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletar Ordem de Serviço");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja deletar a ordem de serviço?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            String sql = "DELETE FROM tbos WHERE OS = ?";
            try {
                PreparedStatement pst = conexao.prepareStatement(sql);
                pst.setString(1, selectedOS);
                pst.execute();
                updateTable();
                selectedOS = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTable() {
        final ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
        FilteredList<ModelTable> filteredData = new FilteredList<>(oblist, b -> true);
        filterField.setText("");

        filteredData.clear();
        try {
            assert conexao != null;
            ResultSet rs = conexao.createStatement().executeQuery("SELECT * FROM tbos");

            while(rs.next()){
                oblist.add(new ModelTable(rs.getString("OS"), rs.getString("Data_OS"),
                        rs.getString("Equipamento"), rs.getString("Defeito"),
                        rs.getString("Servico"), rs.getString("Valor"),
                        rs.getString("ID_Cliente")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        os_col_num.setCellValueFactory(new PropertyValueFactory<>("os"));
        os_col_data.setCellValueFactory(new PropertyValueFactory<>("data_os"));
        os_col_equip.setCellValueFactory(new PropertyValueFactory<>("equipamento"));
        os_col_def.setCellValueFactory(new PropertyValueFactory<>("defeito"));
        os_col_serv.setCellValueFactory(new PropertyValueFactory<>("servico"));
        os_col_valor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        os_col_id_cliente.setCellValueFactory(new PropertyValueFactory<>("id_cliente"));

        os_table.setItems(oblist);

        filterField.textProperty().addListener((observableValue, s, t1) -> {
            filteredData.setPredicate(valor -> {
                if (t1 == null || t1.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = t1.toLowerCase();

                if (valor.getOs().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (valor.getValor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (valor.getData_os().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (valor.getDefeito().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (valor.getEquipamento().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (valor.getId_cliente().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return valor.getServico().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<ModelTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(os_table.comparatorProperty());
        os_table.setItems(sortedData);
    }
}
