package br.com.agenda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaConexao {
	private static final String USUARIO = "postgres";
	private static final String SENHA = "postgres";
	private static final String URL = "jdbc:postgresql://localhost:5432/agenda";
	
	public static Connection conectar() throws SQLException {
		DriverManager.registerDriver(new org.postgresql.Driver());
		Connection conexao = DriverManager.getConnection(URL,USUARIO,SENHA);
		return conexao;
	}

}
