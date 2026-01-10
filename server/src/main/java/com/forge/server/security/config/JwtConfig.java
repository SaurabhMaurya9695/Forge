package com.forge.server.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration Properties
 * <p>
 * Loads JWT-related configuration from application properties.
 * Configure via: app.jwt.secret, app.jwt.expiration-ms, etc.
 */
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {

    private String secret = "ForgeCiCd@2026";
    private long expirationMs = 86400000; // 24 hours
    private long refreshExpirationMs = 604800000; // 7 days
    private String tokenPrefix = "Bearer ";
    private String headerName = "Authorization";

    /**
     * Gets the JWT secret key
     *
     * @return JWT secret key
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets the JWT secret key
     *
     * @param secret JWT secret key
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Gets token expiration time in milliseconds
     *
     * @return expiration time in ms
     */
    public long getExpirationMs() {
        return expirationMs;
    }

    /**
     * Sets token expiration time in milliseconds
     *
     * @param expirationMs expiration time in ms
     */
    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }

    /**
     * Gets refresh token expiration time in milliseconds
     *
     * @return refresh expiration time in ms
     */
    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    /**
     * Sets refresh token expiration time in milliseconds
     *
     * @param refreshExpirationMs refresh expiration time in ms
     */
    public void setRefreshExpirationMs(long refreshExpirationMs) {
        this.refreshExpirationMs = refreshExpirationMs;
    }

    /**
     * Gets the token prefix (e.g., "Bearer ")
     *
     * @return token prefix
     */
    public String getTokenPrefix() {
        return tokenPrefix;
    }

    /**
     * Sets the token prefix
     *
     * @param tokenPrefix token prefix
     */
    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    /**
     * Gets the header name for JWT token
     *
     * @return header name
     */
    public String getHeaderName() {
        return headerName;
    }

    /**
     * Sets the header name for JWT token
     *
     * @param headerName header name
     */
    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }
}
