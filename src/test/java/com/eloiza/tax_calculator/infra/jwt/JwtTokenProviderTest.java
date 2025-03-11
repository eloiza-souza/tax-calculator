package com.eloiza.tax_calculator.infra.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private String jwtSecret;
    private long jwtExpiration;

    @BeforeEach
    void setUp() {
        jwtSecret = "mysecretkeymysecretkeymysecretkeymysecretkey"; // 256-bit key
        jwtExpiration = 3600000L; // 1 hora em milissegundos
        jwtTokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiration);
    }

    @Test
    void generateAccessToken_success() {
        Authentication authentication = createAuthentication("testuser", "ROLE_USER");

        String token = jwtTokenProvider.generateAccessToken(authentication);

        assertNotNull(token, "O token não deve ser nulo");
        assertFalse(token.isEmpty(), "O token não deve estar vazio");
    }

    @Test
    void getUsername_success() {
        Authentication authentication = createAuthentication("testuser", "ROLE_USER");

        String token = jwtTokenProvider.generateAccessToken(authentication);
        String username = jwtTokenProvider.getUsername(token);

        assertEquals("testuser", username, "O nome de usuário deve corresponder ao do token");
    }

    @Test
    void validateToken_success() {
        Authentication authentication = createAuthentication("testuser", "ROLE_USER");

        String token = jwtTokenProvider.generateAccessToken(authentication);
        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid, "O token deve ser válido");
    }

    @Test
    void validateToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertFalse(isValid, "O token deve ser inválido");
    }

    @Test
    void isTokenExpired_ExpiredToken() {
        String expiredToken = createExpiredToken("testuser");

        boolean isExpired = jwtTokenProvider.isTokenExpired(expiredToken);

        assertTrue(isExpired, "O token deve estar expirado");
    }

    @Test
    void isTokenExpired_ValidToken() {
        Authentication authentication = createAuthentication("testuser", "ROLE_USER");

        String token = jwtTokenProvider.generateAccessToken(authentication);
        boolean isExpired = jwtTokenProvider.isTokenExpired(token);

        assertFalse(isExpired, "O token não deve estar expirado");
    }

    @Test
    void getUsername_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(io.jsonwebtoken.JwtException.class,
                () -> jwtTokenProvider.getUsername(invalidToken),
                "Deve lançar JwtException para um token inválido");
    }

    @Test
    void validateToken_ExpiredToken() {
        String expiredToken = createExpiredToken("testuser");

        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        assertFalse(isValid, "O token expirado deve ser inválido");
    }

    // Métodos auxiliares
    private Authentication createAuthentication(String username, String role) {
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    private String createExpiredToken(String username) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired 1 second ago
                .signWith(key)
                .compact();
    }
}