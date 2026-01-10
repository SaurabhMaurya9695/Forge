package com.forge.server.api.models.request;

import com.forge.common.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Registration Request DTO
 * <p>
 * Data Transfer Object for user registration requests.
 * Contains validation annotations for input validation.
 *
 * @author Forge Team
 */
public class RegisterRequest {

    @NotBlank(message = ValidationConstants.VALIDATION_USERNAME_REQUIRED)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = ValidationConstants.VALIDATION_EMAIL_REQUIRED)
    @Email(message = ValidationConstants.VALIDATION_EMAIL_INVALID)
    private String email;

    @NotBlank(message = ValidationConstants.VALIDATION_PASSWORD_REQUIRED)
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

