package com.forge.server.security.config;

import com.forge.common.constants.ApiConstants;
import com.forge.common.constants.SecurityConstants;
import com.forge.server.security.filter.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security Configuration
 * <p>
 * Configures security settings including CORS, JWT authentication, and endpoint authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor for SecurityConfig
     *
     * @param jwtAuthenticationFilter JWT authentication filter
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the security filter chain
     *
     * @param http HttpSecurity builder
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(configureCorsSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, ApiConstants.FULL_REGISTER_PATH, ApiConstants.FULL_LOGIN_PATH).permitAll()
                        .requestMatchers(ApiConstants.API_HEALTH_PATH + "/**", ApiConstants.ACTUATOR_PATH).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configures CORS settings
     *
     * @return CORS configuration source
     */
    private CorsConfigurationSource configureCorsSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(SecurityConstants.ALLOWED_ORIGINS);
        corsConfiguration.setAllowedMethods(SecurityConstants.ALLOWED_METHODS);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setExposedHeaders(SecurityConstants.EXPOSED_HEADERS);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(SecurityConstants.CORS_MAX_AGE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
