package com.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    static {
        try {
            Class.forName(Config.getConfig().getProperty("driver"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(Config.getConfig().getProperty("url"), Config.getConfig());
    }
}
