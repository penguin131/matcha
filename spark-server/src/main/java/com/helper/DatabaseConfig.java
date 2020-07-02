package com.helper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    public static DatabaseProperties getDatabaseProperties() throws Exception {
        try {
            InputStream input = new FileInputStream("../sparkjava-hello-world-1.0/WEB-INF/classes/application.properties");
            Properties props = new Properties();
            props.load(input);
            Class.forName(props.getProperty("driver"));
            String url = props.getProperty("url");
            return new DatabaseProperties(props, url);
        } catch (Exception ex) {
            throw new Exception("Error receiving database connection info.");
        }
    }

    public static class DatabaseProperties {
        private Properties properties;
        private String url;

        private DatabaseProperties(Properties properties, String url) {
            this.properties = properties;
            this.url = url;
        }

        public Properties getProperties() {
            return properties;
        }

        public String getUrl() {
            return url;
        }
    }
}
