package br.com.agenda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.agenda.modelo.Evento;

public class EventoDAO {
	PreparedStatement pstm;
	ResultSet rs;
	private Connection conexao;
	
	public void salvar(Evento evento) throws SQLException{
		String sql = "INSERT INTO evento(titulo_evento,inicio_evento,fim_evento,desc_evento,status_evento) VALUES(?,?,?,?,?)";
		conexao = FabricaConexao.conectar();
		pstm = conexao.prepareStatement(sql);
		
		pstm.setString(1, evento.getTitulo());
		pstm.setDate(2, new java.sql.Date(evento.getInicio().getTime()));
		pstm.setDate(3, new java.sql.Date(evento.getFim().getTime()));
		pstm.setString(4, evento.getDescricao());
		pstm.setBoolean(5, evento.isStatus());
		pstm.executeUpdate();
	}

	public void atualizar(Evento evento) throws SQLException{
		String sql = "UPDATE evento SET titulo_evento=? ,inicio_evento=?, fim_evento=?, desc_evento=?, status_evento=? WHERE id_evento =?";
		conexao = FabricaConexao.conectar();
		pstm = conexao.prepareStatement(sql);
		
		pstm.setString(1, evento.getTitulo());
		pstm.setDate(2, new java.sql.Date(evento.getInicio().getTime()));
		pstm.setDate(3, new java.sql.Date(evento.getFim().getTime()));
		pstm.setString(4, evento.getDescricao());
		pstm.setBoolean(5, evento.isStatus());
		pstm.setLong(6, evento.getId());
		pstm.executeUpdate();
	}
	
	public List<Evento> todosEventos() throws SQLException{
		List<Evento> eventos = new ArrayList<Evento>();
		
		String sql = "SELECT * FROM evento";
		
		conexao = FabricaConexao.conectar();
		pstm = conexao.prepareStatement(sql);
		
		rs = pstm.executeQuery();
		
		while(rs.next()){
			
			Evento e = new Evento();
			
			e.setId(rs.getLong(1));
			e.setTitulo(rs.getString(2));
			e.setInicio(rs.getDate(3));
			e.setFim(rs.getDate(4));
			e.setDescricao(rs.getString(5));
			e.setStatus(rs.getBoolean(6));
			eventos.add(e);
		}
		return eventos;
	}
	
	
}
