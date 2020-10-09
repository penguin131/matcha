package com.school21.server.security;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SecurityHelper {
    private final static Logger logger = Logger.getLogger(SecurityHelper.class);
    public static String generateHash(int hashSize) {
        int leftLimit = 48;
        int rightLimit = 122;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(hashSize)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        logger.info("generateHash(): " + generatedString);
        return generatedString;
    }
}
