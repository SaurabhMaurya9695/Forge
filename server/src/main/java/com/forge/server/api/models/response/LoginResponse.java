package com.forge.server.api.models.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Login Response DTO
 * <p>
 * Data Transfer Object for login responses.
 * Contains user information and JWT tokens.
 */
public class LoginResponse {

    private UUID id;
    private String username;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
    private LocalDateTime loginTime;

    public LoginResponse() {
        this.tokenType = "Bearer";
    }

    public LoginResponse(UUID id, String username, String email, String role, String accessToken, String refreshToken,
            long expiresIn) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
        this.loginTime = LocalDateTime.now();
    }

    /**
     * Gets user ID
     *
     * @return user ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets user ID
     *
     * @param id user ID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Gets username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets user email
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user email
     *
     * @param email email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets user role
     *
     * @return user role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets user role
     *
     * @param role user role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets access token
     *
     * @return JWT access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets access token
     *
     * @param accessToken JWT access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Gets refresh token
     *
     * @return JWT refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets refresh token
     *
     * @param refreshToken JWT refresh token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Gets token type
     *
     * @return token type (e.g., "Bearer")
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Sets token type
     *
     * @param tokenType token type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Gets token expiration time in seconds
     *
     * @return expiration time in seconds
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Sets token expiration time in seconds
     *
     * @param expiresIn expiration time in seconds
     */
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Gets login timestamp
     *
     * @return login time
     */
    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    /**
     * Sets login timestamp
     *
     * @param loginTime login time
     */
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}

