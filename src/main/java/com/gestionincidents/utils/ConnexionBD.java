package com.gestionincidents.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnexionBD {

    private static final Logger logger = LogManager.getLogger(ConnexionBD.class);
    private static HikariDataSource dataSource;

    public static Connection getConnection() throws SQLException, IOException {
        if (dataSource == null) {
            try {
                Properties properties = new Properties();
                InputStream inputStream = ConnexionBD.class.getResourceAsStream("/application.properties");

                if (inputStream == null) {
                    throw new IOException("Fichier de configuration application.properties non trouvé.");
                }

                properties.load(inputStream);

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(properties.getProperty("spring.datasource.url"));
                config.setUsername(properties.getProperty("spring.datasource.username"));
                config.setPassword(properties.getProperty("spring.datasource.password"));
                config.setMaximumPoolSize(10); // Ajustez la taille du pool selon vos besoins

                dataSource = new HikariDataSource(config);

                logger.info("Pool de connexions HikariCP initialisé.");

            } catch (IOException e) {
                logger.error("Erreur lors de l'initialisation du pool de connexions : " + e.getMessage());
                throw e;
            }
        }
        return dataSource.getConnection();
    }
}