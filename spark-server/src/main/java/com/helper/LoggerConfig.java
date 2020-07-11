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
//            p.setProperty("log4j.appender.file.File", "/Users/bootcamp/logs/log4j-spark-application.log");
//            p.setProperty("log4j.appender.file.File", "/root/logs/log4j-spark-application.log");
            PropertyConfigurator.configure(p);
        } catch (IOException e) {
            throw new IOException("No config file");
        }
    }
}
