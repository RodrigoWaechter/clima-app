package com.unisc.projeto.clima_app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unisc.projeto.clima_app.database.DatabaseConnection;
import com.unisc.projeto.clima_app.domain.DadoDiario;
import com.unisc.projeto.clima_app.domain.Localizacao;

public class HistoricoDAO {

    private static final Logger LOGGER = Logger.getLogger(HistoricoDAO.class.getName());

    public ArrayList<DadoDiario> findDadosDiarios(LocalDate dataInicio, LocalDate dataFim) {
        ArrayList<DadoDiario> dados = new ArrayList<>();
        String sql = "SELECT d.*, l.nome_cidade FROM dados_diarios d " +
                "INNER JOIN localizacoes l ON d.id_localizacao = l.id_localizacao " +
                "WHERE d.data BETWEEN ? AND ? ORDER BY l.nome_cidade, d.data";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setDate(1, Date.valueOf(dataInicio));
                pstmt.setDate(2, Date.valueOf(dataFim));

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        DadoDiario dado = mapRowToObject(rs);
                        dados.add(dado);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar histórico com filtros.", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return dados;
    }

    public ArrayList<DadoDiario> findDadosDiarios() {
        ArrayList<DadoDiario> dados = new ArrayList<>();
        String sql = "SELECT * FROM dados_diarios d INNER JOIN localizacoes l ON d.id_localizacao = l.id_localizacao";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        DadoDiario dado = mapRowToObject(rs);
                        dados.add(dado);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar histórico", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return dados;
    }

	// pega a linha do banco e setta no objeto
	private DadoDiario mapRowToObject(ResultSet rs) throws SQLException {
		Localizacao loc = new Localizacao();
		loc.setNomeCidade(rs.getString("nome_cidade"));
		
		DadoDiario dado = new DadoDiario();
		dado.setLocalizacao(loc);
		dado.setData(rs.getDate("data").toLocalDate());
		dado.setTemperaturaMax(rs.getDouble("temperatura_max"));
		dado.setTemperaturaMin(rs.getDouble("temperatura_min"));
		dado.setPrecipitacaoTotal(rs.getDouble("precipitacao_total"));
		dado.setVelocidadeVentoMax(rs.getDouble("velocidade_vento_max"));
		return dado;
	}
}