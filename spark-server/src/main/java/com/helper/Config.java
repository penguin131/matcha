package com.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties config = new Properties();
    private static final Properties loggerConfig = new Properties();
    static {
        try {
            File configFile = new File("target/spark-server-1.0/WEB-INF/classes/application.properties");
            InputStream input1 = new FileInputStream(configFile);
            config.load(input1);
            File loggerConfigFile = new File("target/spark-server-1.0/WEB-INF/classes/log4j.properties");
            InputStream input2 = new FileInputStream(loggerConfigFile);
            loggerConfig.load(input2);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static Properties getConfig() {
        return config;
    }

    public static Properties getLoggerConfig() {
        return loggerConfig;
    }
}
