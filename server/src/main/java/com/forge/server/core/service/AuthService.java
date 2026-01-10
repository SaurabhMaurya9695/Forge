package com.forge.server.core.service;

import com.forge.common.constants.EntityConstants;
import com.forge.common.constants.MessageConstants;
import com.forge.server.api.models.response.LoginResponse;
import com.forge.server.api.models.response.RegisterResponse;
import com.forge.server.common.exception.InvalidCredentialsException;
import com.forge.server.core.entity.User;
import com.forge.server.security.provider.JwtTokenProvider;

import org.springframework.stereotype.Service;

/**
 * Authentication Service
 * <p>
 * Service responsible for authentication-related operations.
 * Acts as a facade between the API layer and business logic layer.
 * Follows the Facade pattern and Single Responsibility Principle.
 *
 * @author Forge Team
 */
@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructor for AuthService
     *
     * @param userService            user service
     * @param passwordEncoderService password encoder service
     * @param jwtTokenProvider       JWT token provider
     */
    public AuthService(UserService userService, PasswordEncoderService passwordEncoderService,
                      JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoderService = passwordEncoderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user
     *
     * @param username username
     * @param email    email address
     * @param password plain text password
     * @return RegisterResponse containing user information
     */
    public RegisterResponse register(String username, String email, String password) {
        User user = userService.registerUser(username, email, password);
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setCreatedAt(user.getCreatedAt());
        response.setMessage(MessageConstants.USER_REGISTERED_SUCCESSFULLY);
        return response;
    }

    /**
     * Authenticates user and generates JWT tokens
     *
     * @param email    user email
     * @param password plain text password
     * @return LoginResponse containing user info and tokens
     * @throws InvalidCredentialsException if credentials are invalid
     */
    public LoginResponse login(String email, String password) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(MessageConstants.ERROR_INVALID_EMAIL_OR_PASSWORD));

        if (!passwordEncoderService.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException(MessageConstants.ERROR_INVALID_EMAIL_OR_PASSWORD);
        }

        String userId = user.getId().toString();
        String accessToken = jwtTokenProvider.generateToken(userId, user.getEmail(), user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(86400L); // 24 hours in seconds
        response.setLoginTime(java.time.LocalDateTime.now());

        return response;
    }
}
