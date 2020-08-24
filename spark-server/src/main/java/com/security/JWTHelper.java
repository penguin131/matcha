package com.security;

import com.helper.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.util.Date;
import java.util.Properties;

/**
 * Работа с JWT
 * Структура:
 * Заголовок, полезная нагрузка, подпись
 */
public class JWTHelper {
    private final static Logger logger = Logger.getLogger(JWTHelper.class);

    private static String SECRET_KEY;
    static {
        try {
            SECRET_KEY = Config.getConfig().getProperty("JWT.key");
        } catch (Exception ex) {
            logger.info("Read config exception: " + ex.getMessage());
        }
    }

    /**
     *
     * @param id - user
     * Optional parameters:
     * @param issuer - application name
     * @param subject - application subject
     * @param ttlMillis time to live
     * @return token hash
     */
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) throws Exception {
        logger.info("decodeJWT() jwt string: " + jwt);
        if (jwt == null) {
            throw new Exception("jwt string is null.");
        }
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }

    public static String getUserNameFromToken(String jwt) throws Exception {
        return JWTHelper.decodeJWT(jwt).getId();
    }
}
