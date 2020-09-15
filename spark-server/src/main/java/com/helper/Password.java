package com.helper;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Random;

public class Password {
    private static final Random RANDOM = new SecureRandom();
    private static final int iterations = 20*1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;
    private static Logger logger = Logger.getLogger(Password.class);

    public static String getSaltedHash(String password) throws Exception {
        logger.info(String.format("==>  getSaltedHash(%s)", password));
        byte[] salt = new byte[saltLen];
        RANDOM.nextBytes(salt);
        logger.info("salt: " + new String(salt));
        String hash = Base64.encodeBase64String(salt) + "$" + hash(password, salt);
        logger.info("<==    getSaltedHash(): " + hash);
        return hash;
    }

    //password в изначальном виде, stored хешированный
    public static boolean check(String password, String stored) throws Exception{
        String[] saltAndHash = stored.split("\\$");
        if (saltAndHash.length != 2) {
            throw new IllegalStateException(
                    "The stored password must have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndHash[0]));
        return hashOfInput.equals(saltAndHash[1]);
    }

    private static String hash(String password, byte[] salt) throws Exception {
        logger.info(String.format("==>  hash(%s, salt)", password));
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, iterations, desiredKeyLen));
        String res = Base64.encodeBase64String(key.getEncoded());
        logger.info("<==    hash(): " + res);
        return res;
    }
}
