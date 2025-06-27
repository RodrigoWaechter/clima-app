package com.unisc.projeto.clima_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unisc.projeto.clima_app.domain.Preferencia;
import com.unisc.projeto.clima_app.domain.Tema;

public class PreferenciaDAO {
    private static final Logger LOGGER = Logger.getLogger(PreferenciaDAO.class.getName());

    public Optional<Preferencia> findPreferencia() {
        String sql = "SELECT * FROM configuracoes LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                Preferencia pref = new Preferencia();
                pref.setIdPreferencia(rs.getInt("id_configuracao")); 
                pref.setCidadePreferida(rs.getString("cidade_preferida"));
                
                String nomeDoTema = rs.getString("tema_app");
                pref.setTemaApp(Tema.fromDisplayName(nomeDoTema));
               
                return Optional.of(pref);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar configurações do banco de dados.", e);
        }
        return Optional.empty();
    }

    public void saveOrUpdate(Preferencia preferencia) {
        Optional<Preferencia> existente = findPreferencia();

        if (existente.isPresent()) {
            preferencia.setIdPreferencia(existente.get().getIdPreferencia());
            update(preferencia);
        } else {
            insert(preferencia);
        }
    }

    private void insert(Preferencia preferencia) {
        String sql = "INSERT INTO configuracoes (cidade_preferida, tema_app) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, preferencia.getCidadePreferida());
            pstmt.setString(2, preferencia.getTemaApp().getDisplayName());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir nova configuração.", e);
            throw new RuntimeException("Falha ao salvar nova configuração.", e);
        }
    }

    private void update(Preferencia preferencia) {
        String sql = "UPDATE configuracoes SET cidade_preferida = ?, tema_app = ? WHERE id_configuracao = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, preferencia.getCidadePreferida());
            pstmt.setString(2, preferencia.getTemaApp().getDisplayName());
            pstmt.setInt(3, preferencia.getIdPreferencia());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar configuração.", e);
            throw new RuntimeException("Falha ao atualizar configuração.", e);
        }
    }
}