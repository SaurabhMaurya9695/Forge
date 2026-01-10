package com.forge.server.api.controllers;

import com.forge.common.constants.ApiConstants;
import com.forge.common.constants.MessageConstants;
import com.forge.shared.model.request.LoginRequest;
import com.forge.shared.model.request.RegisterRequest;
import com.forge.shared.model.response.LoginResponse;
import com.forge.shared.model.response.RegisterResponse;
import com.forge.server.core.service.AuthService;
import com.forge.server.security.util.AppUserDetails;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping(ApiConstants.ENDPOINT_SECURE_TEST)
    public ResponseEntity<Map<String, Object>> secureTest() {
        // Get authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get user details
        AppUserDetails userDetails = null;
        if (authentication != null && authentication.getPrincipal() instanceof AppUserDetails) {
            userDetails = (AppUserDetails) authentication.getPrincipal();
        }

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put(MessageConstants.STATUS, MessageConstants.STATUS_UP);
        response.put(MessageConstants.TIMESTAMP, LocalDateTime.now());
        response.put(MessageConstants.MESSAGE, "Secure endpoint accessed successfully");

        if (userDetails != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", userDetails.getId());
            userInfo.put("email", userDetails.getEmail());
            userInfo.put("username", userDetails.getUsername());
            userInfo.put("authorities",
                    userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).toList());
            response.put("user", userInfo);
        } else {
            response.put("user", "Unable to retrieve user details");
        }

        response.put("authenticated", authentication != null && authentication.isAuthenticated());

        return ResponseEntity.ok(response);
    }
}

