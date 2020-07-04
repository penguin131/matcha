package com.security;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import com.helper.Config;
import io.jsonwebtoken.*;
import java.util.Date;
import java.util.Properties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

/**
 * Работа с JWT
 * Структура:
 * Заголовок, полезная нагрузка, подпись
 */
public class JWTHelper {

    private static String SECRET_KEY;
    static {
        try {
            File conf = Config.getConfig();
            InputStream input = new FileInputStream(conf);
            Properties props = new Properties();
            props.load(input);
            SECRET_KEY = props.getProperty("JWT.key");
        } catch (Exception ex) {
            //todo log
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

    public static Claims decodeJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }
}
