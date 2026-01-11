package com.forge.server.core.service.plugin;

import com.forge.server.common.plugin.exception.PluginException;
import com.forge.server.plugins.PluginManager;
import com.forge.server.plugins.api.Plugin;
import com.forge.server.plugins.api.PluginState;
import com.forge.shared.model.response.PluginInstallResponse;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PluginInstallationService {

    private static final String PLUGIN_INSTALLED_SUCCESSFULLY = "Plugin installed successfully";
    private static final String PLUGIN_ALREADY_SUCCESSFULLY = "Plugin already installed: ";
    private static final String ERROR_PLUGIN_ALREADY_INSTALLED = "Plugin '%s' is already installed";
    private static final String PLUGIN_INSTALLATION_FAILED = "Plugin installation failed: ";
    private static final String ERROR_PLUGIN_INSTALLATION_FAILED = PLUGIN_INSTALLATION_FAILED + " %s";
    private static final String ERROR_DURING_PLUGIN_INSTALLATION = "I/O error during plugin installation: ";
    private static final String INSTALLING_PLUGIN = "Installing plugin: ";

    private static final Logger logger = Logger.getLogger(PluginInstallationService.class.getName());

    private final PluginManager pluginManager;

    public PluginInstallationService(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public PluginInstallResponse installPlugin(String pluginName) {
        logger.info(INSTALLING_PLUGIN + pluginName);
        try {
            Plugin existingPlugin = pluginManager.getPlugin(pluginName);
            if (existingPlugin != null) {
                logger.warning(PLUGIN_ALREADY_SUCCESSFULLY + pluginName);
                throw new PluginException(String.format(ERROR_PLUGIN_ALREADY_INSTALLED, pluginName));
            }

            Plugin installedPlugin;
            try {
                installedPlugin = pluginManager.installPlugin(pluginName);
            } catch (IOException e) {
                logger.log(Level.SEVERE, ERROR_DURING_PLUGIN_INSTALLATION + pluginName + " - " + e.getMessage(), e);
                throw new PluginException(
                        String.format(ERROR_PLUGIN_INSTALLATION_FAILED, "I/O error: " + e.getMessage()), e);
            }

            PluginInstallResponse response = new PluginInstallResponse();
            response.setPluginName(installedPlugin.getName());
            response.setVersion(installedPlugin.getVersion());
            response.setState(
                    installedPlugin.getState() != null ? installedPlugin.getState().name() : PluginState.LOADED.name());
            response.setMessage(PLUGIN_INSTALLED_SUCCESSFULLY);
            logger.info(PLUGIN_INSTALLED_SUCCESSFULLY + ": " + pluginName);
            return response;
        } catch (PluginException e) {
            logger.log(Level.SEVERE, PLUGIN_INSTALLATION_FAILED + pluginName + " - " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, PLUGIN_INSTALLATION_FAILED + pluginName + " - " + e.getMessage(), e);
            throw new PluginException(String.format(ERROR_PLUGIN_INSTALLATION_FAILED, e.getMessage()), e);
        }
    }
}

