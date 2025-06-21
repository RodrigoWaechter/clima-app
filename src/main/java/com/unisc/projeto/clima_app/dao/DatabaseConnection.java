package com.unisc.projeto.clima_app.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final Properties props = new Properties();
    private static Connection singleConnection;

    private DatabaseConnection() {}

    // Bloco estático para carregar as propriedades do banco uma única vez.
    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("Arquivo 'config.properties' não encontrado no classpath.");
            }
            props.load(input);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar 'config.properties'", ex);
            throw new RuntimeException(ex);
        }
    }

   
    public static Connection getConnection() throws SQLException {
        if (singleConnection == null || singleConnection.isClosed()) {
            singleConnection = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
            );
        }
        return singleConnection;
    }
}