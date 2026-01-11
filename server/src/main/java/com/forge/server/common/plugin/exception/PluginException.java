package com.forge.server.common.plugin.exception;

import java.io.Serial;

public class PluginException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PluginException(String message) {
        super(message);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(Throwable cause) {
        super(cause);
    }
}
