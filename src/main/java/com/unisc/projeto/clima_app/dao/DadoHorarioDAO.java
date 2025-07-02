package com.unisc.projeto.clima_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unisc.projeto.clima_app.database.DatabaseConnection;
import com.unisc.projeto.clima_app.domain.DadoHorario;

public class DadoHorarioDAO {

    private static final Logger LOGGER = Logger.getLogger(DadoHorarioDAO.class.getName());

    public void save(List<DadoHorario> dados) {
        if (dados == null || dados.isEmpty()) return;

        Connection conn = null;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO dados_horarios (id_localizacao, horario, temperatura, umidade_relativa, sensacao_termica, velocidade_vento, direcao_vento, precipitacao, cd_clima) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        sql.append("ON DUPLICATE KEY UPDATE ");
        sql.append("temperatura = VALUES(temperatura), ");
        sql.append("umidade_relativa = VALUES(umidade_relativa), ");
        sql.append("sensacao_termica = VALUES(sensacao_termica), ");
        sql.append("velocidade_vento = VALUES(velocidade_vento), ");
        sql.append("direcao_vento = VALUES(direcao_vento), ");
        sql.append("precipitacao = VALUES(precipitacao), ");
        sql.append("cd_clima = VALUES(cd_clima)");

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                for (DadoHorario dado : dados) {
                    pstmt.setInt(1, dado.getLocalizacao().getIdLocalizacao());
                    pstmt.setTimestamp(2, Timestamp.valueOf(dado.getHorario()));
                    pstmt.setDouble(3, dado.getTemperatura());
                    pstmt.setDouble(4, dado.getUmidadeRelativa());
                    pstmt.setDouble(5, dado.getSensacaoTermica());
                    pstmt.setDouble(6, dado.getVelocidadeVento());
                    pstmt.setShort(7, dado.getDirecaoVento());
                    pstmt.setDouble(8, dado.getPrecipitacao());
                    pstmt.setInt(9, dado.getCdClima());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            conn.commit(); 
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro na transação de salvar previsão horária, executando rollback.", e);
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erro ao executar rollback.", ex);
            }
            throw new RuntimeException("Falha ao salvar dados horários no banco.", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

	public List<DadoHorario> queryProximas24Horas(int localizacaoId) {
		List<DadoHorario> dados = new ArrayList<>();
		StringBuilder sql = new StringBuilder(); 
		sql.append("SELECT * FROM dados_horarios WHERE id_localizacao = ? AND horario >= ? ORDER BY horario ASC LIMIT 24");
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
				pstmt.setInt(1, localizacaoId);
				pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						dados.add(mapRowToObject(rs));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Erro ao buscar previsão de 24 horas do banco.", e);
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		return dados;
	}

	public Optional<DadoHorario> queryHoraAutal(Integer localizacaoId) {
		StringBuilder sql = new StringBuilder(); 
		sql.append("SELECT * FROM dados_horarios WHERE id_localizacao = ? AND horario <= ? ORDER BY horario DESC LIMIT 1");
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
				pstmt.setInt(1, localizacaoId);
				pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						return Optional.of(mapRowToObject(rs));
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Erro ao buscar último dado horário para a localização ID: " + localizacaoId, e);
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		return Optional.empty();
	}

	// pega a linha do banco e setta no objeto
	private DadoHorario mapRowToObject(ResultSet rs) throws SQLException {
		DadoHorario dado = new DadoHorario();
		dado.setIdDadoHorario(rs.getInt("id_dado_horario"));
		if (rs.getTimestamp("horario") != null) {
			dado.setHorario(rs.getTimestamp("horario").toLocalDateTime());
		}
		dado.setTemperatura(rs.getDouble("temperatura"));
		dado.setUmidadeRelativa(rs.getDouble("umidade_relativa"));
		dado.setSensacaoTermica(rs.getDouble("sensacao_termica"));
		dado.setVelocidadeVento(rs.getDouble("velocidade_vento"));
		dado.setDirecaoVento(rs.getShort("direcao_vento"));
		dado.setPrecipitacao(rs.getDouble("precipitacao"));
		dado.setCdClima(rs.getInt("cd_clima"));
		return dado;
	}
}