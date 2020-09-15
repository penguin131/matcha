package com.helper;

import com.service.DatabaseService;
import com.service.DatabaseServiceSQLImpl;
import org.apache.log4j.Logger;

import java.sql.DriverManager;

public abstract class DatabaseServiceHelper {
    private static final Logger logger = Logger.getLogger(DatabaseServiceHelper.class);
    private static DatabaseService _instance = null;

    public static synchronized DatabaseService getDatabaseService() {
        if (_instance == null) {
            try {
                _instance = new DatabaseServiceSQLImpl(DriverManager.getConnection(Config.getUrl(), Config.getConfig()));
            } catch (Exception ex) {
                logger.info("Error receiving database connection info.");
                ex.printStackTrace();
            }
        }
        return _instance;
    }
}
