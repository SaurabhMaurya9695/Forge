package com.forge.server.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Application User Details
 * <p>
 * Implements UserDetails for Spring Security authentication.
 */
public class AppUserDetails implements UserDetails {

    private final String id;
    private final String email;
    private final String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;

    /**
     * Constructor for AppUserDetails
     *
     * @param id          user ID
     * @param email       user email
     * @param password    user password hash
     * @param name        user name
     * @param authorities user authorities
     */
    public AppUserDetails(String id, String email, String password, String name, Set<GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = name;
        this.authorities = authorities;
    }

    /**
     * Gets user authorities
     *
     * @return collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Gets user password hash
     *
     * @return password hash
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets username
     *
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Gets user ID
     *
     * @return user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets user email
     *
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Checks if account is non-expired
     *
     * @return true if account is non-expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if account is non-locked
     *
     * @return true if account is non-locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if credentials are non-expired
     *
     * @return true if credentials are non-expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if account is enabled
     *
     * @return true if account is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
