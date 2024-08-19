package com.elderlink.backend.auth.services.impl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
    }

    @Test
    void testGenerateToken() {
        // Create a UserDetails object representing a user
        UserDetails userDetails = new User("user@example.com", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        // Generate a JWT token for the user
        String token = jwtService.generateToken(userDetails);
        // Ensure that the generated token is not null
        assertNotNull(token);
        // Extract the username from the generated token and assert its correctness
        String username = jwtService.extractUsername(token);
        assertEquals("user@example.com", username);
    }

    @Test
    void testIsTokenValid() {
        // Create a UserDetails object representing a user
        UserDetails userDetails = new User("user@example.com", "password", Collections.emptyList());
        // Generate a JWT token for the user
        String token = jwtService.generateToken(userDetails);
        // Validate the generated token against the UserDetails object
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testExtractExpiration() {
        // Generate a JWT token for a user
        String token = jwtService.generateToken(new User("user@example.com", "password", Collections.emptyList()));
        // Extract the expiration date from the token
        Date expiration = jwtService.extractExpiration(token);
        // Ensure that the extracted expiration date is not null and after the current date
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testExtractClaim() {
        // Create a UserDetails object representing a user
        UserDetails userDetails = new User("user@example.com", "password", Collections.emptyList());
        // Generate a JWT token for the user
        String token = jwtService.generateToken(userDetails);
        // Extract the "issuedAt" claim from the token
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        // Ensure that the extracted issuedAt date is not null
        assertNotNull(issuedAt);
    }
}
