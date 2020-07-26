package com.helper;

import java.io.File;

public class Config {
    private static File config;
    private static File loggerConfig;
    static {
        config = new File("target/spark-server-1.0/WEB-INF/classes/application.properties");
        loggerConfig = new File("target/spark-server-1.0/WEB-INF/classes/log4j.properties");
    }

    public static File getConfig() {
        return config;
    }

    public static File getLoggerConfig() {
        return loggerConfig;
    }
}