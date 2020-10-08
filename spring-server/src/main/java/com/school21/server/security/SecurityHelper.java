package com.school21.server.security;

import org.apache.log4j.Logger;

import java.util.Random;

public class SecurityHelper {
    private final static Logger logger = Logger.getLogger(SecurityHelper.class);
    public static String generateHash() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 40;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        logger.info("generateHash(): " + generatedString);
        return generatedString;
    }
}
