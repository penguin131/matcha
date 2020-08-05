package com.service;

import org.apache.log4j.Logger;

public abstract class DatabaseServiceHelper {
    private static final Logger logger = Logger.getLogger(DatabaseServiceHelper.class);
    private static DatabaseService _instance = null;

    public static DatabaseService getDatabaseService() {
        if (_instance == null) {
            try {
                _instance = new DatabaseServiceSQLImpl();
            } catch (Exception ex) {
                logger.info("Error receiving database connection info.");
            }
        }
        return _instance;
    }
}
