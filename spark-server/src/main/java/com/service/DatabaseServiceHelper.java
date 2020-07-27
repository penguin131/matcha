package com.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public abstract class DatabaseServiceHelper {
    private static final Logger logger = Logger.getLogger(DatabaseServiceHelper.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static DatabaseService _instance = null;

    public static DatabaseService getDatabaseService(File config) {
        if (_instance == null) {
            try {
                Properties props = new Properties();
                InputStream input = new FileInputStream(config);
                props.load(input);
                Class.forName(props.getProperty("driver"));
                String url = props.getProperty("url");
                _instance = new DatabaseServiceSQLImpl(props);
                logger.info("url: " + url + "\nProperties:\n" + mapper.writeValueAsString(props));
            } catch (Exception ex) {
                logger.info("Error receiving database connection info.");
            }
        }
        return _instance;
    }
}
