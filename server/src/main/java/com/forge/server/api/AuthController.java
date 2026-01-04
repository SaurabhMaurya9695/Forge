package com.forge.server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 *
 * @author Forge Team
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Register a new user
     *
     * @param request Registration request containing username, email, password, etc.
     * @return Registration response with user details and token
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registration endpoint - to be implemented");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate user and generate JWT token
     *
     * @param request Login request containing username/email and password
     * @return Login response with JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User login endpoint - to be implemented");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}

