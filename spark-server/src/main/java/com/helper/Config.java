package com.helper;

import java.io.File;

public class Config {
    private static File config;
    static {
        config = new File("target/spark-server-1.0/WEB-INF/classes/application.properties");
        if (!config.exists()) {
            config = new File("../spark-server-1.0/WEB-INF/classes/application.properties");
        }
    }

    public static File getConfig() {
        return config;
    }
}
