package com.forge.server.core.service;

import com.forge.server.common.exception.UserAlreadyExistsException;
import com.forge.server.core.entity.User;
import com.forge.server.core.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Service
 * <p>
 * Business logic layer for user operations.
 * Follows Single Responsibility Principle - handles user-related business logic.
 * Uses Transactional annotation for data consistency.
 *
 * @author Forge Team
 */
@Service
@Transactional
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    /**
     * Register a new user
     * <p>
     * Validates that username and email are unique before creating the user.
     * Encodes the password before storing it.
     *
     * @param username the username
     * @param email    the email address
     * @param password the plain text password
     * @return the created user entity
     * @throws UserAlreadyExistsException if username or email already exists
     */
    @Override
    public User registerUser(String username, String email, String password) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username '" + username + "' is already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email '" + email + "' is already registered");
        }

        // Encode password
        String passwordHash = passwordEncoderService.encode(password);

        // Create and save user
        User user = new User(username, email, passwordHash);
        return userRepository.save(user);
    }

    /**
     * Find user by username
     *
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public java.util.Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email
     *
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    @Transactional(readOnly = true)
    public java.util.Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

