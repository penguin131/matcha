package com.security;

import com.service.DatabaseService;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class SecurityHelper {
    private final static Logger logger = Logger.getLogger(DatabaseService.class);
    public static String generateHash() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        logger.info("generateHash(): " + generatedString);
        return generatedString;
    }
}
