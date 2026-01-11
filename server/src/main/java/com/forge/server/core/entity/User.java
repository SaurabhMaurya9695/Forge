package com.forge.server.core.entity;

import com.forge.common.constants.ValidationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User Entity
 * <p>
 * Represents a user in the system with authentication and authorization information.
 * This entity is mapped to the 'users' table in the database.
 *
 * @author Forge Team
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email") }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = ValidationConstants.VALIDATION_USERNAME_REQUIRED)
    @Size(min = 3, max = 50, message = ValidationConstants.VALIDATION_USERNAME_LENGTH_REQUIRED)
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = ValidationConstants.VALIDATION_EMAIL_REQUIRED)
    @Email(message = ValidationConstants.VALIDATION_EMAIL_INVALID)
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = ValidationConstants.VALIDATION_PASSWORD_REQUIRED)
    @Column(name = "password", nullable = false)
    private String passwordHash;

    @Column(name = "role", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private UserRole role ;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Note: Role should be set by service layer, not in entity
        // This is kept for backward compatibility but service layer should set it
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Default constructor
    public User() {
    }

    // Constructor for creating new users
    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * User Role Enumeration
     */
    public enum UserRole {
        ADMIN, DEVELOPER, VIEWER
    }
}

