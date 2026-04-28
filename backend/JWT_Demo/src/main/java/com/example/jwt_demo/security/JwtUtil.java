package com.example.jwt_demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    // Initializes the key after the class is instantiated and the jwtSecret is injected,
    // preventing the repeated creation of the key and enhancing performance
    private SecretKey key;
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    //Generate Token

    public String generateUserToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", List.of("ROLE_USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpirationMs))
                .signWith(key)
                .compact();
    }
    public String generateGuestToken() {
        return Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("roles", List.of("ROLE_GUEST"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> roles= claims.get("roles", List.class);

        return roles.stream()
                .map(Object::toString)
                .toList();

    }

    // Validate Token

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }
        catch (SecurityException e){
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        catch (MalformedJwtException e){
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e){
            logger.error("Expired JWT token: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing Authorization header");
        }

        String token = authHeader.substring(7);

        if (token.isBlank()) {
            throw new IllegalArgumentException("Empty JWT token");
        }

        return token;
    }


}
