package com.parkingbooking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtTokenProvider - Handles JWT token generation and validation
 * 
 * JWT (JSON Web Token) is used for stateless authentication
 * 
 * How it works:
 * 1. User logs in with email/password
 * 2. Server validates credentials
 * 3. Server generates JWT token containing user info
 * 4. Client stores token (usually in localStorage)
 * 5. Client sends token in Authorization header for each request
 * 6. Server validates token and extracts user info
 * 
 * JWT Structure:
 * - Header: Algorithm and token type
 * - Payload: User data (email, role, etc.)
 * - Signature: Ensures token hasn't been tampered with
 */
@Component
public class JwtTokenProvider {

    /**
     * Secret key for signing tokens
     * Loaded from application.properties
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Token expiration time in milliseconds
     * Default: 86400000 ms = 24 hours
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generate signing key from secret
     * 
     * @return Cryptographic key for signing
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate JWT token for authenticated user
     * 
     * @param authentication Spring Security authentication object
     * @return JWT token string
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Usually email
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generate token from email (alternative method)
     * 
     * @param email User's email
     * @return JWT token string
     */
    public String generateTokenFromEmail(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Extract email from JWT token
     * 
     * @param token JWT token
     * @return User's email
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Validate JWT token
     * 
     * Checks if:
     * - Token signature is valid
     * - Token is not expired
     * - Token format is correct
     * 
     * @param token JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty");
        }
        return false;
    }
}
