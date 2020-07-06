package com.security;

import com.service.DatabaseService;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class SecurityHelper {
    private final static Logger logger = Logger.getLogger(DatabaseService.class);
    public static String generateHash() {
        byte[] array = new byte[40];
        new Random().nextBytes(array);
        String hash = new String(array, StandardCharsets.UTF_8);
        logger.info("generateHash(): " + hash);
        return hash;
    }
}
