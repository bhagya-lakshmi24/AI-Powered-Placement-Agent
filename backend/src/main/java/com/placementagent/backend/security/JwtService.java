
package com.placementagent.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

        private final SecretKey secretKey;

        public JwtService(@Value("${app.jwt.secret}") String secret) {
                this.secretKey = Keys.hmacShaKeyFor(
                                secret.getBytes(StandardCharsets.UTF_8));
        }

        public String generateToken(String email) {
                return Jwts.builder()
                                .subject(email)
                                .issuedAt(new Date())
                                .expiration(new Date(System.currentTimeMillis() + 86400000))
                                .signWith(secretKey)
                                .compact();
        }

        public String extractEmail(String token) {
                return Jwts.parser()
                                .verifyWith(secretKey)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .getSubject();
        }

        public boolean isTokenValid(String token) {
                try {
                        extractEmail(token);
                        return true;
                } catch (JwtException | IllegalArgumentException e) {
                        return false;
                }
        }
}
