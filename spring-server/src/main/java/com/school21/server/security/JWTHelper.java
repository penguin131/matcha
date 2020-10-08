package com.school21.server.security;


import com.school21.server.utils.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * Работа с JWT
 * Структура:
 * Заголовок, полезная нагрузка, подпись
 */
@Component
public class JWTHelper {
    private final static Logger logger = Logger.getLogger(JWTHelper.class);

    private String SECRET_KEY;

    public JWTHelper(@Autowired Config config) {
        SECRET_KEY = config.getJwtKey();
    }

    public String createJWT(String id, String issuer, String subject, long ttlMillis) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.nanoTime();
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

    public Claims decodeJWT(String jwt) throws Exception {
        if (jwt == null) {
            throw new Exception("jwt string is null.");
        }
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }

    public String getUserNameFromToken(String jwt) throws Exception {
        logger.info(String.format("getUserNameFromToken(%s)", jwt));
        return decodeJWT(jwt).getId();
    }
}
