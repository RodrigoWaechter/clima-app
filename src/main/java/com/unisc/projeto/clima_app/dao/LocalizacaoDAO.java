package com.unisc.projeto.clima_app.dao;

import com.unisc.projeto.clima_app.domain.Localizacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para a tabela 'localizacoes'.
 */
public class LocalizacaoDAO {

    private static final Logger LOGGER = Logger.getLogger(LocalizacaoDAO.class.getName());
    // CORRIGIDO: Nome da tabela atualizado para o plural.
    private static final String TABLE_NAME = "localizacoes";

    /**
     * Busca uma localização no banco de dados pelo nome exato da cidade.
     */
    public Optional<Localizacao> findByName(String nomeCidade) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE nome_cidade = ?";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomeCidade);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToObject(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar localização por nome: " + nomeCidade, e);
            throw new RuntimeException("Falha ao consultar dados no banco.", e);
        }
        return Optional.empty();
    }

    /**
     * Salva uma nova Localizacao no banco e retorna o objeto salvo com o ID gerado.
     */
    public Localizacao save(Localizacao localizacao) {
        String sql = "INSERT INTO " + TABLE_NAME + " (nome_cidade, latitude, longitude, data_hora_registro) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, localizacao.getNomeCidade());
            pstmt.setDouble(2, localizacao.getLatitude());
            pstmt.setDouble(3, localizacao.getLongitude());
            pstmt.setTimestamp(4, Timestamp.valueOf(localizacao.getDataHoraRegistro()));
            
            if (pstmt.executeUpdate() == 0) {
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar localização.", e);
            throw new RuntimeException("Falha ao inserir dados no banco.", e);
        }
    }
    
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