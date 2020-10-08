package com.school21.server.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//Тесты упадут, если изменить секретный ключ в конфиге. Это не нормально.
@SpringBootTest
class JWTHelperTest {

    private final String validToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJzbWlnaHQiLCJpYXQiOjE2MDIxNTgwMTAsInN1YiI6InNlY3VyaXR5IiwiaXNzIjoic2VjdXJpdHlTZXJ2aWNlIiwiZXhwIjoxNjAzMTU4MDEwfQ.cpawkqfj85-hLW0oTmM-qJ6erzmZPeUkxEuWa7U81EA";
    private final long TTL = 1000000000;
    private final long EXPIRATION = 1603158010000L;
    private final long ISSUED_AT = 1602158010000L;

    @Autowired
    private JWTHelper jwtHelper;

    @Test
    void decodeInvalid() {
        assertThrows(Exception.class, ()-> jwtHelper.decodeJWT(null));
        assertThrows(Exception.class, ()-> jwtHelper.decodeJWT(validToken + "w"));
        assertThrows(Exception.class, ()-> jwtHelper.decodeJWT(""));
    }

    @Test
    void decodeValid() throws Exception {
        Claims claims = jwtHelper.decodeJWT(validToken);
        assertNotEquals(claims, null);
        assertEquals(claims.getExpiration().getTime(), EXPIRATION);
        assertEquals(claims.getIssuedAt().getTime(), ISSUED_AT);
        assertEquals(claims.getId(), "smight");
        assertEquals(claims.getSubject(), "security");
        assertEquals(claims.getIssuer(), "securityService");
    }

    @Test
    void getUserFromValidToken() throws Exception {
        assertEquals("smight", jwtHelper.getUserNameFromToken(validToken));
    }

    @Test
    void createJwtTimeFail() {
        assertNotEquals(jwtHelper.createJWT("smight", "securityService", "security", TTL), validToken);
        assertNotEquals(jwtHelper.createJWT("smight", "securityService", "security", TTL),
                        jwtHelper.createJWT("smight", "securityService", "security", TTL));
    }
}
