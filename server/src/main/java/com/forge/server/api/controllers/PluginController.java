package com.forge.server.api.controllers;

import com.forge.common.constants.ApiConstants;
import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.core.service.PluginService;
import com.forge.shared.model.request.PluginInstallRequest;
import com.forge.shared.model.response.PluginInstallResponse;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Plugin Controller
 * <p>
 * REST controller for plugin management endpoints.
 * Handles HTTP requests and delegates business logic to service layer.
 *
 * @author Forge Team
 */
@RestController
@RequestMapping(ApiConstants.API_PLUGINS_PATH)
public class PluginController {

    private static final Logger logger = LoggerFactory.getLogger(PluginController.class);

    private final PluginService pluginService;

    /**
     * Constructor for PluginController
     *
     * @param pluginService plugin service instance
     */
    public PluginController(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    /**
     * Installs a plugin
     *
     * @param request plugin installation request containing plugin name, JAR path, and class name
     * @return ResponseEntity containing PluginInstallResponse with plugin information
     */
    @PostMapping(ApiConstants.ENDPOINT_PLUGIN_INSTALL)
    public ResponseEntity<PluginInstallResponse> installPlugin(@Valid @RequestBody PluginInstallRequest request) {

        logger.info("Received plugin installation request: pluginName={}, jarPath={}, className={}",
                request.getPluginName(), request.getJarPath(), request.getClassName());

        try {
            PluginInstallResponse response = pluginService.installPlugin(request.getPluginName(), request.getJarPath(),
                    request.getClassName());

            logger.info("Plugin installation successful: pluginName={}", request.getPluginName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PluginException e) {
            logger.error("Plugin installation failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
