package com.unisc.projeto.clima_app.dao;

import com.unisc.projeto.clima_app.database.DatabaseConnection;
import com.unisc.projeto.clima_app.domain.Localizacao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalizacaoDAO {

	private static final Logger LOGGER = Logger.getLogger(LocalizacaoDAO.class.getName());

	public Optional<Localizacao> findByName(String nomeCidade) {
		Connection conn = null;
		String sql = "SELECT * FROM localizacoes WHERE nome_cidade = ?";
		try {
			conn = DatabaseConnection.getConnection();
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, nomeCidade);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						return Optional.of(mapRowToObject(rs));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Erro ao buscar localização por nome: " + nomeCidade, e);
			throw new RuntimeException("Falha ao consultar dados no banco.", e);
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		return Optional.empty();
	}

	public Optional<Localizacao> findLast() {
		Connection conn = null;
		String sql = "SELECT * FROM localizacoes ORDER BY data_hora_registro DESC LIMIT 1";
		try {
			conn = DatabaseConnection.getConnection();
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						return Optional.of(mapRowToObject(rs));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Erro ao buscar a última localização registrada.", e);
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		return Optional.empty();
	}

	public Localizacao save(Localizacao localizacao) {
	    Connection conn = null;
		String sql = "INSERT INTO localizacoes (nome_cidade, latitude, longitude, data_hora_registro) VALUES (?, ?, ?, ?)";
		try {
			conn = DatabaseConnection.getConnection();
			try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

				pstmt.setString(1, localizacao.getNomeCidade());
				pstmt.setDouble(2, localizacao.getLatitude());
				pstmt.setDouble(3, localizacao.getLongitude());
				pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

				int affectedRows = pstmt.executeUpdate();
				if (affectedRows == 0) {
					throw new SQLException("Falha ao criar localização, nenhuma linha afetada.");
				}

				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						localizacao.setIdLocalizacao(generatedKeys.getInt(1));
						return localizacao;
					} else {
						throw new SQLException("Falha ao criar localização, nenhum ID obtido.");
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Erro ao salvar localização.", e);
			throw new RuntimeException("Falha ao inserir dados no banco.", e);
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
	}
	
	// pega a linha do banco e setta no objeto
	private Localizacao mapRowToObject(ResultSet rs) throws SQLException {
		Localizacao localizacao = new Localizacao();
		localizacao.setIdLocalizacao(rs.getInt("id_localizacao"));
		localizacao.setNomeCidade(rs.getString("nome_cidade"));
		localizacao.setLatitude(rs.getDouble("latitude"));
		localizacao.setLongitude(rs.getDouble("longitude"));
		if (rs.getTimestamp("data_hora_registro") != null) {
			localizacao.setDataHoraRegistro(rs.getTimestamp("data_hora_registro").toLocalDateTime());
		}
		return localizacao;
	}
}