package com.unisc.projeto.clima_app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unisc.projeto.clima_app.database.DatabaseConnection;
import com.unisc.projeto.clima_app.domain.DadoDiario;

public class DadoDiarioDAO {

    private static final Logger LOGGER = Logger.getLogger(DadoDiarioDAO.class.getName());

    public void save(List<DadoDiario> dados) {
        if (dados == null || dados.isEmpty()) return;

        Connection conn = null;
        StringBuilder sql = new StringBuilder(); 
        sql.append("INSERT INTO dados_diarios (id_localizacao, data, temperatura_max, temperatura_min, precipitacao_total, velocidade_vento_max, cd_clima) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?) "); 
        sql.append("ON DUPLICATE KEY UPDATE "); 
        sql.append("temperatura_max = VALUES(temperatura_max), "); 
        sql.append("temperatura_min = VALUES(temperatura_min), "); 
        sql.append("precipitacao_total = VALUES(precipitacao_total), "); 
        sql.append("velocidade_vento_max = VALUES(velocidade_vento_max), "); 
        sql.append("cd_clima = VALUES(cd_clima)"); 
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
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
            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro na transação de salvar previsões diárias, executando rollback.", e);
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erro ao executar rollback.", ex);
            }
            throw new RuntimeException("Falha ao salvar dados de previsão no banco.", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }


	public List<DadoDiario> queryProximos7Dias(Integer localizacaoId) {
		List<DadoDiario> dados = new ArrayList<>();
		StringBuilder sql = new StringBuilder(); 
		sql.append("SELECT * FROM dados_diarios WHERE id_localizacao = ? AND data >= ? ORDER BY data ASC LIMIT 7");

		try {
			Connection conn = DatabaseConnection.getConnection();

			try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
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