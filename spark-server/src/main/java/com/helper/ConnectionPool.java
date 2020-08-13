package com.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionPool {
    private static int CONNECTION_POOL_SIZE;
    private static Queue<Connection> connectionPool;
    private static Queue<Connection> usedConnections;

    public Connection getConnection() {
        try {
            while (connectionPool.size() == 0)
                wait(1);
            Connection result = connectionPool.remove();
            usedConnections.add(result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.add(connection);
            usedConnections.remove(connection);
        }
    }

    static {
        try {
            Class.forName(Config.getConfig().getProperty("driver"));
            CONNECTION_POOL_SIZE = Integer.parseInt(Config.getConfig().getProperty("connectionPool.size"));
            connectionPool = new ConcurrentLinkedQueue<>();
            usedConnections = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
                connectionPool.add(createConnection());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(Config.getConfig().getProperty("url"), Config.getConfig());
    }
}
