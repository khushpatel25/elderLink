package com.elderlink.backend.controllers;
import com.elderlink.backend.auth.services.AuthService;
import com.elderlink.backend.auth.services.JwtService;
import com.elderlink.backend.auth.services.RefreshTokenService;
import com.elderlink.backend.auth.utils.*;
import com.elderlink.backend.domains.entities.RefreshTokenEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.domains.enums.UserType;
import com.elderlink.backend.exceptions.UserAlreadyExistException;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerUser_Success() {
        RegReq regReq = new RegReq("John", "Doe", "john.doe@example.com", "123-456-7890", "password", LocalDate.of(1980, 1, 1), null);
        UserEntity userEntity = new UserEntity();
        when(modelMapper.map(any(RegReq.class), eq(UserEntity.class))).thenReturn(userEntity);
        when(authService.userRegister(any(UserEntity.class))).thenReturn(new AuthRes("accessToken", "refreshToken"));

        ResponseEntity<Object> response = authController.registerUser(regReq);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void registerUser_UserAlreadyExists() {
        RegReq regReq = new RegReq();
        regReq.setEmail("user@example.com"); // Set a non-null email here.

        doThrow(new UserAlreadyExistException("User already exists")).when(userService).isUserExistByEmail("user@example.com");

        ResponseEntity<Object> response = authController.registerUser(regReq);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }


    @Test
    void logOut_Success() {
        LogoutReq logoutReq = new LogoutReq("dummyRefreshToken");

        ResponseEntity response = authController.logOut(logoutReq);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void authenticateUser_Success() {
        AuthReq authReq = new AuthReq("john.doe@example.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(authService.userAuth(any(AuthReq.class))).thenReturn(new AuthRes("accessToken", "refreshToken"));

        ResponseEntity<Object> response = authController.authenticateUser(authReq);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void refreshToken_Success() {
        RefreshTokenReq refreshTokenReq = new RefreshTokenReq("dummyRefreshToken");
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(new UserEntity());
        when(refreshTokenService.verifyRefreshToken(anyString())).thenReturn(refreshTokenEntity);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("newAccessToken");

        ResponseEntity<Object> response = authController.refreshToken(refreshTokenReq);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    void logOut_WhenExceptionThrown() {
        LogoutReq logoutReq = new LogoutReq("invalidRefreshToken");

        doThrow(new RuntimeException("Some error occurred")).when(refreshTokenService).deleteRefreshToken(anyString());

        ResponseEntity response = authController.logOut(logoutReq);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void authenticateUser_UserNotFound() {
        AuthReq authReq = new AuthReq("nonexistent@example.com", "password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = authController.authenticateUser(authReq);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User with this email doesn't exist."));
    }

    @Test
    void authenticateUser_WhenGeneralExceptionOccurs() {
        AuthReq authReq = new AuthReq("error@example.com", "password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        when(authService.userAuth(any(AuthReq.class))).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Object> response = authController.authenticateUser(authReq);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Unexpected error"));
    }

    @Test
    void refreshToken_WhenExceptionThrown() {
        RefreshTokenReq refreshTokenReq = new RefreshTokenReq("invalidRefreshToken");

        // Simulate an exception when verifying the refresh token
        doThrow(new RuntimeException("Some error occurred")).when(refreshTokenService).verifyRefreshToken(anyString());

        ResponseEntity<Object> response = authController.refreshToken(refreshTokenReq);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Some error occurred"));
    }

}
