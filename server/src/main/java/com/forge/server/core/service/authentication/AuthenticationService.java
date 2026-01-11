package com.forge.server.core.service.authentication;

import com.forge.shared.model.response.LoginResponse;
import com.forge.server.common.exception.InvalidCredentialsException;
import com.forge.server.core.entity.User;
import com.forge.server.core.service.PasswordEncoderService;
import com.forge.server.core.service.UserService;
import com.forge.server.security.provider.JwtTokenProvider;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Forge Team
 */
@Service
public class AuthenticationService {

    private static final String ERROR_INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";

    private final UserService userService;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(UserService userService, PasswordEncoderService passwordEncoderService,
            JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoderService = passwordEncoderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Authenticates user and generates JWT tokens
     *
     * @param email    user email
     * @param password plain text password
     * @return LoginResponse containing user info and tokens
     * @throws InvalidCredentialsException if credentials are invalid
     */
    public LoginResponse authenticate(String email, String password) {
        User user = userService.findByEmail(email).orElseThrow(
                () -> new InvalidCredentialsException(ERROR_INVALID_EMAIL_OR_PASSWORD));

        if (!passwordEncoderService.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException(ERROR_INVALID_EMAIL_OR_PASSWORD);
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
        response.setLoginTime(LocalDateTime.now());

        return response;
    }
}

