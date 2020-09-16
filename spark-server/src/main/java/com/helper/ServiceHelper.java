package com.helper;

import com.service.LogicService;
import com.service.DatabaseService;
import com.service.DatabaseServiceSQLImpl;
import com.service.LogicServiceBean;
import org.apache.log4j.Logger;

import java.sql.DriverManager;

public abstract class ServiceHelper {
    private static final Logger logger = Logger.getLogger(ServiceHelper.class);
    private static DatabaseService _database = null;
    private static LogicService _logic = null;

    public static synchronized DatabaseService getDatabaseService() {
        if (_database == null) {
            try {
                _database = new DatabaseServiceSQLImpl(DriverManager.getConnection(Config.getUrl(), Config.getConfig()));
            } catch (Exception ex) {
                logger.info("Error receiving database connection info.");
                ex.printStackTrace();
            }
        }
        return _database;
    }

    public static synchronized LogicService getLogicService() {
        if (_logic == null) {
            _logic = new LogicServiceBean();
        }
        return _logic;
    }
}
