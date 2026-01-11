package com.forge.server.plugins.api;

import java.util.Optional;
import java.util.logging.Logger;

public interface PluginContext {

    String getPluginName();

    Optional<String> getConfig(String key);

    default String getConfig(String key, String defaultValue) {
        return getConfig(key).orElse(defaultValue);
    }

    Logger getLogger();
}
