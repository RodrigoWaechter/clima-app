package com.unisc.projeto.clima_app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.unisc.projeto.clima_app.database.DatabaseConnection;
import com.unisc.projeto.clima_app.domain.Preferencia;
import com.unisc.projeto.clima_app.domain.Tema;

public class PreferenciaDAO {
    private static final Logger LOGGER = Logger.getLogger(PreferenciaDAO.class.getName());

    public Optional<Preferencia> findPreferencia() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM configuracoes LIMIT 1");
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString());
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {                   
                    return Optional.of(mapRowToObject(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar configurações do banco de dados.", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
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
        StringBuilder sql = new StringBuilder(); 
        sql.append("INSERT INTO configuracoes (cidade_preferida, tema_app) VALUES (?, ?)");
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                pstmt.setString(1, preferencia.getCidadePreferida());
                pstmt.setString(2, preferencia.getTemaApp().getDisplayName());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao inserir nova configuração.", e);
            throw new RuntimeException("Falha ao salvar nova configuração.", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void update(Preferencia preferencia) {
        StringBuilder sql = new StringBuilder(); 
        sql.append("UPDATE configuracoes SET cidade_preferida = ?, tema_app = ? WHERE id_configuracao = ?");
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                pstmt.setString(1, preferencia.getCidadePreferida());
                pstmt.setString(2, preferencia.getTemaApp().getDisplayName());
                pstmt.setInt(3, preferencia.getIdPreferencia());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar configuração.", e);
            throw new RuntimeException("Falha ao atualizar configuração.", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
    
	// pega a linha do banco e setta no objeto
	private Preferencia mapRowToObject(ResultSet rs) throws SQLException {
		Preferencia pref = new Preferencia();
		pref.setIdPreferencia(rs.getInt("id_configuracao")); 
		pref.setCidadePreferida(rs.getString("cidade_preferida"));
		
		String nomeDoTema = rs.getString("tema_app");
		pref.setTemaApp(Tema.fromDisplayName(nomeDoTema));
		return pref;
	}
}