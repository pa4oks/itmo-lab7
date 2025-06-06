package ru.se.ifmo.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.se.ifmo.lab.ResponseWrapper;

/**
 * Default handler for ResponseWrapper: logs outcome and returns data or null.
 */
public final class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    private ResponseHandler() {
    }

    /**
     * Handles a ResponseWrapper by logging success or error.
     *
     * @param response the ResponseWrapper to handle
     * @param <T>      type of payload
     */
    public static <T> void handle(ResponseWrapper<T> response) {
        if (response == null) {
            logger.error("No response received");
            return;
        }
        if (response.success()) {
            logger.info("Operation succeeded, data = {}", response.data());
        } else {
            logger.error("Operation failed: {}", response.message());
        }
    }
}
