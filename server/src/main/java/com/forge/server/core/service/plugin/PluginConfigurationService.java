package com.forge.server.core.service.plugin;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class PluginConfigurationService {

    private static final Logger logger = Logger.getLogger(PluginConfigurationService.class.getName());
    private static final String CONFIG_DIR = "plugins/config";
    private static final String CONFIG_FILE_EXTENSION = ".properties";
    private static final String SAVING_CONFIGURATION_FOR_PLUGIN = "Saving configuration for plugin: ";
    private static final String CONFIGURATION_SAVED_SUCCESSFULLY = "Configuration saved successfully for plugin: ";
    private static final String ERROR_SAVING_CONFIGURATION = "Error saving configuration for plugin: ";
    private static final String LOADING_CONFIGURATION_FOR_PLUGIN = "Loading configuration for plugin: ";
    private static final String CONFIGURATION_LOADED_SUCCESSFULLY = "Configuration loaded successfully for plugin: ";
    private static final String ERROR_LOADING_CONFIGURATION = "Error loading configuration for plugin: ";
    private static final String NO_CONFIGURATION_FILE_FOUND_FOR_PLUGIN = "No configuration file found for plugin: ";
    private static final String RELOADING_CONFIGURATION_FOR_PLUGIN = "Reloading configuration for plugin: ";

    public void savePluginConfiguration(String pluginName, Map<String, String> config) throws IOException {
        logger.info(SAVING_CONFIGURATION_FOR_PLUGIN + pluginName);
        
        Path configDir = Paths.get(CONFIG_DIR);
        if (!Files.exists(configDir)) {
            Files.createDirectories(configDir);
        }

        Path configFile = configDir.resolve(pluginName.toLowerCase() + CONFIG_FILE_EXTENSION);
        Properties properties = new Properties();
        properties.putAll(config);
        
        try {
            properties.store(Files.newOutputStream(configFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING), 
                           "Plugin configuration for " + pluginName);
            logger.info(CONFIGURATION_SAVED_SUCCESSFULLY + pluginName);
        } catch (IOException e) {
            logger.severe(ERROR_SAVING_CONFIGURATION + pluginName + " - " + e.getMessage());
            throw e;
        }
    }

    public Map<String, String> loadPluginConfiguration(String pluginName) {
        logger.info(LOADING_CONFIGURATION_FOR_PLUGIN + pluginName);

        Path configFile = Paths.get(CONFIG_DIR, pluginName.toLowerCase() + CONFIG_FILE_EXTENSION);
        if (!Files.exists(configFile)) {
            logger.info(NO_CONFIGURATION_FILE_FOUND_FOR_PLUGIN + pluginName);
            return new HashMap<>();
        }

        Properties properties = new Properties();
        try (var inputStream = Files.newInputStream(configFile)) {
            properties.load(inputStream);
            Map<String, String> config = new HashMap<>();
            for (String key : properties.stringPropertyNames()) {
                config.put(key, properties.getProperty(key));
            }
            logger.info(CONFIGURATION_LOADED_SUCCESSFULLY + pluginName);
            return config;
        } catch (IOException e) {
            logger.warning(ERROR_LOADING_CONFIGURATION + pluginName + " - " + e.getMessage());
            return new HashMap<>();
        }
    }

    public void reloadPluginConfiguration(String pluginName) {
        logger.info(RELOADING_CONFIGURATION_FOR_PLUGIN + pluginName);
        loadPluginConfiguration(pluginName);
    }
}

