package com.forge.server.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import com.forge.common.constants.JwtConstants;
import com.forge.server.security.config.JwtConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token Provider
 * <p>
 * Handles JWT token generation, validation, and claims extraction.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    /**
     * Constructor for JwtTokenProvider
     *
     * @param jwtConfig JWT configuration
     * @throws IllegalStateException if JWT secret is null or empty, or if key generation fails
     */
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        String secret = jwtConfig.getSecret();
        
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("JWT secret cannot be null or empty. Please configure app.jwt.secret in application.yml");
        }
        
        // Ensure secret is at least 32 bytes (256 bits) for HMAC-SHA256
        // If shorter, hash it to meet the requirement
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            // Use SHA-256 to hash the secret and ensure it's exactly 32 bytes
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                secretBytes = digest.digest(secretBytes);
                logger.warn("JWT secret was shorter than 32 bytes. It has been hashed to meet the requirement.");
            } catch (java.security.NoSuchAlgorithmException e) {
                logger.error("Failed to hash JWT secret: {}", e.getMessage());
                throw new IllegalStateException("Failed to initialize JWT secret key", e);
            }
        }
        
        try {
            this.secretKey = Keys.hmacShaKeyFor(secretBytes);
        } catch (Exception e) {
            logger.error("Failed to create JWT secret key: {}", e.getMessage());
            throw new IllegalStateException("Failed to create JWT secret key. Please ensure app.jwt.secret is properly configured.", e);
        }
    }

    /**
     * Generates JWT token for user
     *
     * @param id         user ID
     * @param email      user email
     * @param name       user name
     * @return JWT token string
     */
    public String generateToken(String id, String email, String name) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationMs());

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.CLAIM_EMAIL, email);
        claims.put(JwtConstants.CLAIM_NAME, name);

        return Jwts.builder()
                .subject(id)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Generates refresh token
     *
     * @param id user ID
     * @return refresh token string
     */
    public String generateRefreshToken(String id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getRefreshExpirationMs());

        return Jwts.builder()
                .subject(id)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extracts user ID from token
     *
     * @param token JWT token
     * @return user ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * Extracts email from token
     *
     * @param token JWT token
     * @return user email
     */
    public String getEmailFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get(JwtConstants.CLAIM_EMAIL, String.class);
    }

    /**
     * Extracts name from token
     *
     * @param token JWT token
     * @return user name
     */
    public String getNameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get(JwtConstants.CLAIM_NAME, String.class);
    }

    /**
     * Validates JWT token
     *
     * @param token JWT token to validate
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SignatureException ex) {
            logger.warn(JwtConstants.LOG_INVALID_SIGNATURE, ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.warn(JwtConstants.LOG_INVALID_TOKEN, ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.warn(JwtConstants.LOG_EXPIRED_TOKEN, ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.warn(JwtConstants.LOG_UNSUPPORTED_TOKEN, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.warn(JwtConstants.LOG_EMPTY_CLAIMS, ex.getMessage());
        }
        return false;
    }

    /**
     * Checks if token is expired
     *
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    /**
     * Extracts claims from token
     *
     * @param token JWT token
     * @return token claims
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
