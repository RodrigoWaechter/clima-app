package com.unisc.projeto.clima_app.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupDatabase {

    private static final Logger LOGGER = Logger.getLogger(SetupDatabase.class.getName());
    private static final String SCRIPT_FILE = "/script-clima_app.sql";
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/clima_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; 


    public static void runSetup() {
        LOGGER.log(Level.INFO, "--- INICIANDO SETUP DO BANCO DE DADOS ---");
        try {
            checkAndCreateDatabase();
            checkAndPopulateTables();
            LOGGER.log(Level.INFO, "\n[SUCESSO] O banco de dados está pronto para ser usado.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "\n[ERRO] Ocorreu uma falha durante o setup do banco de dados.", e);
        } finally {
            LOGGER.log(Level.INFO, "--- SETUP DO BANCO DE DADOS FINALIZADO ---");
        }
    }

    private static void checkAndCreateDatabase() throws SQLException {
        String serverUrl = getServerUrl(DB_URL);
        String dbName = getDatabaseName(DB_URL);

        LOGGER.log(Level.INFO, "Verificando se o banco de dados ''{0}'' existe...", dbName);
        try (Connection conn = DriverManager.getConnection(serverUrl, DB_USER, DB_PASSWORD);
             ResultSet rs = conn.getMetaData().getCatalogs()) {
            boolean dbExists = false;
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(dbName)) {
                    dbExists = true;
                    break;
                }
            }

            if (dbExists) {
                LOGGER.log(Level.INFO, "-> Banco de dados encontrado.");
            } else {
                LOGGER.log(Level.INFO, "-> Banco de dados NÃO encontrado. Criando...");
                try (Statement stmt = conn.createStatement()) {
                    String sql = "CREATE DATABASE " + dbName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(sql);
                    LOGGER.log(Level.INFO, "-> Banco de dados criado com sucesso.");
                }
            }
        }
    }

    private static void checkAndPopulateTables() throws SQLException, IOException {
        LOGGER.log(Level.INFO, "Verificando se o banco de dados já contém tabelas...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean anyTableExists = false;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
                if (rs.next()) {
                    anyTableExists = true;
                }
            }

            if (anyTableExists) {
                LOGGER.log(Level.INFO, "-> Tabelas encontradas. O banco de dados parece já estar populado.");
            } else {
                LOGGER.log(Level.INFO, "-> Nenhuma tabela encontrada. Populando o banco de dados...");
                populateDatabase(conn);
            }
        }
    }

    private static void populateDatabase(Connection conn) throws IOException, SQLException {
        LOGGER.log(Level.INFO, "Executando script SQL...");
        try (Statement stmt = conn.createStatement()) {
            InputStream is = SetupDatabase.class.getResourceAsStream(SCRIPT_FILE);
            if (is == null) {
                throw new IOException("Arquivo de script '" + SCRIPT_FILE + "' não encontrado em 'src/main/resources'.");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty() || line.trim().startsWith("--")) {
                        continue;
                    }
                    sb.append(line).append(System.lineSeparator());
                }

                String[] commands = sb.toString().split(";");
                for (String command : commands) {
                    if (command.trim().isEmpty()) {
                        continue;
                    }
                    stmt.execute(command);
                }
            }
            LOGGER.log(Level.INFO, "-> Script executado e banco de dados populado com sucesso.");
        }
    }

    private static String getServerUrl(String dbUrl) {
        int lastSlash = dbUrl.lastIndexOf('/');
        if (lastSlash == -1) {
            throw new IllegalArgumentException("Formato de URL do banco de dados inválido. Esperado: jdbc:mysql://host:port/dbname");
        }
        return dbUrl.substring(0, lastSlash);
    }

    private static String getDatabaseName(String dbUrl) {
        int lastSlash = dbUrl.lastIndexOf('/');
        if (lastSlash == -1) {
            throw new IllegalArgumentException("Formato de URL do banco de dados inválido. Esperado: jdbc:mysql://host:port/dbname");
        }
        int questionMark = dbUrl.indexOf('?', lastSlash);
        return (questionMark == -1) ? dbUrl.substring(lastSlash + 1) : dbUrl.substring(lastSlash + 1, questionMark);
    }
}