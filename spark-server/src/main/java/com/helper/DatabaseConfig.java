package com.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    public static DatabaseProperties getDatabaseProperties() throws Exception {
        try {
            File conf = Config.getConfig();
            InputStream input = new FileInputStream(conf);
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
