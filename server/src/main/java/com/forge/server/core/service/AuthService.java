package com.forge.server.core.service;

import com.forge.server.api.models.response.RegisterResponse;
import com.forge.server.core.entity.User;

import org.springframework.stereotype.Service;

/**
 * Authentication Service
 * <p>
 * Service responsible for authentication-related operations.
 * Acts as a facade between the API layer and business logic layer.
 * Follows the Facade pattern and Single Responsibility Principle.
 *
 * @author Forge Team
 */
@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user
     * <p>
     * Delegates to UserServiceInterface and converts entity to response DTO.
     *
     * @param username the username
     * @param email    the email address
     * @param password the plain text password
     * @return RegisterResponse containing user information
     */
    public RegisterResponse register(String username, String email, String password) {
        User user = userService.registerUser(username, email, password);
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setCreatedAt(user.getCreatedAt());
        response.setMessage("User registered successfully");
        return response;
    }
}

