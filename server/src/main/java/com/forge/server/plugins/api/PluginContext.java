package com.forge.server.plugins.api;

import java.util.Optional;

public interface PluginContext {

    String getPluginName();

    Optional<String> getConfig(String key);

    default String getConfig(String key, String defaultValue) {
        return getConfig(key).orElse(defaultValue);
    }

    org.slf4j.Logger getLogger();
}
