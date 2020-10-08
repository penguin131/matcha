package com.school21.server.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Config {

    private String jwtKey;

    public String getJwtKey() {
        return jwtKey;
    }

    public Config(@Autowired Environment env) {
        this.jwtKey = env.getProperty("jwt.key");
    }
}
