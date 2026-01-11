package com.forge.server.api.controllers;

import com.forge.common.constants.ApiConstants;
import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.core.service.PluginService;
import com.forge.shared.model.request.PluginInstallRequest;
import com.forge.shared.model.response.PluginInstallResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.API_PLUGINS_PATH)
public class PluginController {

    private static final Logger logger = Logger.getLogger(PluginController.class.getName());
    private static final String RECEIVED_PLUGIN_INSTALLATION_REQUEST = "Received plugin installation request: ";
    private static final String PLUGIN_INSTALLATION_SUCCESSFUL = "Plugin installation successful: ";
    private static final String PLUGIN_INSTALLATION_FAILED = "Plugin installation failed: ";

    private final PluginService pluginService;

    /**
     * Constructor for PluginController
     *
     * @param pluginService plugin service instance
     */
    public PluginController(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @PostMapping(ApiConstants.ENDPOINT_PLUGIN_INSTALL)
    public ResponseEntity<PluginInstallResponse> installPlugin(@Valid @RequestBody PluginInstallRequest request) {
        logger.info(RECEIVED_PLUGIN_INSTALLATION_REQUEST + request.getPluginName());
        try {
            PluginInstallResponse response = pluginService.installPlugin(request.getPluginName());
            logger.info(PLUGIN_INSTALLATION_SUCCESSFUL + request.getPluginName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PluginException e) {
            logger.log(Level.SEVERE, PLUGIN_INSTALLATION_FAILED + e.getMessage(), e);
            throw e;
        }
    }
}
