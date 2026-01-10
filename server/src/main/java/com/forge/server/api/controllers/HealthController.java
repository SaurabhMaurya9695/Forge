package com.forge.server.api.controllers;

import com.forge.common.constants.ApiConstants;
import com.forge.common.constants.EntityConstants;
import com.forge.common.constants.MessageConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * <p>
 * Provides basic health check endpoint for the application.
 * This is a simple example controller following REST principles.
 *
 * @author Forge Team
 */
@RestController
@RequestMapping(ApiConstants.API_HEALTH_PATH)
public class HealthController {

    /**
     * Health check endpoint
     *
     * @return Health status with timestamp
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", MessageConstants.STATUS_UP);
        response.put("service", EntityConstants.SERVICE_NAME_FORGE_SERVER);
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}

