package com.unisc.projeto.clima_app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

	private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

	private DatabaseConnection() {
	}

	public static Connection getConnection() throws SQLException {
		try {
			return DriverManager.getConnection(
					ConfigManager.getDbUrl(),
					ConfigManager.getDbUser(),
					ConfigManager.getDbPassword());
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar ao banco de dados", e);
		}
	}

	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "Erro ao fechar a conexão com o banco de dados.", e);
			}
		}
	}
}