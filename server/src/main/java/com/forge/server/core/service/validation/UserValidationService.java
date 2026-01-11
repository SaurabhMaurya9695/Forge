package com.forge.server.core.service.validation;

import com.forge.server.common.exception.UserAlreadyExistsException;
import com.forge.server.core.repository.UserRepository;

import org.springframework.stereotype.Service;

/**
 *
 * @author Forge Team
 */
@Service
public class UserValidationService {

    public static final String USERNAME_S_IS_ALREADY_TAKEN = "Username '%s' is already taken";
    public static final String EMAIL_S_IS_ALREADY_REGISTERED = "Email '%s' is already registered";
    private final UserRepository userRepository;

    public UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates that username is unique
     *
     * @param username username to validate
     * @throws UserAlreadyExistsException if username already exists
     */
    public void validateUsernameUnique(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(String.format(USERNAME_S_IS_ALREADY_TAKEN, username));
        }
    }

    /**
     * Validates that email is unique
     *
     * @param email email to validate
     * @throws UserAlreadyExistsException if email already exists
     */
    public void validateEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(String.format(EMAIL_S_IS_ALREADY_REGISTERED, email));
        }
    }

    /**
     * Validates both username and email are unique
     *
     * @param username username to validate
     * @param email    email to validate
     * @throws UserAlreadyExistsException if either username or email already exists
     */
    public void validateUserUnique(String username, String email) {
        validateUsernameUnique(username);
        validateEmailUnique(email);
    }
}

