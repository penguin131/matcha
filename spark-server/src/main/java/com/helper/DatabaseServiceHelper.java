package com.helper;

import com.service.DatabaseService;
import com.service.DatabaseServiceORMImp;
import com.service.DatabaseServiceSQLImpl;
import org.apache.log4j.Logger;

import javax.persistence.Persistence;

public abstract class DatabaseServiceHelper {
    private static final Logger logger = Logger.getLogger(DatabaseServiceHelper.class);
    private static DatabaseService _instance = null;

    public static synchronized DatabaseService getDatabaseService() {
        if (_instance == null) {
            try {
                if ("true".equals(Config.isORMModeEnabled())) {
                    _instance = new DatabaseServiceORMImp(Persistence.createEntityManagerFactory("Model").createEntityManager());
                } else {
                    _instance = new DatabaseServiceSQLImpl();
                }
            } catch (Exception ex) {
                logger.info("Error receiving database connection info.");
                ex.printStackTrace();
            }
        }
        return _instance;
    }
}
