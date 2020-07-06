package com.helper;

import java.io.File;

public class Config {
    private static File config;
    private static File loggerConfig;
    private static boolean isProd = true;
    static {
        config = new File("target/spark-server-1.0/WEB-INF/classes/application.properties");
        if (!config.exists()) {
            isProd = false;
            config = new File("../spark-server-1.0/WEB-INF/classes/application.properties");
        }
        loggerConfig = new File("target/spark-server-1.0/WEB-INF/classes/log4j.properties");
        if (!loggerConfig.exists()) {
            loggerConfig = new File("../spark-server-1.0/WEB-INF/classes/log4j.properties");
        }
    }

    public static File getConfig() {
        return config;
    }

    public static File getLoggerConfig() {
        return loggerConfig;
    }

    public static boolean isIsProd() {
        return isProd;
    }
}
