package com.parkingbooking.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter - Intercepts requests and validates JWT tokens
 * 
 * This filter runs for EVERY request to protected endpoints
 * 
 * Flow:
 * 1. Client sends request with Authorization header
 * 2. Filter extracts JWT token from header
 * 3. Filter validates token
 * 4. If valid, filter loads user details
 * 5. Filter sets authentication in Spring Security context
 * 6. Request proceeds to controller
 * 
 * Authorization Header Format:
 * "Bearer
 * eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDA5OTg4MDB9.signature"
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * This is the JWT token
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Filter method that runs for each request
     * 
     * @param request     HTTP request
     * @param response    HTTP response
     * @param filterChain Filter chain to continue processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            // Step 1: Extract JWT token from request header
            String jwt = getJwtFromRequest(request);

            // Step 2: Validate token and set authentication
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Step 3: Get email from token
                String email = tokenProvider.getEmailFromToken(jwt);

                // Step 4: Load user details from database
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                // Step 5: Create authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Step 6: Set authentication in Spring Security context
                // Now Spring Security knows who the user is!
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        // Continue with the request
        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header
     * 
     * Header format: "Bearer <token>"
     * We need to remove "Bearer " prefix
     * 
     * @param request HTTP request
     * @return JWT token string or null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Check if header exists and starts with "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Remove "Bearer " prefix and return token
            return bearerToken.substring(7);
        }

        return null;
    }
}
