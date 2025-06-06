package ru.se.ifmo.db;

import jakarta.inject.Inject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnectionController {
    private Connection connection;

    public SQLConnectionController() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            String jdbcURL = "jdbc:" + properties.getProperty("db.driver") + "://"
                + properties.getProperty("db.host") + ":" + properties.getProperty("db.port")
                + "/" + properties.getProperty("db.name");
            connection = DriverManager.getConnection(jdbcURL, properties.getProperty("db.user"),
                properties.getProperty("db.password"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        } catch (SQLException e) {
            DatabaseUtils.logSQLException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
