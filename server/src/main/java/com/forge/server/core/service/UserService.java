package com.forge.server.core.service;

import com.forge.server.core.entity.User;
import com.forge.server.core.repository.UserRepository;
import com.forge.server.core.service.validation.UserValidationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Forge Team
 */
@Service
@Transactional
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final UserValidationService validationService;

    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService,
            UserValidationService validationService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.validationService = validationService;
    }

    /**
     * Register a new user
     * <p>
     * Validates that username and email are unique before creating the user.
     * Encodes the password before storing it.
     * Sets default role (business logic moved from entity to service).
     *
     * @param username the username
     * @param email    the email address
     * @param password the plain text password
     * @return the created user entity
     */
    @Override
    public User registerUser(String username, String email, String password) {
        validationService.validateUserUnique(username, email);
        String passwordHash = passwordEncoderService.encode(password);
        User user = new User(username, email, passwordHash);
        user.setRole(User.UserRole.DEVELOPER);
        return userRepository.save(user);
    }

    /**
     * Find user by email
     *
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

