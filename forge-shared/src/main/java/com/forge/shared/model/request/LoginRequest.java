package com.forge.shared.model.request;

import com.forge.common.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Login Request DTO
 * <p>
 * Data Transfer Object for user login requests.
 * Contains validation annotations for input validation.
 * <p>
 * This is a shared model that can be used across modules.
 *
 * @author Forge Team
 */
public class LoginRequest {

    @NotBlank(message = ValidationConstants.VALIDATION_EMAIL_REQUIRED)
    @Email(message = ValidationConstants.VALIDATION_EMAIL_INVALID)
    private String email;

    @NotBlank(message = ValidationConstants.VALIDATION_PASSWORD_REQUIRED)
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets user email
     *
     * @return email address
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets user password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

