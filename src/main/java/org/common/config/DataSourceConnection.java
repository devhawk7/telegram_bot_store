package org.common.config;

import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@RequiredArgsConstructor
public class DataSourceConnection {
    private Connection connection;

    public Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            Properties properties = new Properties();
            try (FileInputStream input = new FileInputStream("src/main/resources/application.properties")) {
                properties.load(input);
            }
            String url = properties.getProperty("datasource.url");
            String user = properties.getProperty("datasource.userName");
            String password = properties.getProperty("datasource.password");
            String driver = properties.getProperty("datasource.Driver");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
