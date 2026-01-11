package com.forge.server.plugins.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class PluginClassLoader extends URLClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(PluginClassLoader.class);

    public PluginClassLoader(String pluginName, Path pluginJarPath, ClassLoader parent) throws IOException {
        super(new URL[] { pluginJarPath.toUri().toURL() }, parent);
        logger.debug("Created PluginClassLoader for plugin: {} from: {}", pluginName, pluginJarPath);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> cachedClass = findLoadedClass(name);
        if (cachedClass != null) {
            if (resolve) {
                resolveClass(cachedClass);
            }
            return cachedClass;
        }
        return super.loadClass(name, resolve);
    }
}
