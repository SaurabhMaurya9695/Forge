package com.forge.server.core.service;

import com.forge.server.core.service.plugin.PluginInstallationService;
import com.forge.shared.model.response.PluginInstallResponse;

import org.springframework.stereotype.Service;

/**
 *
 * @author Forge Team
 */
@Service
public class PluginService {

    private final PluginInstallationService pluginInstallationService;

    /**
     * Constructor for PluginService
     *
     * @param pluginInstallationService plugin installation service
     */
    public PluginService(PluginInstallationService pluginInstallationService) {
        this.pluginInstallationService = pluginInstallationService;
    }

    /**
     * Installs a plugin
     * Delegates to PluginInstallationService
     *
     * @param pluginName name of the plugin
     * @param jarPath    path to the plugin JAR file
     * @param className  fully qualified class name of the plugin implementation
     * @return PluginInstallResponse containing plugin information
     */
    public PluginInstallResponse installPlugin(String pluginName, String jarPath, String className) {
        return pluginInstallationService.installPlugin(pluginName, jarPath, className);
    }
}

