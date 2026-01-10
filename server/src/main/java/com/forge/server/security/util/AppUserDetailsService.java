package com.forge.server.security.util;

import com.forge.common.constants.PasswordConstants;
import com.forge.server.core.entity.User;
import com.forge.server.core.repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * Application User Details Service
 * <p>
 * Loads user details for Spring Security authentication.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor for AppUserDetailsService
     *
     * @param userRepository user repository
     */
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user by email (username)
     *
     * @param email user email
     * @return UserDetails for the user
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(this::buildUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Loads user by user ID
     *
     * @param userId user ID
     * @return UserDetails for the user
     * @throws UsernameNotFoundException if user not found
     */
    public UserDetails loadUserById(UUID userId) throws UsernameNotFoundException {
        return userRepository.findById(userId)
                .map(this::buildUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Builds UserDetails from User entity
     *
     * @param user user entity
     * @return UserDetails instance
     */
    private UserDetails buildUserDetails(User user) {
        Set<GrantedAuthority> authorities = Set.of(
                new SimpleGrantedAuthority(PasswordConstants.ROLE_PREFIX + user.getRole().name())
        );

        return new AppUserDetails(
                user.getId().toString(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getUsername(),
                authorities
        );
    }
}
