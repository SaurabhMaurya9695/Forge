package com.forge.server.common.exception;

import com.forge.common.constants.MessageConstants;
import com.forge.server.common.plugin.exception.PluginException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 * <p>
 * Handles exceptions across all controllers and provides consistent error responses.
 * Follows the Single Responsibility Principle by centralizing exception handling.
 *
 * @author Forge Team
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_VALIDATION_FAILED = "Validation failed";
    private static final String ERROR_UNEXPECTED = "An unexpected error occurred: %s";

    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put(MessageConstants.STATUS, MessageConstants.STATUS_ERROR);
        response.put(MessageConstants.MESSAGE, ERROR_VALIDATION_FAILED);
        response.put(MessageConstants.STATUS_ERROR, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle user already exists exception
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(MessageConstants.STATUS, MessageConstants.STATUS_ERROR);
        response.put(MessageConstants.MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handle invalid credentials exception
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(MessageConstants.STATUS, MessageConstants.STATUS_ERROR);
        response.put(MessageConstants.MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handle plugin exceptions
     */
    @ExceptionHandler(PluginException.class)
    public ResponseEntity<Map<String, Object>> handlePluginException(PluginException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(MessageConstants.STATUS, MessageConstants.STATUS_ERROR);
        response.put(MessageConstants.MESSAGE, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle generic runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(MessageConstants.STATUS, MessageConstants.STATUS_ERROR);
        response.put(MessageConstants.MESSAGE, String.format(ERROR_UNEXPECTED, ex.getMessage()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

