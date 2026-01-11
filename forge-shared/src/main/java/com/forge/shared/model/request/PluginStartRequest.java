package com.forge.shared.model.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public class PluginStartRequest {

    @NotEmpty(message = "Configuration cannot be empty")
    private Map<String, String> config;

    public PluginStartRequest() {
    }

    public PluginStartRequest(Map<String, String> config) {
        this.config = config;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
