package com.helper;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoggerConfig {

    public static void configureLogger() throws IOException {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(Config.getLoggerConfig()));
            if (!Config.isIsProd()) {
                p.setProperty("log4j.appender.file.File", "/Users/bootcamp/logs/log4j-spark-application.log");
            } else {
                p.setProperty("log4j.appender.file.File", "/Users/bootcamp/logs/log4j-spark-application.log");//todo add prod classpath
            }
            PropertyConfigurator.configure(p);
        } catch (IOException e) {
            throw new IOException("No config file");
        }
    }
}
