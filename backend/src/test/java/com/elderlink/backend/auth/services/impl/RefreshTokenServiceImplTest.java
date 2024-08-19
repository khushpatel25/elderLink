package com.elderlink.backend.services.impl;
import com.elderlink.backend.auth.services.impl.RefreshTokenServiceImpl;
import com.elderlink.backend.domains.entities.RefreshTokenEntity;
import com.elderlink.backend.domains.entities.UserEntity;
import com.elderlink.backend.repositories.RefreshTokenRepository;
import com.elderlink.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.lang.reflect.Field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private final String userEmail = "user@example.com";

    @BeforeEach
    void setUp() {
        // Setup common mocking behavior here
    }
    // Test case to ensure that a refresh token is created and saved successfully
    @Test
    void whenCreateRefreshToken_thenSaveAndReturnToken() {
        // Prepare a mock UserEntity
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(userEmail);
// Mock the UserRepository to return the mockUser when findByEmail is called
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        // Mock the RefreshTokenRepository to return the created RefreshTokenEntity
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenAnswer(i -> i.getArguments()[0]);
        // Call the method under test to create a refresh token
        RefreshTokenEntity createdToken = refreshTokenService.createRefreshToken(userEmail);
        // Assertions
        assertNotNull(createdToken);
        assertNotNull(createdToken.getRefreshToken());
        assertTrue(createdToken.getExpirationTime().isAfter(Instant.now()));
        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
    }
    // Test case to ensure that a valid refresh token is successfully verified
    @Test
    void whenVerifyValidRefreshToken_thenSuccess() throws NoSuchFieldException, IllegalAccessException {
        // Generate a random refresh token string
        String refreshTokenStr = UUID.randomUUID().toString();

        // Use reflection to access the private field
        Field expirationTimeField = RefreshTokenServiceImpl.class.getDeclaredField("refreshTokenExpirationTime");
        expirationTimeField.setAccessible(true); // Make the field accessible
        long refreshTokenExpirationTime = (long) expirationTimeField.get(refreshTokenService); // Get value from the service instance
        // Create a RefreshTokenEntity with the random token and future expiration time
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .refreshToken(refreshTokenStr)
                .expirationTime(Instant.now().plusMillis(refreshTokenExpirationTime))
                .user(new UserEntity())
                .build();
        // Mock the RefreshTokenRepository to return the refreshTokenEntity when findByRefreshToken is called

        when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(refreshTokenEntity));
        // Call the method under test to verify the refresh token
        RefreshTokenEntity verifiedToken = refreshTokenService.verifyRefreshToken(refreshTokenStr);

        assertNotNull(verifiedToken);
        assertEquals(refreshTokenStr, verifiedToken.getRefreshToken());
    }
    // Test case to ensure that an expired refresh token throws an exception upon verification
    @Test
    void whenVerifyExpiredRefreshToken_thenThrowException() {
        // Generate a random expired refresh token string
        String expiredToken = UUID.randomUUID().toString();
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .refreshToken(expiredToken)
                .expirationTime(Instant.now().minusMillis(100000))
                .user(new UserEntity())
                .build();
        // Mock the RefreshTokenRepository to return the refreshTokenEntity when findByRefreshToken is called
        when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(refreshTokenEntity));
// Call the method under test and assert that it throws the expected exception
        Exception exception = assertThrows(RuntimeException.class, () -> refreshTokenService.verifyRefreshToken(expiredToken));
        assertEquals("Error occurred while verifying the refreshToken. -> RefreshToken is expired!", exception.getMessage());
    }


    @Test
    void whenDeleteRefreshToken_thenTokenIsDeleted() {
        // Generate a random refresh token string
        String refreshTokenStr = UUID.randomUUID().toString();
        // Create a RefreshTokenEntity with the generated token
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .id(1L)
                .refreshToken(refreshTokenStr)
                .user(new UserEntity())
                .build();
        // Mock the RefreshTokenRepository to return the refreshTokenEntity when findByRefreshToken is called
        when(refreshTokenRepository.findByRefreshToken(anyString())).thenReturn(Optional.of(refreshTokenEntity));
        // Mock the deleteById method of RefreshTokenRepository to do nothing
        doNothing().when(refreshTokenRepository).deleteById(anyLong());
// Call the method under test to delete the refresh token
        refreshTokenService.deleteRefreshToken(refreshTokenStr);
// Verify that deleteById method of RefreshTokenRepository was called with the correct token ID
        verify(refreshTokenRepository).deleteById(refreshTokenEntity.getId());
    }

    @Test
    void whenCreateRefreshTokenForNonExistentUser_thenThrowUsernameNotFoundException() {
        // Generate a random non-existent refresh token string
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Mock the RefreshTokenRepository to return Optional.empty() indicating non-existent token
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> refreshTokenService.createRefreshToken(userEmail));
        // Call the method under test and assert that it throws the expected exception
        assertEquals("User with this email doesn't exist!", exception.getMessage());
    }

    @Test
    void whenVerifyNonExistentRefreshToken_thenThrowEntityNotFoundException() {
        // Generate a random non-existent refresh token string
        String nonExistentToken = UUID.randomUUID().toString();
        // Mock the RefreshTokenRepository to return Optional.empty() indicating non-existent token
        when(refreshTokenRepository.findByRefreshToken(nonExistentToken)).thenReturn(Optional.empty());
        // Call the method under test and assert that it throws the expected exception
        Exception exception = assertThrows(EntityNotFoundException.class, () -> refreshTokenService.verifyRefreshToken(nonExistentToken));
        assertEquals("RefreshToken isn't found!", exception.getMessage());
    }

    @Test
    void whenDeleteNonExistentRefreshToken_thenThrowEntityNotFoundException() {
        // Generate a valid refresh token string
        String nonExistentToken = UUID.randomUUID().toString();
        // Create a RefreshTokenEntity with the generated token
        when(refreshTokenRepository.findByRefreshToken(nonExistentToken)).thenReturn(Optional.empty());
        // Mock the RefreshTokenRepository to return the refreshTokenEntity when findByRefreshToken is called
        Exception exception = assertThrows(EntityNotFoundException.class, () -> refreshTokenService.deleteRefreshToken(nonExistentToken));
        // Mock the deleteById method of RefreshTokenRepository to throw a RuntimeException
        assertEquals("RefreshToken is not found!", exception.getMessage());
    }

    @Test
    void whenDeleteRefreshTokenThrowsUnexpectedException_thenThrowRuntimeException() {
        // Generate a valid refresh token string
        String validToken = UUID.randomUUID().toString();
        // Create a RefreshTokenEntity with the generated token
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .id(1L)
                .refreshToken(validToken)
                .user(new UserEntity())
                .build();
//Mock the RefreshTokenRepository to return the refreshTokenEntity when findByRefreshToken is called
        when(refreshTokenRepository.findByRefreshToken(validToken)).thenReturn(Optional.of(refreshTokenEntity));
        doThrow(new RuntimeException("Database error")).when(refreshTokenRepository).deleteById(refreshTokenEntity.getId());
// Mock the deleteById method of RefreshTokenRepository to throw a RuntimeException
        Exception exception = assertThrows(RuntimeException.class, () -> refreshTokenService.deleteRefreshToken(validToken));
        // Call the method under test and assert that it throws the expected exception
        assertTrue(exception.getMessage().contains("Error occurred while deleting the refreshToken. -> Database error"));
    }

    @Test
    void whenCreateRefreshTokenAndSaveFails_thenThrowRuntimeException() {
        String userEmail = "user@example.com";
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> refreshTokenService.createRefreshToken(userEmail), "Error occurred while creating refreshToken. -> Database error");
    }

    @Test
    void whenDeleteRefreshTokenAndDeleteFails_thenThrowRuntimeException() {
        String refreshTokenStr = UUID.randomUUID().toString();
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefreshToken(refreshTokenStr);

        when(refreshTokenRepository.findByRefreshToken(refreshTokenStr)).thenReturn(Optional.of(refreshTokenEntity));
        doThrow(new RuntimeException("Database error")).when(refreshTokenRepository).deleteById(refreshTokenEntity.getId());

        assertThrows(RuntimeException.class, () -> refreshTokenService.deleteRefreshToken(refreshTokenStr), "Error occurred while deleting the refreshToken. -> Database error");
    }





}
