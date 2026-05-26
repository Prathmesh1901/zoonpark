package com.parkingbooking.security;

import com.parkingbooking.entity.User;
import com.parkingbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * CustomUserDetailsService - Loads user data for Spring Security
 * 
 * Spring Security needs to know how to load user information
 * This service tells Spring Security how to fetch user from database
 * 
 * When a user logs in:
 * 1. Spring Security calls loadUserByUsername()
 * 2. We fetch user from database
 * 3. We convert our User entity to Spring Security's UserDetails
 * 4. Spring Security compares passwords
 * 5. If match, authentication succeeds
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user by email (username in our case is email)
     * 
     * @param email User's email
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user));
    }

    /**
     * Convert user role to Spring Security authorities
     * 
     * Authorities determine what the user can access
     * Format: "ROLE_" + role name
     * Example: "ROLE_CUSTOMER", "ROLE_OWNER", "ROLE_ADMIN"
     * 
     * @param user User entity
     * @return Collection of granted authorities
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
    }
}
