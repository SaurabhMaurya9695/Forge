package com.forge.server.core.service;

import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.core.service.plugin.PluginConfigurationService;
import com.forge.server.core.service.plugin.PluginInstallationService;
import com.forge.server.plugins.PluginManager;
import com.forge.server.plugins.api.Plugin;
import com.forge.shared.model.response.PluginInstallResponse;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PluginService {

    private static final Logger logger = Logger.getLogger(PluginService.class.getName());
    private static final String STARTING_PLUGIN = "Starting plugin: ";
    private static final String PLUGIN_STARTED_SUCCESSFULLY = "Plugin started successfully: ";
    private static final String PLUGIN_START_FAILED = "Plugin start failed: ";
    private static final String RELOADING_CONFIGURATION = "Reloading configuration for plugin: ";

    private final PluginInstallationService pluginInstallationService;
    private final PluginManager pluginManager;
    private final PluginConfigurationService configurationService;

    public PluginService(PluginInstallationService pluginInstallationService, PluginManager pluginManager,
            PluginConfigurationService configurationService) {
        this.pluginInstallationService = pluginInstallationService;
        this.pluginManager = pluginManager;
        this.configurationService = configurationService;
    }

    public PluginInstallResponse installPlugin(String pluginName) {
        return pluginInstallationService.installPlugin(pluginName);
    }

    public PluginInstallResponse startPlugin(String pluginName, Map<String, String> config)
            throws PluginException, IOException {
        logger.info(STARTING_PLUGIN + pluginName);

        try {
            configurationService.savePluginConfiguration(pluginName, config);
            logger.info(RELOADING_CONFIGURATION + pluginName);
            pluginManager.reloadPluginConfiguration(pluginName);

            pluginManager.startPlugin(pluginName);

            Plugin plugin = pluginManager.getPlugin(pluginName);
            PluginInstallResponse response = new PluginInstallResponse();
            response.setPluginName(plugin.getName());
            response.setVersion(plugin.getVersion());
            response.setState(plugin.getState().name());
            response.setMessage(PLUGIN_STARTED_SUCCESSFULLY + pluginName);
            return response;
        } catch (PluginException | IOException e) {
            logger.log(Level.SEVERE, PLUGIN_START_FAILED + pluginName + " - " + e.getMessage(), e);
            throw e;
        }
    }
}

