package com.unisc.projeto.clima_app.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitária responsável por fornecer conexões com o banco de dados.
 * Esta classe agora atua como uma FÁBRICA de conexões, não mais como um Singleton,
 * resolvendo o problema de "conexão fechada".
 */
public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final Properties props = new Properties();

    // O bloco estático carrega as configurações do banco de dados uma única vez
    // quando a classe é carregada pela primeira vez.
    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                LOGGER.severe("ERRO CRÍTICO: Não foi possível encontrar o arquivo db.properties no classpath.");
                throw new IllegalStateException("Arquivo de configuração do banco de dados (db.properties) não encontrado.");
            }
            props.load(input);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar as propriedades do banco de dados.", ex);
            // Lança uma exceção em tempo de execução para parar a aplicação se as props não puderem ser carregadas.
            throw new RuntimeException(ex);
        }
    }

    // Construtor privado para impedir a instanciação, seguindo o padrão de classe utilitária.
    private DatabaseConnection() {}

    /**
     * Obtém uma NOVA conexão com o banco de dados a cada chamada.
     * A responsabilidade de fechar a conexão é de quem a solicita (o que já é feito
     * corretamente nos DAOs com try-with-resources).
     *
     * @return Uma nova instância de Connection.
     * @throws SQLException se ocorrer um erro ao obter a conexão.
     */
    public static Connection getDatabaseConnection() throws SQLException {
        // A cada chamada, uma nova conexão é criada e retornada.
        return DriverManager.getConnection(
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password")
        );
    }
}