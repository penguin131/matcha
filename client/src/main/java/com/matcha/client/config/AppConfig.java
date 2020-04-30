package com.matcha.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static String serverPort;
    private static String serverAddress;

    public static String getServerPort() {
        return serverPort;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    @Autowired
    public AppConfig(@Value("${matcha.server.port}") String port,
                     @Value("${matcha.server.address}") String address) {
        serverAddress = address;
        serverPort = port;
    }
}
