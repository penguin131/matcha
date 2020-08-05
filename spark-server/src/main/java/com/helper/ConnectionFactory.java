package com.helper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static Properties props;
    static {
        try {
            props = new Properties();
            InputStream input = new FileInputStream(Config.getConfig());
            props.load(input);
            Class.forName(props.getProperty("driver"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(props.getProperty("url"), props);
    }
}
