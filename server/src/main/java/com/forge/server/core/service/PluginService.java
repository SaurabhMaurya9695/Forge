package com.forge.server.core.service;

import com.forge.server.core.service.plugin.PluginInstallationService;
import com.forge.shared.model.response.PluginInstallResponse;

import org.springframework.stereotype.Service;

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

    public PluginInstallResponse installPlugin(String pluginName) {
        return pluginInstallationService.installPlugin(pluginName);
    }
}

