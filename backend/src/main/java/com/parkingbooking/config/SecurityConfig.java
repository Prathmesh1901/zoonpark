package com.parkingbooking.config;

import com.parkingbooking.security.CustomUserDetailsService;
import com.parkingbooking.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SecurityConfig - Spring Security configuration
 * 
 * This is the CENTRAL configuration for application security
 * 
 * Key Concepts:
 * 1. Authentication: Who are you? (Login)
 * 2. Authorization: What can you do? (Permissions)
 * 3. Password Encoding: Never store plain passwords
 * 4. JWT Filter: Validate tokens on each request
 * 5. CORS: Allow frontend to call backend APIs
 * 6. Stateless Sessions: No server-side session storage
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password encoder bean
     * 
     * BCrypt is a strong hashing algorithm
     * - One-way encryption (cannot be reversed)
     * - Includes salt (prevents rainbow table attacks)
     * - Adaptive (can increase complexity over time)
     * 
     * When user registers: password -> BCrypt -> hash stored in DB
     * When user logs in: password -> BCrypt -> compare with stored hash
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication provider
     * 
     * Tells Spring Security:
     * - How to load users (userDetailsService)
     * - How to verify passwords (passwordEncoder)
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication manager
     * 
     * Used in login process to authenticate users
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * CORS configuration
     * 
     * CORS (Cross-Origin Resource Sharing) allows frontend to call backend
     * 
     * Without CORS:
     * - Frontend: http://localhost:5173
     * - Backend: http://localhost:8080
     * - Browser blocks requests (different origins)
     * 
     * With CORS:
     * - We explicitly allow frontend origin
     * - Browser allows requests
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:5173", "http://localhost:3000", "https://zoonpark.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Security filter chain - Main security configuration
     * 
     * This defines:
     * - Which endpoints are public (no authentication needed)
     * - Which endpoints require authentication
     * - Which endpoints require specific roles
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (not needed for JWT-based APIs)
                .csrf(csrf -> csrf.disable())

                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no authentication required)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/parking-spaces").permitAll() // Allow
                                                                                                                 // search
                                                                                                                 // for
                                                                                                                 // everyone
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/parking-spaces/{id}").permitAll() // Allow
                                                                                                                      // viewing
                                                                                                                      // details

                        // Admin-only endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Owner-only endpoints
                        .requestMatchers("/parking-spaces/my-spaces").hasRole("OWNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/parking-spaces").hasRole("OWNER")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/parking-spaces/**").hasRole("OWNER")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/parking-spaces/**")
                        .hasRole("OWNER")

                        // All other endpoints require authentication
                        .anyRequest().authenticated())

                // Stateless session (no server-side sessions)
                // Each request must include JWT token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set authentication provider
                .authenticationProvider(authenticationProvider())

                // Add JWT filter before Spring Security's authentication filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
