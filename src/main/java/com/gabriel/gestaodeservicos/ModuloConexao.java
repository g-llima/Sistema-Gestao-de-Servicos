package com.gabriel.gestaodeservicos;

import java.sql.*;

public class ModuloConexao {

    //Método para conectar com o DB
    public static Connection conector(){

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/dbinfox";
        String user = "root";
        String password = "";

        try {
            Class.forName(driver);
            Connection conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            return null;
        }
    }

}
