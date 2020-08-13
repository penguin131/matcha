package com.helper;

import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

public class LoggerConfig {

    public static void configureLogger() {
        Properties p = Config.getLoggerConfig();
//            p.setProperty("log4j.appender.file.File", "/Users/bootcamp/logs/log4j-spark-application.log");
//            p.setProperty("log4j.appender.file.File", "/root/logs/log4j-spark-application.log");
        PropertyConfigurator.configure(p);
    }
}
