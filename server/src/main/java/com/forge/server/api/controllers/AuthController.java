package com.forge.server.api.controllers;

import com.forge.common.constants.ApiConstants;
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
@RequestMapping(ApiConstants.API_AUTH_PATH)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user
     *
     * @param request registration request containing username, email, password
     * @return registration response with user details
     */
    @PostMapping(ApiConstants.ENDPOINT_REGISTER)
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
    @PostMapping(ApiConstants.ENDPOINT_LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}

