package com.security;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class SecurityHelper {
    public static String generateHash() {
        byte[] array = new byte[40];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
