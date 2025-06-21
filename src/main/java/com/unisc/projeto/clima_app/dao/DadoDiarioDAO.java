package com.unisc.projeto.clima_app.dao;

import com.unisc.projeto.clima_app.domain.DadoDiario;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DadoDiarioDAO {

	private static final Logger LOGGER = Logger.getLogger(DadoDiarioDAO.class.getName());

	// os multi try nos metodos serve para fechar o PreparedStatement e o ResultSet
	public void salvarPrevisoes(List<DadoDiario> dados) {
		if (dados == null || dados.isEmpty()) {
			return;
		}
		int localizacaoId = dados.get(0).getLocalizacao().getIdLocalizacao();

		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();

			// Inicia a transação
			conn.setAutoCommit(false);

			deleteByLocalizacaoId(conn, localizacaoId);

			String sql = "INSERT INTO dados_diarios (id_localizacao, data, temperatura_max, temperatura_min, precipitacao_total, velocidade_vento_max, cd_clima) VALUES (?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				for (DadoDiario dado : dados) {
					pstmt.setInt(1, dado.getLocalizacao().getIdLocalizacao());
					pstmt.setDate(2, Date.valueOf(dado.getData()));
					pstmt.setDouble(3, dado.getTemperaturaMax());
					pstmt.setDouble(4, dado.getTemperaturaMin());
					pstmt.setDouble(5, dado.getPrecipitacaoTotal());
					pstmt.setDouble(6, dado.getVelocidadeVentoMax());
					pstmt.setInt(7, dado.getCdClima());
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
			// Confirma a transação
			conn.commit(); 
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Erro na transação de salvar previsões diárias.", e);
			if (conn != null) {
				try {
					//rollback se der alguma cagada no meio
					conn.rollback();
				} catch (SQLException ex) {
					LOGGER.log(Level.SEVERE, "Erro no rollback.", ex);
				}
			}
			throw new RuntimeException("Falha ao salvar dados de previsão no banco.", e);
		}
	}

	public void deleteByLocalizacaoId(Connection conn, int localizacaoId) throws SQLException {
		String sql = "DELETE FROM dados_diarios WHERE id_localizacao = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, localizacaoId);
			pstmt.executeUpdate();
		}
	}

	public List<DadoDiario> queryProximos7Dias(Integer localizacaoId) {
		List<DadoDiario> dados = new ArrayList<>();
		String sql = "SELECT * FROM dados_diarios WHERE id_localizacao = ? AND data >= ? ORDER BY data ASC LIMIT 7";

		try {
			Connection conn = DatabaseConnection.getConnection();

			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, localizacaoId);
				pstmt.setDate(2, Date.valueOf(LocalDate.now()));

				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						dados.add(mapRowToObject(rs));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Erro ao buscar previsão de 7 dias do banco.", e);
		}
		return dados;
	}

	// pega a linha do banco e setta no objeto
	private DadoDiario mapRowToObject(ResultSet rs) throws SQLException {
		DadoDiario dado = new DadoDiario();
		dado.setIdDadoDiario(rs.getInt("id_dado_diario"));
		dado.setData(rs.getDate("data").toLocalDate());
		dado.setTemperaturaMax(rs.getDouble("temperatura_max"));
		dado.setTemperaturaMin(rs.getDouble("temperatura_min"));
		dado.setPrecipitacaoTotal(rs.getDouble("precipitacao_total"));
		dado.setVelocidadeVentoMax(rs.getDouble("velocidade_vento_max"));
		dado.setCdClima(rs.getInt("cd_clima"));
		return dado;
	}
}