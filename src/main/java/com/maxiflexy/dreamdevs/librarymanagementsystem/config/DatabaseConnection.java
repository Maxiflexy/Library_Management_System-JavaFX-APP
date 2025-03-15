package com.maxiflexy.dreamdevs.librarymanagementsystem.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "/config.properties";
    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try {
            loadConfig();
            Class.forName(driver);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loadConfig() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = DatabaseConnection.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IOException("Config file not found: " + CONFIG_FILE);
            }
            properties.load(inputStream);
            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
            driver = properties.getProperty("db.driver");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

