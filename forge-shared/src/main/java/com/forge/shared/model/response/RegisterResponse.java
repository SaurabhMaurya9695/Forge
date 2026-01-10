package com.forge.shared.model.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Registration Response DTO
 * <p>
 * Data Transfer Object for user registration responses.
 * Contains user information without sensitive data like password.
 * <p>
 * This is a shared model that can be used across modules.
 *
 * @author Forge Team
 */
public class RegisterResponse {

    private UUID id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(UUID id, String username, String email, String role, LocalDateTime createdAt,
            String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

