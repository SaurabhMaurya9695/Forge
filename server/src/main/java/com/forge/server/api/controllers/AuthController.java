package com.forge.server.api.controllers;

import com.forge.server.api.models.request.LoginRequest;
import com.forge.server.api.models.request.RegisterRequest;
import com.forge.server.api.models.response.LoginResponse;
import com.forge.server.api.models.response.RegisterResponse;
import com.forge.server.core.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 * <p>
 * REST controller for authentication endpoints.
 * Handles HTTP requests and delegates business logic to service layer.
 *
 * @author Forge Team
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user
     * <p>
     * Endpoint: POST /api/auth/register
     * Validates the request and creates a new user account.
     *
     * @param request Registration request containing username, email, password
     * @return Registration response with user details
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request.getUsername(), request.getEmail(),
                request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates user and returns JWT tokens
     *
     * @param request login request containing email and password
     * @return login response with user details and JWT tokens
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}

