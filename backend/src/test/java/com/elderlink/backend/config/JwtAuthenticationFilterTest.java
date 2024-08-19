package com.elderlink.backend.config;
import com.elderlink.backend.auth.services.JwtService;
import com.elderlink.backend.config.JwtAuthenticationFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import jakarta.servlet.FilterChain;
import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void whenNoAuthHeader_thenContinueChain() throws IOException, ServletException {
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void whenInvalidAuthHeader_thenContinueChain() throws IOException, ServletException {
        request.addHeader("Authorization", "Invalid Bearer token");
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void whenMalformedToken_thenUnauthorizedResponse() throws IOException, ServletException {
        request.addHeader("Authorization", "Bearer malformed.token.here");
        doThrow(new MalformedJwtException("Malformed JWT token")).when(jwtService).extractUsername(anyString());
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("User is Unauthorized."));
    }

    @Test
    void whenExpiredToken_thenUnauthorizedResponse() throws IOException, ServletException {
        request.addHeader("Authorization", "Bearer expired.token.here");
        doThrow(new ExpiredJwtException(null, null, "JWT Token is expired")).when(jwtService).extractUsername(anyString());
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("User is Unauthorized."));
    }
//
//    @Test
//    void whenValidToken_thenAuthenticate() throws IOException, ServletException {
//        String validToken = "valid.token.here";
//        UserDetails userDetails = new User("user@example.com", "", Collections.emptyList());
//        request.addHeader("Authorization", "Bearer " + validToken);
//        when(jwtService.extractUsername(validToken)).thenReturn("user@example.com");
//        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
//        when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);
//        jwtAuthenticationFilter.doFilter(request, response, filterChain);
//        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
//        verify(jwtService, times(1)).isTokenValid(anyString(), any(UserDetails.class));
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
// @Test
// void whenValidToken_thenAuthenticate() throws IOException, ServletException {
//     // Arrange
//     String validToken = "valid.token.here";
//     UserDetails userDetails = new User("user@example.com", "", Collections.emptyList());

//     // Mock setup
//     when(jwtService.extractUsername(validToken)).thenReturn("user@example.com");
//     when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
//     when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);

//     // Act
//     request.addHeader("Authorization", "Bearer " + validToken);
//     jwtAuthenticationFilter.doFilter(request, response, filterChain);

//     // Assert
//     verify(userDetailsService, times(1)).loadUserByUsername("user@example.com");
//     verify(jwtService, times(1)).isTokenValid(validToken, userDetails);
//     verify(filterChain, times(1)).doFilter(request, response);
// }

//<<<<<<< HEAD
//    @Test
//    void whenValidToken_thenAuthenticate() throws IOException, ServletException {
//        // Given a valid token in the Authorization header
//        String validToken = "valid.token.here";
//        UserDetails userDetails = new User("user@example.com", "", Collections.emptyList());
//        request.addHeader("Authorization", "Bearer " + validToken);
//        when(jwtService.extractUsername(validToken)).thenReturn("user@example.com");
//        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
//        when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);
//        jwtAuthenticationFilter.doFilter(request, response, filterChain);
//        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
//        verify(jwtService, times(1)).isTokenValid(anyString(), any(UserDetails.class));
//        verify(filterChain, times(1)).doFilter(request, response);
//    }
//=======
//>>>>>>> 3772ccef2b15ff86dd749e135145f87e49ceb4dd

}
