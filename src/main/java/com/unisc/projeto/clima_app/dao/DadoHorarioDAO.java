package com.unisc.projeto.clima_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unisc.projeto.clima_app.domain.DadoHorario;

/**
 * DAO para realizar as operações de CRUD para a entidade DadoHorario.
 */
public class DadoHorarioDAO {
    
    private static final Logger LOGGER = Logger.getLogger(DadoHorarioDAO.class.getName());
    // CORRIGIDO: Nome da tabela atualizado para o plural.
    private static final String TABLE_NAME = "dados_horarios";

    public void save(DadoHorario dado) {
        String sql = "INSERT INTO " + TABLE_NAME + " (id_localizacao, horario, temperatura, umidade_relativa, sensacao_termica, velocidade_vento, direcao_vento, precipitacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            validateLocalizacao(dado);
            pstmt.setInt(1, dado.getLocalizacao().getIdLocalizacao());
            pstmt.setTimestamp(2, Timestamp.valueOf(dado.getHorario()));
            pstmt.setDouble(3, dado.getTemperatura());
            pstmt.setDouble(4, dado.getUmidadeRelativa());
            pstmt.setDouble(5, dado.getSensacaoTermica());
            pstmt.setDouble(6, dado.getVelocidadeVento());
            pstmt.setShort(7, dado.getDirecaoVento());
            pstmt.setDouble(8, dado.getPrecipitacao());
            
            pstmt.executeUpdate();
            LOGGER.info("Novo dado horário salvo para a localização ID: " + dado.getLocalizacao().getIdLocalizacao());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao salvar dado horário.", e);
            throw new RuntimeException("Falha ao inserir dados no banco.", e);
        }
    }
    
    public Optional<DadoHorario> findById(Integer id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_dado_horario = ?";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToObject(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar dado horário pelo ID: " + id, e);
            throw new RuntimeException("Falha ao consultar dados no banco.", e);
        }
        return Optional.empty();
    }

    public Optional<DadoHorario> findLatestByLocalizacaoId(Integer localizacaoId) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_localizacao = ? ORDER BY horario DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, localizacaoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToObject(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar último dado horário para a localização ID: " + localizacaoId, e);
            throw new RuntimeException("Falha ao consultar dados no banco.", e);
        }
        return Optional.empty();
    }

    public List<DadoHorario> findAllByLocalizacaoId(Integer localizacaoId) {
        List<DadoHorario> dados = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_localizacao = ? ORDER BY horario DESC";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, localizacaoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dados.add(mapRowToObject(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar todos os dados horários para a localização ID: " + localizacaoId, e);
            throw new RuntimeException("Falha ao consultar dados no banco.", e);
        }
        return dados;
    }

    public void update(DadoHorario dado) {
        String sql = "UPDATE " + TABLE_NAME + " SET id_localizacao = ?, horario = ?, temperatura = ?, umidade_relativa = ?, sensacao_termica = ?, velocidade_vento = ?, direcao_vento = ?, precipitacao = ? WHERE id_dado_horario = ?";
        
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            validateLocalizacao(dado);
            pstmt.setInt(1, dado.getLocalizacao().getIdLocalizacao());
            pstmt.setTimestamp(2, Timestamp.valueOf(dado.getHorario()));
            pstmt.setDouble(3, dado.getTemperatura());
            pstmt.setDouble(4, dado.getUmidadeRelativa());
            pstmt.setDouble(5, dado.getSensacaoTermica());
            pstmt.setDouble(6, dado.getVelocidadeVento());
            pstmt.setShort(7, dado.getDirecaoVento());
            pstmt.setDouble(8, dado.getPrecipitacao());
            pstmt.setInt(9, dado.getIdDadoHorario());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar dado horário.", e);
            throw new RuntimeException("Falha ao atualizar dados no banco.", e);
        }
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id_dado_horario = ?";
        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao deletar dado horário pelo ID: " + id, e);
            throw new RuntimeException("Falha ao deletar dados do banco.", e);
        }
    }

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
        
        return dado;
    }

    private void validateLocalizacao(DadoHorario dado) {
        if (dado.getLocalizacao() == null || dado.getLocalizacao().getIdLocalizacao() == null) {
            throw new IllegalArgumentException("DadoHorario deve ter uma Localizacao com ID para ser salvo ou atualizado.");
        }
    }
}
