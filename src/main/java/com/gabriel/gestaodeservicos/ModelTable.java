package com.gabriel.gestaodeservicos;

public class ModelTable {

    String os, data_os, equipamento, defeito, servico, valor, id_cliente;

    public ModelTable(String os, String data_os, String equipamento, String defeito, String servico, String valor, String id_cliente){
        this.os = os;
        this.data_os = data_os;
        this.equipamento = equipamento;
        this.defeito = defeito;
        this.servico = servico;
        this.valor = valor;
        this.id_cliente = id_cliente;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getData_os() {
        return data_os;
    }

    public void setData_os(String data_os) {
        this.data_os = data_os;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getDefeito() {
        return defeito;
    }

    public void setDefeito(String defeito) {
        this.defeito = defeito;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }
}
