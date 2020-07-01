package com.helper;

import java.util.Properties;

public class DatabaseConfig {
    public static DatabaseProperties getDatabaseProperties() {
        String url = "jdbc:postgresql://84.38.183.163:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user","sammy");
        props.setProperty("password","123");
        props.setProperty("ssl","true");
        return new DatabaseProperties(props, url);
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
