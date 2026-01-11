package com.forge.server.api.controllers;

import com.forge.common.constants.ApiConstants;
import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.core.service.PluginService;
import com.forge.shared.model.request.PluginInstallRequest;
import com.forge.shared.model.request.PluginStartRequest;
import com.forge.shared.model.response.PluginInstallResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    private static final String RECEIVED_PLUGIN_START_REQUEST = "Received plugin start request: ";
    private static final String PLUGIN_START_SUCCESSFUL = "Plugin start successful: ";
    private static final String PLUGIN_START_FAILED = "Plugin start failed: ";
    private static final String FAILED_TO_SAVE_PLUGIN_CONFIGURATION = "Failed to save plugin configuration: ";

    private final PluginService pluginService;

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

    @PostMapping(ApiConstants.ENDPOINT_PLUGIN_START + "/{pluginName}")
    public ResponseEntity<PluginInstallResponse> startPlugin(@PathVariable String pluginName,
            @Valid @RequestBody PluginStartRequest request) {
        logger.info(RECEIVED_PLUGIN_START_REQUEST + pluginName);
        try {
            PluginInstallResponse response = pluginService.startPlugin(pluginName, request.getConfig());
            logger.info(PLUGIN_START_SUCCESSFUL + pluginName);
            return ResponseEntity.ok(response);
        } catch (PluginException e) {
            logger.log(Level.SEVERE, PLUGIN_START_FAILED + e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            logger.log(Level.SEVERE, PLUGIN_START_FAILED + e.getMessage(), e);
            throw new PluginException(FAILED_TO_SAVE_PLUGIN_CONFIGURATION + e.getMessage(), e);
        }
    }
}
