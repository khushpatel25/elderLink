package com.elderlink.backend.services.impl;

import com.elderlink.backend.auth.services.impl.AuthServiceImpl;
import com.elderlink.backend.auth.services.JwtService;
import com.elderlink.backend.auth.services.RefreshTokenService;
import com.elderlink.backend.auth.utils.AuthReq;
import com.elderlink.backend.auth.utils.AuthRes;
import com.elderlink.backend.domains.entities.RefreshTokenEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.domains.enums.UserType;
import com.elderlink.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private final String accessToken = "access-token";
    private final String refreshToken = "refresh-token";

    @BeforeEach
    void setUp() {
        // Mock the behavior of jwtService and refreshTokenService
        lenient().when(jwtService.generateToken(any(UserEntity.class))).thenReturn(accessToken);
        RefreshTokenEntity mockRefreshTokenEntity = mock(RefreshTokenEntity.class);
        lenient().when(mockRefreshTokenEntity.getRefreshToken()).thenReturn(refreshToken);
        lenient().when(refreshTokenService.createRefreshToken(anyString())).thenReturn(mockRefreshTokenEntity);
    }


    @Test
    @DisplayName("Successful Elder Person Registration")
    void testSuccessfulElderPersonRegistration() {
        // Test registration for an elder person
        UserEntity user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setBirthDate(LocalDate.now().minusYears(65));
        user.setUserType(UserType.ELDER_PERSON);

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        AuthRes response = authService.userRegister(user);
        // Assert that the response contains valid access and refresh tokens
        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        // Verify that userRepository.save was called once with any UserEntity parameter
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Successful Volunteer Registration")
    void testSuccessfulVolunteerRegistration() {
        // Create a user entity for a volunteer
        UserEntity user = new UserEntity();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("password");
        user.setBirthDate(LocalDate.now().minusYears(25));
        user.setUserType(UserType.VOLUNTEER);
        // Mock the userRepository save method to return the user entity
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        // Mock the password encoder to return an encoded password
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        AuthRes response = authService.userRegister(user);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
        // Verify that userRepository.save was called once with any UserEntity parameter
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Successful Authentication")
    void testSuccessfulAuthentication() {
        // Create an authentication request with valid email and password
        AuthReq authReq = new AuthReq("user@example.com", "password");
        // Create a user entity with the provided email and a password encoded with the password encoder
        UserEntity user = new UserEntity();
        user.setEmail(authReq.getEmail());
        user.setPassword(passwordEncoder.encode(authReq.getPassword()));

        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // Mocking authenticate to simply not throw an exception

        AuthRes response = authService.userAuth(authReq);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    @DisplayName("Authentication With Invalid Credentials")
    void testAuthenticationWithInvalidCredentials() {
        // Create an authentication request with invalid password
        AuthReq authReq = new AuthReq("user@example.com", "wrongpassword");
        // Mock the authentication manager to throw a runtime exception with message "Bad credentials"
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));
        // Assert that a runtime exception with message "Bad credentials" is thrown
        Exception exception = assertThrows(RuntimeException.class, () -> authService.userAuth(authReq));
        assertTrue(exception.getMessage().contains("Bad credentials"));
    }
}
