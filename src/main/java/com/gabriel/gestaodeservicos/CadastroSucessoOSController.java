package com.gabriel.gestaodeservicos;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class CadastroSucessoOSController implements Initializable {

    @FXML
    private Button voltarBtn, imprimirBtn;
    @FXML
    private Label labelESNome, labelESEquip, labelESDef, labelESDesc, labelESVal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        imprimirBtn.setOnMouseClicked(e -> {
            Date d = new Date();

            Document doc = new Document();
            try {
                PdfWriter.getInstance(doc, new FileOutputStream("C:\\Users\\Gabriel\\Desktop\\Ordem_de_Serviço.pdf"));
                doc.open();
                doc.add(new Paragraph("+---------------------------------------------------------------------------------------------------------------------------+"));
                doc.add(new Paragraph("|                                                     ORDEM DE SERVIÇO                                                      |"));
                doc.add(new Paragraph("+---------------------------------------------------------------------------------------------------------------------------+"));
                doc.add(new Paragraph(d.toString()));
                doc.add(new Paragraph("\n Nome: " + labelESNome.getText()));
                doc.add(new Paragraph("\n Equipamento: " + labelESEquip.getText()));
                doc.add(new Paragraph("\n Defeito: " + labelESDef.getText()));
                doc.add(new Paragraph("\n Descrição do serivço: " + labelESDesc.getText()));
                doc.add(new Paragraph("\n Valor: R$" + labelESVal.getText()));
                imprimirBtn.setText("Feito");
                doc.close();
            } catch (Exception ex) {
                imprimirBtn.setText("Erro");
                ex.printStackTrace();
            }
        });
    }

    public void getInfo(String nome, String equipamento, String defeito, String desc, String valor) {

        labelESNome.setText(nome);
        labelESEquip.setText(equipamento);
        labelESDef.setText(defeito);
        labelESDesc.setText(desc);
        labelESVal.setText(valor);

    }

    public void closeWindow(ActionEvent e) {
        Stage stage = (Stage) voltarBtn.getScene().getWindow();
        stage.close();

    }
}
